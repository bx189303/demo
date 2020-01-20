package haidian.chat.redis.config;

import haidian.chat.controller.WebSocketController;
import haidian.chat.redis.listen.ListenAndSend;
import haidian.chat.redis.listen.SaveMysql;
import haidian.chat.redis.listen.SendInWebsocketoff;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

/**
 * 监听redis配置,需添加三处（参数，添加监听，bean）
 */
@Configuration
public class RedisListenerConfig {

    /**
     * redis消息监听器容器
     * 可以添加多个监听不同话题的redis监听器，只需要把消息监听器和相应的消息订阅处理器绑定，该消息监听器
     * 通过反射技术调用消息订阅处理器的相关方法进行一些业务处理
     * @param connectionFactory
     * @param listenerAdapter
     * @return
     */
    @Bean
    RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,
                                            MessageListenerAdapter listenerAdapter,
                                            MessageListenerAdapter sendUser,
                                            MessageListenerAdapter saveInMysql,
                                            MessageListenerAdapter sendIndexApi
    ) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);

        //可以添加多个 messageListener
        //监听RECEIVE,处理接收到的消息并保存redis
        container.addMessageListener(listenerAdapter, new PatternTopic("RECEIVE"));
        //监听RECEIVE,保存接收到的消息
        container.addMessageListener(saveInMysql, new PatternTopic("RECEIVE"));
        //监听DISPATCH,通过websocket发送
        container.addMessageListener(sendUser, new PatternTopic("DISPATCH"));
        //监听监听DISPATCH,如果websocket不在线，则发送第三方接口(门户)
        container.addMessageListener(sendIndexApi, new PatternTopic("DISPATCH"));
        return container;
    }


    /**
     * 消息监听器适配器，绑定消息处理器，利用反射技术调用消息处理器的业务方法
     * @param redisReceiver
     * @return
     */
    @Bean
    MessageListenerAdapter listenerAdapter(ListenAndSend redisReceiver) {
        return new MessageListenerAdapter(redisReceiver, "listenAndSend");
    }

    @Bean
    MessageListenerAdapter sendUser(WebSocketController redisReceiver) {
        return new MessageListenerAdapter(redisReceiver, "sendMsg");
    }

    @Bean
    MessageListenerAdapter saveInMysql(SaveMysql redisReceiver) {
        return new MessageListenerAdapter(redisReceiver, "saveMsg");
    }

    @Bean
    MessageListenerAdapter sendIndexApi(SendInWebsocketoff redisReceiver) {
        return new MessageListenerAdapter(redisReceiver, "sendIndexMsg");
    }

    //使用默认的工厂初始化redis操作模板
    @Bean
    StringRedisTemplate template(RedisConnectionFactory connectionFactory) {
        return new StringRedisTemplate(connectionFactory);
    }
}

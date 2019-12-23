package haidian.audio.util;

import com.alibaba.fastjson.JSON;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

public class httpUtil {

    public static Response sendPostRequest(String url, Map<String, Object> params){
        RestTemplate client = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        HttpMethod method = HttpMethod.POST;
        // 以什么方式提交，自行选择，一般使用json，或者表单
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        //将请求头部和参数合成一个请求
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(params, headers);
        //执行HTTP请求，将返回的结构使用Response类格式化
        ResponseEntity<Response> response = client.exchange(url, method, requestEntity, Response.class);
        return response.getBody();
    }

    public static void main(String[] args) {
        String url="http://localhost:8080/updateGroup";
        Map<String,Object> map=new HashMap<>();
        map.put("groupId","429e56aa-09bc-46c5-83aa-6a64290336b7");
        map.put("groupName","http测试");
        System.out.println(JSON.toJSONString(sendPostRequest(url,map)));
    }
}

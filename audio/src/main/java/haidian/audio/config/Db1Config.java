package haidian.audio.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = "haidian.audio.dao.db1", sqlSessionTemplateRef = "db1")
public class Db1Config {

    /** 创建数据源 DataSource
     * 加载配置数据源
     * @return
     * @Primary 是指具有默认值
     */
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.db1")
    @Primary
    public DataSource db1DataSource() {
        return DataSourceBuilder.create().build();
    }

    /** 创建会话工厂 sqlsessionFactory   */
    @Bean
    @Primary
    public SqlSessionFactory db1SqlSessionFactory(@Qualifier("db1DataSource") DataSource dataSource, MybatisProperties mybatisProperties) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/*.xml"));
        bean.setConfiguration(mybatisProperties.getConfiguration());
        return bean.getObject();
    }

    /**  创建事务 transactionManager  */
    @Bean
    @Primary
    public DataSourceTransactionManager db1TransactionManager(@Qualifier("db1DataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    /**  创建 sqlSessionTemplate 模板  */
    @Bean(name = "db1")
    @Primary
    public SqlSessionTemplate db1SqlSessionTemplate(@Qualifier("db1SqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

}

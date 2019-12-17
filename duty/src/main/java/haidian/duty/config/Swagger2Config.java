package haidian.duty.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
//import springfox.documentation.builders.ApiInfoBuilder;
//import springfox.documentation.builders.PathSelectors;
//import springfox.documentation.builders.RequestHandlerSelectors;
//import springfox.documentation.service.ApiInfo;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class Swagger2Config {

    //是否开启swagger，正式环境一般是需要关闭的，可根据springboot的多环境配置进行设置
    @Value(value = "${swaggerEnable}")
    Boolean swaggerEnabled;

//    @Bean
//    public Docket createRestApi() {
//        return new Docket(DocumentationType.SWAGGER_2)
//                .enable(swaggerEnabled)
//                .apiInfo(apiInfo())
//                .select()
//                .apis(RequestHandlerSelectors.basePackage("com.service_haidian.controller"))
//                .paths(PathSelectors.any())
//                .build();
//    }
//
//    private ApiInfo apiInfo() {
//        return new ApiInfoBuilder()
//                .title("API Document")
//                .description("接口文档")
////                .contact(new Contact("b","",""))
//                .version("1.0")
//                .build();
//    }
}

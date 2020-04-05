package com.henu.mall.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author lv
 * @date 2020-04-02 15:38
 */
@Configuration
@EnableSwagger2
public class Swagger2Config {


    @Bean
    public Docket ApiUser(Environment environment) {
        //设置要显示的swagger环境
        Profiles profiles = Profiles.of("dev", "test");
        // 获取项目环境
        boolean flag = environment.acceptsProfiles(profiles);
        String host;
        if(flag){
            host="localhost:7777/api";
        }else{
            host="www.mall.wast.club:5678/api";
        }
        return new Docket(DocumentationType.SWAGGER_2)
                .host(host)
                .apiInfo(apiInfo())
                .groupName("前台商城")
                .enable(true)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.henu.mall.controller.merber"))
                //.paths(PathSelectors.any())
                .build();
    }

    // 配置了Swagger的Docket的bean实例
    // enable 是否开启swagger
    @Bean
    public Docket createRestApi(Environment environment) {
        //设置要显示的swagger环境
        Profiles profiles = Profiles.of("dev", "test");
        // 获取项目环境
        boolean flag = environment.acceptsProfiles(profiles);
        String host;
        if(flag){
            host="localhost:7777/api";
        }else{
            host="www.mall.wast.club:5678/api";
        }
        return new Docket(DocumentationType.SWAGGER_2)
                .host(host)
                .apiInfo(apiInfo())
                .groupName("后台管理")
                .enable(true)
                .select()

                //RequestHandlerSelectors 配置扫描接口方式
                // basePackage 指定扫描的包
                // any 扫描全部
                // none 都不扫描
                // withClassAnnotation 扫描类上的注解
                // withMethodAnnotation 扫描方法上的注解
                .apis(RequestHandlerSelectors.basePackage("com.henu.mall.controller.admin"))
                //过滤什么路径
                //any
                //.paths(PathSelectors.any())
                .build();
    }

    // 配置swagger信息=apiInfo
    private ApiInfo apiInfo() {
        //作者信息
        Contact contact = new Contact("30号小学生","http://www.mall.wast.club/","1176386463@qq.com");
        return new ApiInfoBuilder()
                .title("智慧药房接口测试")
                .description("测试测试测试")
                .termsOfServiceUrl("http://www.mall.wast.club/")
                .contact(contact)
                .version("1.1")
                .build();
    }

}


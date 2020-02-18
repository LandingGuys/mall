package com.henu.mall.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * @author lv
 * @date 2020-01-21 9:30
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Resource
    private SessionInterceptor sessionInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(sessionInterceptor).addPathPatterns("/**")
            .excludePathPatterns("/user/register","/user/login","/oauth/*","/error"
            ,"/products/*","/products");
    }
}

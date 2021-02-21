package com.example.demo.Interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private SessionInterceptor sessionInterceptor;
//     //还可以通过extends WebMvcConfigurationSupport
//     @Override
//     public void configurePathMatch(PathMatchConfigurer configurer) {
//         configurer.setUseSuffixPatternMatch(false);
//     }

//     //配置资源映射路径
//     @Override
//     public void addResourceHandlers(ResourceHandlerRegistry registry) {
//         registry.addResourceHandler("/**")
//                 .addResourceLocations("classpath:/templates/")
//                 .addResourceLocations("classpath:/static/");
//     }
    @Override
    public  void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(sessionInterceptor).addPathPatterns("/**");
    }
}

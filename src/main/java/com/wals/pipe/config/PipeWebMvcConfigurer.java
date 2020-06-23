package com.wals.pipe.config;


import com.wals.pipe.contants.BaseConstants;
import com.wals.pipe.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class PipeWebMvcConfigurer implements WebMvcConfigurer {

    @Autowired
    private LoginInterceptor adminLoginInterceptor;


    public void addInterceptors(InterceptorRegistry registry) {
        // 添加一个拦截器，拦截以/admin为前缀的url路径（后台登陆拦截）
        List<String> urlStr = new ArrayList<>();
        urlStr.add("");
        urlStr.add("/");
        urlStr.add("/index");
        urlStr.add("/classify/categories");
        urlStr.add("/index.html");
        urlStr.add("/banner/carousels");
        urlStr.add("/product/goods/edit");
        urlStr.add("/profile");

        registry.addInterceptor(adminLoginInterceptor).addPathPatterns(urlStr)
                .addPathPatterns("/admin/**")
                .excludePathPatterns("/admin/login")
                .excludePathPatterns("/admin/dist/**")
                .excludePathPatterns("/admin/plugins/**");
    }

    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/upload/**").addResourceLocations("file:" + BaseConstants.FILE_UPLOAD_DIC);
        registry.addResourceHandler("/goods-img/**").addResourceLocations("file:" + BaseConstants.FILE_UPLOAD_DIC);
    }
}

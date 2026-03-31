package com.bilibili.config;

import com.bilibili.interceptor.LoginInterceptor;
import com.bilibili.interceptor.RefreshTokenInterceptor;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Resource
    private LoginInterceptor loginInterceptor;

    @Resource
    private RefreshTokenInterceptor refreshTokenInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        /**
         * token cookie 刷新拦截器
         */
        registry.addInterceptor(refreshTokenInterceptor)
                .addPathPatterns("/api/**")
                .order(0);

        /**
         * 用户登录拦截器
         */
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/api/**")
                .order(1);
    }
}

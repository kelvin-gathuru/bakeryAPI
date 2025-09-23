package com.threepmanagerapi.threepmanagerapi.settings.config;

import com.threepmanagerapi.threepmanagerapi.settings.filter.JwtAuthenticationFilter;
import com.threepmanagerapi.threepmanagerapi.settings.service.JwtService;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<JwtAuthenticationFilter> jwtFilter(JwtService jwtService) {
        FilterRegistrationBean<JwtAuthenticationFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new JwtAuthenticationFilter(jwtService));
        registrationBean.addUrlPatterns("/api/sales/*","/api/change_password/*");
        return registrationBean;
    }
}
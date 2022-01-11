package com.spring.oauth2_authorization_server.configs.core;

import com.spring.oauth2_authorization_server.constants.AppliedLoggingPath;
import com.spring.oauth2_authorization_server.filters.ApiLoggingFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RequestLoggingConfig {
    @Bean
    public FilterRegistrationBean<ApiLoggingFilter> requestLoggingFilter() {
        FilterRegistrationBean<ApiLoggingFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new ApiLoggingFilter());
        registrationBean.addUrlPatterns(AppliedLoggingPath.patterns);
        return registrationBean;
    }
}

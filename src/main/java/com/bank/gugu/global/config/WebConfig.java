package com.bank.gugu.global.config;

import com.bank.gugu.global.web.argumentResolver.UserAuthArgumentResolver;
import com.bank.gugu.global.web.interceptor.AuthInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@EnableScheduling
@RequiredArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private static final List<String> SWAGGER_PATTERNS = List.of(
            "/gugu-bank/**",
            "/swagger-resources/**",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/webjars/**",
            "/swagger-ui.html"
    );

    private static final List<String> PUBLIC_PATTERNS = List.of(
            "/",
            "/favicon.ico",
            "/error/**",
            "/reissue",
            "/api/v1/none/**"
    );

    private final AuthInterceptor authInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .excludePathPatterns(PUBLIC_PATTERNS)
                .excludePathPatterns(SWAGGER_PATTERNS)
                .addPathPatterns("/**");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new UserAuthArgumentResolver());
    }

}

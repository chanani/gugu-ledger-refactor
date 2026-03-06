package com.bank.gugu.global.config;

import com.bank.gugu.global.web.argumentResolver.UserAuthArgumentResolver;
import com.bank.gugu.global.web.interceptor.AuthInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@EnableScheduling
@RequiredArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final List<String> JWT_PATTERNS = List.of(
            "/v3/api-docs/swagger-config",
            "/signUp",
            "/signIn",
            "/error/**",
            "/reissue",
            "/favicon.ico"
    );

    private final List<String> SWAGGER_URL_PATTERNS = List.of(
            "/gugu-bank/**",
            "/swagger-resources/**",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/webjars/**",
            "/swagger-ui.html"
    );

    private final AuthInterceptor authInterceptor;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(false)
                .maxAge(6000);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .excludePathPatterns(JWT_PATTERNS)
                .excludePathPatterns(SWAGGER_URL_PATTERNS)
                .addPathPatterns("/**");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new UserAuthArgumentResolver());
    }

}

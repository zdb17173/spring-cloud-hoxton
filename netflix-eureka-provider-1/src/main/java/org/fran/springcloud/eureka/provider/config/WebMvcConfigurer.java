package org.fran.springcloud.eureka.provider.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;

@Configuration
public class WebMvcConfigurer{

    @Bean
    public org.springframework.web.servlet.config.annotation.WebMvcConfigurer corsConfigurer() {
        return new org.springframework.web.servlet.config.annotation.WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("*")
                        .allowedMethods("POST", "GET", "HEAD", "OPTIONS");
            }

            @Override
            public void addViewControllers(ViewControllerRegistry registry) {
                registry.addViewController("/").setViewName("redirect:/swagger-ui.html");
            }

            @Override
            public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
                configurer.favorPathExtension(false)
                        .favorParameter(true);
            }

            @Override
            public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(twoFactorAuthInterceptor).addPathPatterns(twoFactorAuthCfg.getPath());
            }
        };
    }
}
package com.bilvantis.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static com.bilvantis.util.AppModernizationAPIConstants.TOKEN;

@Configuration
@EnableWebMvc
public class WebMVCConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedMethods("POST", "GET", "PUT", "DELETE", "OPTIONS")
                .allowedOrigins("*")
                .allowedHeaders("*")
                .exposedHeaders(TOKEN)
                .allowCredentials(false)
                .maxAge(-1);

    }
}

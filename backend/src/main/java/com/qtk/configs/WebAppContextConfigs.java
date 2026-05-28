/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.qtk.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 *
 * @author lossrunner
 */
@Configuration
@ComponentScan(
        basePackages = {
            "com.qtk.controllers",
            "com.qtk.repositories",
            "com.qtk.services",
            "org.springdoc"
        }
)
@EnableWebMvc
@EnableTransactionManagement
@Import({
    org.springdoc.core.configuration.SpringDocConfiguration.class,
    org.springdoc.webmvc.core.configuration.SpringDocWebMvcConfiguration.class,
    org.springdoc.webmvc.core.configuration.MultipleOpenApiSupportConfiguration.class,
    org.springdoc.webmvc.ui.SwaggerConfig.class // Kích hoạt bộ xử lý biến đổi chữ Petstore
})
public class WebAppContextConfigs implements WebMvcConfigurer{
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer){
        configurer.enable();
    }
    
    @Bean
    public StandardServletMultipartResolver multipartResolver(){
        return new StandardServletMultipartResolver();
    }
    

    
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.qtk.configs;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;
/**
 *
 * @author lossrunner
 */

@Configuration
@EnableWebSecurity
@EnableTransactionManagement
@ComponentScan(
        basePackages = {
            "com.qtk.controllers",
            "com.qtk.repositories",
            "com.qtk.services"
        }
)
@PropertySource("classpath:cloudinary.properties")
@Order(2)
public class SpringSecurityConfigs {
    
    @Autowired
    private Environment env;
    
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    
    @Bean
    public HandlerMappingIntrospector mvcHandlerMappingIntrospector() {
        return new HandlerMappingIntrospector();
    }

    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        
        http.securityMatcher("/admin/**", "/", "/login", "/logout").csrf(c -> c.disable())
                .authorizeHttpRequests((requests) -> requests
                .requestMatchers("/", "/admin").hasAuthority("ADMIN")
                .anyRequest().permitAll()
        ).formLogin(form -> form.loginPage("/admin/login") // Đường dẫn tới trang đăng nhập
                .loginProcessingUrl("/login") // Đường dẫn xử lý POST
                .defaultSuccessUrl("/", true) // Chuyển hướng khi thành công
                .failureUrl("/admin/login?error=true") // Chuyển hướng khi thất bại
                .permitAll()
        ).logout((logout) -> logout.logoutUrl("/logout").logoutSuccessUrl("/admin/login").permitAll());

        return http.build();
    }
    
    @Bean
    public Cloudinary cloudinary() {
        Cloudinary cloudinary
                = new Cloudinary(ObjectUtils.asMap(
                        "cloud_name", this.env.getProperty("cloudinary.cloud_name") ,
                        "api_key", this.env.getProperty("cloudinary.api_key"),
                        "api_secret", this.env.getProperty("cloudinary.api_secret"),
                        "secure", true));
        return cloudinary;
    }

}

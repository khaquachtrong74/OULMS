/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.qtk.configs;

import jakarta.servlet.MultipartConfigElement;
import jakarta.servlet.ServletRegistration;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

/**
 *
 * @author lossrunner
 */
public class DispatcherServeletInit extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[]{
            HibernateConfigs.class,
            ThymeleafConfigs.class,
            SpringSecurityConfigs.class,
            ApiSecurityConfigs.class,
            OpenApiConfig.class,
            GoogleDriveConfig.class
        };
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{
            WebAppContextConfigs.class,
//            OpenApiConfig.class
        };
    }

    @Override
    protected String[] getServletMappings() {
        return new String[] { "/" };
    }
    
    @Override
    protected void customizeRegistration(ServletRegistration.Dynamic registration) {
        String location = "/";
        long maxFileSize = 500L * 1024 * 1024; // 500MB
        long maxRequestSize = 500L * 1024 * 1024; // 500MB
        int fileSizeThreshold = 0;

        registration.setMultipartConfig(new MultipartConfigElement(location, maxFileSize, maxRequestSize, fileSizeThreshold));
    }
    
    
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.qtk.filters;

import com.qtk.utils.JwtUtils;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 *
 * @author lossrunner
 */
public class JwtFilter implements Filter{

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        
        if(httpRequest.getRequestURI().startsWith(String.format("%s/api/secure", httpRequest.getContextPath()))){
            String header = httpRequest.getHeader("Authorization");
            
            if(header == null || !header.startsWith("Bearer ")){
                ((HttpServletResponse) response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing or invalid Authorization header");
                return;
            }else{
                String token = header.substring(7);
                try {
                    String username = JwtUtils.validateTokenAndGetUsername(token);
                    if(username != null){
                        httpRequest.setAttribute("username", username);
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(chain, username);
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        
                        chain.doFilter(request, response);
                        return;
                    }
                } catch (Exception e) {
                    Logger.getLogger(e.toString());
                }
            }
        }
    
    }
    
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.qtk.filters;

import com.nimbusds.jwt.JWTClaimsSet;
import com.qtk.utils.JwtUtils;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 *
 * @author lossrunner
 */


public class JwtFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
            throws IOException, ServletException {
            
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        if ("OPTIONS".equalsIgnoreCase(httpRequest.getMethod())) {
            chain.doFilter(request, response);
            return;
        }

        if (httpRequest.getRequestURI().startsWith(httpRequest.getContextPath() + "/api/secure")) {
            
            String token = null;

            String authHeader = httpRequest.getHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                token = authHeader.substring(7);
            }

            if (token == null && httpRequest.getCookies() != null) {
                for (Cookie cookie : httpRequest.getCookies()) {
                    if ("jwtToken".equals(cookie.getName())) {
                        token = cookie.getValue();
                        break; 
                    }
                }
            }

            if (token == null) {
                httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                httpResponse.setContentType("application/json;charset=UTF-8");
                httpResponse.getWriter().write("{\"error\": \"Missing or invalid Authorization header/cookie\"}");
                return; 
            }

            try {
                JWTClaimsSet claimset = JwtUtils.validateTokenAndGetClaims(token);
         
                String username = claimset.getSubject();
                String role = claimset.getStringClaim("role");
                
                if (username != null) {
                    httpRequest.setAttribute("username", username);
                    List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(role));
                    UsernamePasswordAuthenticationToken authentication = 
                            new UsernamePasswordAuthenticationToken(username, null, authorities);
                    
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    
                    chain.doFilter(request, response);
                    return; 
                }
            } catch (Exception e) {
                SecurityContextHolder.clearContext(); 
                httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                httpResponse.setContentType("application/json;charset=UTF-8");
                httpResponse.getWriter().write("{\"error\": \"Xác thực thất bại: " + e.getMessage() + "\"}");
                return;
            }
        }

        chain.doFilter(request, response);
    }
}

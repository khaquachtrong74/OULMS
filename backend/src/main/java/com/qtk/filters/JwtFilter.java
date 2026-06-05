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
public class JwtFilter implements Filter{

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        if ("OPTIONS".equalsIgnoreCase(httpRequest.getMethod())) {
            chain.doFilter(request, response);
            return;
        }
        
        String jwt = null;


    // 3. Tìm trong Header trước (Dành cho Mobile App hoặc Postman)
    String authHeader = httpRequest.getHeader("Authorization");
    if (authHeader != null && authHeader.startsWith("Bearer ")) {
        jwt = authHeader.substring(7);
    }

    // 4. Nếu không có trong Header, thì moi trong Cookie ra (Dành cho web React)
    if (jwt == null && httpRequest.getCookies() != null) {
        for (jakarta.servlet.http.Cookie cookie : httpRequest.getCookies()) {
            if ("jwtToken".equals(cookie.getName())) {
                jwt = cookie.getValue();
                break; // Tìm thấy thì dừng vòng lặp ngay
            }
        }
    }
        if(httpRequest.getRequestURI().startsWith(String.format("%s/api/secure", httpRequest.getContextPath()))){
            String token = null;
            
            if(httpRequest.getCookies() != null){
                for(Cookie cookie : httpRequest.getCookies()){
                    if("jwtToken".equals(cookie.getName())){
                        token = cookie.getValue();
                        break;
                    }
                }
            }
                        
            if(token == null){
                ((HttpServletResponse) response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing or invalid Authorization header");
                return;
            }else{
                try {
                    JWTClaimsSet claimset = JwtUtils.validateTokenAndGetClaims(token);
             
                    String username = claimset.getSubject();
                    String role = claimset.getStringClaim("role");
                    
                    if(username != null){
                        httpRequest.setAttribute("username", username);
                        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(role));
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, null, authorities);
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
        }
        chain.doFilter(request, response);

    
    }
    
}

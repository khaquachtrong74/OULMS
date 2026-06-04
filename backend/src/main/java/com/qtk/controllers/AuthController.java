package com.qtk.controllers;

import com.qtk.dto.GoogleLoginRequestDTO;
import com.qtk.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;


    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping(value = "/google", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> loginGoogle(@RequestBody GoogleLoginRequestDTO request) {
        String jwtToken = this.authService.loginWithGoogle(request);

        ResponseCookie jwtCookie = ResponseCookie.from("jwtToken", jwtToken)
                .httpOnly(true)
                .secure(true)       // Bắt buộc true cho ngrok (HTTPS)
                .path("/")
                .maxAge(24 * 60 * 60) // Đã sửa: Cho phép Cookie sống trong 1 ngày (86400 giây)
                .sameSite("None")     // Cho phép gửi Cookie xuyên domain
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString() + "; Partitioned") 
                .body("{\"message\": \"Đăng nhập Google thành công\"}");
    }
}
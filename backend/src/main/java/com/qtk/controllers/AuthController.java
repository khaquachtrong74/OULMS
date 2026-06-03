package com.qtk.controllers;

import com.qtk.dto.AuthResponseDTO;
import com.qtk.dto.GoogleLoginRequestDTO;
import com.qtk.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin 
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/google")
    public ResponseEntity<AuthResponseDTO> loginGoogle(@RequestBody GoogleLoginRequestDTO request) {
        String jwtToken = this.authService.loginWithGoogle(request);
        
        AuthResponseDTO response = new AuthResponseDTO(jwtToken, "Bearer");
        
        return ResponseEntity.ok(response);
    }
}
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
@CrossOrigin // Đảm bảo ReactJS ở port 8080 có thể gọi tới mà không bị chặn CORS
public class AuthController {

    private final AuthService authService;

    // Sử dụng Constructor Injection để tuân thủ nguyên lý Dependency Inversion (DIP)
    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/google")
    public ResponseEntity<AuthResponseDTO> loginGoogle(@RequestBody GoogleLoginRequestDTO request) {
        // Controller không tự ý kiểm tra logic, chỉ đóng vai trò chuyển tiếp dữ liệu xuống tầng Service
        String jwtToken = this.authService.loginWithGoogle(request);
        
        // Đóng gói chuỗi Token vào một DTO có cấu trúc rõ ràng trước khi trả về
        AuthResponseDTO response = new AuthResponseDTO(jwtToken, "Bearer");
        
        return ResponseEntity.ok(response);
    }
}
package com.qtk.services.impl;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.qtk.dto.GoogleLoginRequestDTO;
import com.qtk.dto.UserResponseDTO;
import com.qtk.enums.UserRole;
import com.qtk.services.AuthService;
import com.qtk.services.UserService;
import com.qtk.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    // Điền Client ID của bạn (lấy trên Google Cloud Console) vào đây
    private static final String GOOGLE_CLIENT_ID = "1040366378422-eoormsur8eqoungkj00mf0sjnkt4k55a.apps.googleusercontent.com";

    @Autowired
    public AuthServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public String loginWithGoogle(GoogleLoginRequestDTO request) {
        try {
            // 1. Khởi tạo công cụ verify của Google
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                    .setAudience(Collections.singletonList(GOOGLE_CLIENT_ID))
                    .build();

            // 2. Xác minh idToken từ Client gửi lên
            GoogleIdToken idToken = verifier.verify(request.getIdToken());
            if (idToken == null) {
                throw new RuntimeException("Invalid Google ID Token"); // Trả về 401
            }

            // 3. Nếu token xịn, bóc tách payload lấy thông tin
            GoogleIdToken.Payload payload = idToken.getPayload();
            String email = payload.getEmail();
            String name = (String) payload.get("name");
            String pictureUrl = (String) payload.get("picture");

            // 4. Kiểm tra User trong DB của bạn
            UserResponseDTO user = this.userService.getUserByUserEmail(email);
            if (user == null) {
                // Tự động đăng ký nếu chưa tồn tại
                Map<String, String> newUserData = new HashMap<>();
                newUserData.put("email", email);
                newUserData.put("firstName", name); // Có thể cần cắt chuỗi họ và tên
                newUserData.put("lastName", "");
                newUserData.put("password", "OAUTH2_USER");
                newUserData.put("role", "STUDENT");
                newUserData.put("avatar", "");
                // Ở đây hàm addUser của bạn đang nhận MultipartFile avatar, 
                // bạn sẽ cần điều chỉnh lại UserService để nhận url avatar dạng String
               
            }

            // 5. Cấp JWT nội bộ của hệ thống
            return JwtUtils.generateToken(user.getEmail(), user.getRole().getAuthority());

        } catch (Exception e) {
            throw new RuntimeException("Lỗi xác thực Google: " + e.getMessage(), e);
        }
    }
}
package com.qtk.services.impl;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.nimbusds.jose.JOSEException;
import com.qtk.dto.GoogleLoginRequestDTO;
import com.qtk.dto.UserResponseDTO;
import com.qtk.services.AuthService;
import com.qtk.services.UserService;
import com.qtk.utils.JwtUtils;
import java.io.IOException;
import java.security.GeneralSecurityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

@Service
@PropertySource("classpath:databases.properties")

public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final String googleClientId;

    @Autowired
    public AuthServiceImpl(UserService userService, @Value("${GOOGLE_CLIENT_ID}") String googleClientId) {
        this.googleClientId = googleClientId;
        this.userService = userService;
    }

    @Override
    public String loginWithGoogle(GoogleLoginRequestDTO request) {
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                    .setAudience(Collections.singletonList(this.googleClientId))
                    .build();

            GoogleIdToken idToken = verifier.verify(request.getIdToken());
            if (idToken == null) {
                throw new RuntimeException("Invalid Google ID Token");
            }

            GoogleIdToken.Payload payload = idToken.getPayload();
            String email = payload.getEmail();
            String name = (String) payload.get("name");
            String pictureUrl = (String) payload.get("picture");

            UserResponseDTO user = this.userService.getUserByUserEmail(email);
            if (user == null) {
                user = this.userService.addOAuth2User(email, name, pictureUrl);
            }

            return JwtUtils.generateToken(user.getEmail(), user.getRole().getAuthority());

        } catch (JOSEException | IOException | RuntimeException | GeneralSecurityException e) {
            throw new RuntimeException("Lỗi xác thực Google: " + e.getMessage(), e);
        }
    }
}

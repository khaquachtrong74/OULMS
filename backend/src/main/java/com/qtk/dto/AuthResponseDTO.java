package com.qtk.dto;

public class AuthResponseDTO {
    
    private String token;
    private String type; 

    // Constructor không tham số (Bắt buộc phải có để Jackson làm việc)
    public AuthResponseDTO() {
    }

    // Constructor có tham số
    public AuthResponseDTO(String token, String type) {
        this.token = token;
        this.type = type;
    }

    // Getters và Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
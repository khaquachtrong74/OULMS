package com.qtk.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
import com.qtk.enums.UserRole;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequestDTO {
    private int id;
    
    @NotBlank(message = "Tên không được để trống")
    @Size(min = 2, max = 50, message = "Tên phải từ 2 đến 50 kí tự")
    private String firstName;
    
    @NotBlank(message = "Họ không được để trống")
    private String lastName;
    
    private String email; 
    
    private UserRole role;
    
    private MultipartFile avatar;
}
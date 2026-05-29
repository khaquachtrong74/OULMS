/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.qtk.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author lossrunner
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor 
public class UserCreateRequestDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String role;
    
    @Schema(type = "string", format = "binary", description = "File ảnh đại diện")
    private MultipartFile avatar;
}

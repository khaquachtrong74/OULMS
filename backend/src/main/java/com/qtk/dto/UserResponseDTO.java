/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.qtk.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author lossrunner
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor 
public class UserResponseDTO {
    private int id;
    private String email;
    private String role;
    private String firstName;
    private String lastName;
    private String avatar;
}

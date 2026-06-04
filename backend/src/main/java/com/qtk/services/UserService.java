/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.qtk.services;

import com.qtk.dto.UserCreateRequestDTO;
import com.qtk.dto.UserResponseDTO;
import com.qtk.dto.UserUpdateRequestDTO;
import java.util.List;
import java.util.Map;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 *
 * @author lossrunner
 */
public interface UserService extends UserDetailsService{
    UserResponseDTO getUserByUsername(String username);
    UserResponseDTO getUserByUserEmail(String userEmail);
    UserResponseDTO addUser(UserCreateRequestDTO dto);
    UserResponseDTO addOAuth2User(String email, String name, String avatarUrl);
    List<UserResponseDTO> getAllUsers(Map<String, String> params);
    List<UserResponseDTO> getLecturers();
    UserResponseDTO getUserById(int id);
    UserResponseDTO updateUser(UserUpdateRequestDTO dto);
    void deleteUser(int id);
    boolean authenticate(String userEmail, String password);
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.qtk.services.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.qtk.dto.UserCreateRequestDTO;
import com.qtk.dto.UserResponseDTO;
import com.qtk.enums.UserRole;
import com.qtk.pojo.User;
import com.qtk.repositories.UserRepository;
import com.qtk.services.UserService;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author lossrunner
 */
@Service("userDetailsService")
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private Cloudinary cloudinary;

    @Override
    public UserResponseDTO getUserByUsername(String username) {
        User user = this.userRepo.getUserByUsername(username);
        return mapToResponseDTO(user);
    }

    @Override
    public UserResponseDTO addUser(UserCreateRequestDTO dto) {
        User u = mapToEntity(dto);
        u.setRole(UserRole.STUDENT);
        if (dto.getAvatar() != null && !dto.getAvatar().isEmpty()) {
            try {
                Map res = this.cloudinary.uploader().upload(dto.getAvatar().getBytes(), ObjectUtils.asMap("resource_type", "auto"));
                u.setAvatar(res.get("secure_url").toString());
            } catch (IOException e) {
                throw new RuntimeException("Lỗi tải ảnh lên hệ thống. Vui lòng thử lại sau!", e);
            }
        }
        
        User savedUser = this.userRepo.addUser(u);
        return mapToResponseDTO(savedUser);
        
    }

    @Override
    public boolean authenticate(String userEmail, String password) {
        return this.userRepo.authenticate(userEmail, password);
    }

//  Adapter pattern tuan thu nguyen tac Dependency Inversion
//    Chuyen dinh dang User thanh UserDetails - Spring Security co the hieu
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userRepo.getUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Invalid username!");
        }

        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole().getAuthority()));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(), user.getPassword(), authorities);
    }

    @Override
    public UserResponseDTO getUserByUserEmail(String userEmail) {
        User user = this.userRepo.getUserByUserEmail(userEmail);
        return mapToResponseDTO(user);
    }

    @Override
    public void deleteUser(int id) {
        this.userRepo.deleteUser(id);
    }
    
    private User mapToEntity(UserCreateRequestDTO dto) {
        User u = new User();
        u.setFirstName(dto.getFirstName());
        u.setLastName(dto.getLastName());
        u.setEmail(dto.getEmail());
        u.setPassword(this.passwordEncoder.encode(dto.getPassword()));
        return u;
    }

    private UserResponseDTO mapToResponseDTO(User user) {
        if (user == null) return null;
        
        UserResponseDTO responseDTO = new UserResponseDTO();
        responseDTO.setId(user.getId());
        responseDTO.setEmail(user.getEmail());
        responseDTO.setRole(user.getRole().getAuthority());
        responseDTO.setFirstName(user.getFirstName());
        responseDTO.setLastName(user.getLastName());
        responseDTO.setAvatar(user.getAvatar());
        return responseDTO;
    }
}

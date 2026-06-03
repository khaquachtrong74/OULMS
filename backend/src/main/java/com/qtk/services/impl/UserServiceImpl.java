/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.qtk.services.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.qtk.dto.UserCreateRequestDTO;
import com.qtk.dto.UserResponseDTO;
import com.qtk.dto.UserUpdateRequestDTO;
import com.qtk.enums.UserRole;
import com.qtk.pojo.User;
import com.qtk.repositories.UserRepository;
import com.qtk.services.UserService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;
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
        if (user == null) {
            return null;
        }

        UserResponseDTO responseDTO = new UserResponseDTO();
        responseDTO.setId(user.getId());
        responseDTO.setEmail(user.getEmail());
        responseDTO.setRole(user.getRole());
        responseDTO.setFirstName(user.getFirstName());
        responseDTO.setLastName(user.getLastName());
        responseDTO.setAvatar(user.getAvatar());
        return responseDTO;
    }

    @Override
    public UserResponseDTO addOAuth2User(String email, String name, String avatarUrl) {
        User u = new User();
        u.setEmail(email);

        // Bản chất xử lý dữ liệu: Tách chuỗi name của Google thành firstName và lastName
        // Nếu name là "Quách Trọng Kha", lastName sẽ là "Kha", firstName là "Quách Trọng"
        String firstName = name;
        String lastName = "";
        if (name != null && name.trim().contains(" ")) {
            int lastSpaceIndex = name.trim().lastIndexOf(" ");
            firstName = name.substring(0, lastSpaceIndex).trim();
            lastName = name.substring(lastSpaceIndex + 1).trim();
        }

        u.setFirstName(firstName);
        u.setLastName(lastName);
        u.setAvatar(avatarUrl);
        u.setRole(UserRole.STUDENT); // Cấp quyền mặc định

        // Sinh password mặc định để thỏa mãn ràng buộc (constraint) của CSDL nếu cột password NOT NULL
        u.setPassword(this.passwordEncoder.encode("OAUTH2_NO_PASSWORD"));

        // Lưu xuống Database
        this.userRepo.addUser(u);

        // Đóng gói toàn bộ thông tin từ Entity sang DTO để trả về an toàn
        return new UserResponseDTO(
                u.getId(),
                u.getEmail(),
                u.getRole(),
                u.getFirstName(),
                u.getLastName(),
                u.getAvatar()
        );
    }

    @Override
    public List<UserResponseDTO> getAllUsers(Map<String, String> params) {
        List<User> results = this.userRepo.getAllUsers(params);
        if (results.isEmpty()) {
            return new ArrayList<UserResponseDTO>();
        }
        return results.stream()
                .map(user -> this.mapToResponseDTO(user))
                .collect(Collectors.toList());
    }

    @Override
    public UserResponseDTO getUserById(int id) {
        User user = this.userRepo.getUserById(id);
        return mapToResponseDTO(user);
    }

    @Override
    public UserResponseDTO updateUser(UserUpdateRequestDTO dto) {
        User u = this.userRepo.getUserById(dto.getId());
        if (u != null) {
            u.setFirstName(dto.getFirstName());
            u.setLastName(dto.getLastName());
            u.setRole(dto.getRole());

            // Xử lý upload ảnh nếu người dùng có chọn file mới
            if (dto.getAvatar() != null && !dto.getAvatar().isEmpty()) {
                Map res = null;
                try {
                    res = this.cloudinary.uploader().upload(dto.getAvatar().getBytes(), ObjectUtils.asMap("resource_type", "auto"));
                } catch (IOException ex) {
                    System.getLogger(UserServiceImpl.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
                }
                u.setAvatar(res.get("secure_url").toString());
            }

            this.userRepo.updateUser(u);
        }
        return mapToResponseDTO(u);
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.qtk.services.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
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
    public User getUserByUsername(String username) {
        return this.userRepo.getUserByUsername(username);
    }

    @Override
    public User addUser(Map<String, String> info, MultipartFile avatar) {
        User u = new User();
        u.setFirstName(info.get("firstName"));
        u.setLastName(info.get("lastName"));
        u.setEmail(info.get("email"));
        u.setRole(info.get("role"));
        u.setPassword(this.passwordEncoder.encode(info.get("password")));
        
        if(!avatar.isEmpty()){
            try {
                Map res = this.cloudinary.uploader().upload(avatar.getBytes(), ObjectUtils.asMap("resource_type", "auto"));
                u.setAvatar(res.get("secure_url").toString());
            } catch (IOException e) {
                Logger.getLogger("");
            }
        }
        return this.userRepo.addUser(u);
        
    }

    @Override
    public boolean authenticate(String userEmail, String password) {
        return this.userRepo.authenticate(userEmail, password);
    }

//  Adapter pattern tuan thu nguyen tac Dependency Inversion
//    Chuyen dinh dang User thanh UserDetails - Spring Security co the hieu
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.getUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Invalid username!");
        }

        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole()));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(), user.getPassword(), authorities);
    }

    @Override
    public User getUserByUserEmail(String userEmail) {
        return this.userRepo.getUserByUserEmail(userEmail);
    }

    @Override
    public void deleteUser(int id) {
        this.userRepo.deleteUser(id);
    }
    
}

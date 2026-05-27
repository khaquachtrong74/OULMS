package com.qtk.controllers;

import com.nimbusds.jose.JOSEException;
import com.qtk.pojo.User;
import com.qtk.services.UserService;
import com.qtk.utils.JwtUtils;
import java.security.Principal;
import java.util.Collections;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author lossrunner
 */

@RestController
@RequestMapping("/api")
@CrossOrigin
public class ApiUserController {
    @Autowired
    private UserService userService;
    
    
    @PostMapping(path = "/users", 
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE, 
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> create(@RequestParam Map<String, String> params, 
            @RequestParam(value = "avatar") MultipartFile avatar) {
        
        User u = this.userService.addUser(params, avatar);
        
        return new ResponseEntity<>(u, HttpStatus.CREATED);
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User u){
        if(this.userService.authenticate(u.getEmail(), u.getPassword())){
            try {
                User userDetailed = this.userService.getUserByUserEmail(u.getEmail());
                String token = JwtUtils.generateToken(userDetailed.getEmail(), userDetailed.getRole());
          
            return ResponseEntity.ok().body(Collections.singletonMap("token", token));
            } catch (JOSEException e) {
                return ResponseEntity.status(500).body("Error when create JWT");
            }
            
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Wrong info login");
    }
    
    @GetMapping("/secure/profile")
    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    public ResponseEntity<User> getProfile(Principal principal){
        return new ResponseEntity<>(this.userService.getUserByUserEmail(principal.getName()), HttpStatus.OK);
    }
    
    @DeleteMapping("/secure/users/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable int id){
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
    
}

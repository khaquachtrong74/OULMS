package com.qtk.controllers;

import com.nimbusds.jose.JOSEException;
import com.qtk.dto.UserCreateRequestDTO;
import com.qtk.dto.UserLoginRequestDTO;
import com.qtk.dto.UserResponseDTO;
import com.qtk.pojo.User;
import com.qtk.services.UserService;
import com.qtk.utils.JwtUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import java.security.Principal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        content = @Content(
            mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
            schema = @Schema(implementation = UserCreateRequestDTO.class)
        )
    )
    public ResponseEntity<?> create(@Valid @ModelAttribute 
            UserCreateRequestDTO requestDTO, BindingResult bindingResult) {
        
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        
        UserResponseDTO u = this.userService.addUser(requestDTO);
        return new ResponseEntity<>(u, HttpStatus.CREATED);
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginRequestDTO u){
        if(this.userService.authenticate(u.getEmail(), u.getPassword())){
            try {
                UserResponseDTO userDetailed = this.userService.getUserByUserEmail(u.getEmail());
                String token = JwtUtils.generateToken(userDetailed.getEmail(), userDetailed.getRole());
          
                ResponseCookie jwtCookie = ResponseCookie.from("jwtToken", token)
                        .httpOnly(true)
                        .secure(false)
                        .path("/")
                        .maxAge(24*60*60)
                        .sameSite("Lax")
                        .build();
                
                
            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                    .body(userDetailed);
            } catch (JOSEException e) {
                return ResponseEntity.status(500).body("Error when create JWT");
            }
            
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Wrong info login");
    }
    
    @GetMapping("/secure/profile")
    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    public ResponseEntity<UserResponseDTO> getProfile(Principal principal){
        UserResponseDTO userEntity = this.userService.getUserByUserEmail(principal.getName());
        return new ResponseEntity<>(userEntity, HttpStatus.OK);
    }
    
    @DeleteMapping("/secure/users/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable("id") int id){
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
    
}

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
import java.security.Principal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    
    
    private UserResponseDTO convertToDTO(User entity) {
        if (entity == null) return null;
        return new UserResponseDTO(
            entity.getId(), 
            entity.getEmail(), 
            entity.getRole()
        );
    }
    
    
    @PostMapping(path = "/users", 
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE, 
        produces = MediaType.APPLICATION_JSON_VALUE)
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        content = @Content(
            mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
            schema = @Schema(implementation = UserCreateRequestDTO.class)
        )
    )
    public ResponseEntity<UserResponseDTO> create( @ModelAttribute UserCreateRequestDTO requestDTO) {
        
        Map<String, String> params = new HashMap<>();
        params.put("firstName", requestDTO.getFirstName());
        params.put("lastName", requestDTO.getLastName());
        params.put("email", requestDTO.getEmail());
        params.put("role", requestDTO.getRole());
        params.put("password", requestDTO.getPassword());
        
        User u = this.userService.addUser(params, requestDTO.getAvatar());
        
        UserResponseDTO responseDTO = new UserResponseDTO();
        responseDTO.setId(u.getId());
        responseDTO.setEmail(u.getEmail());
        responseDTO.setRole(u.getRole());
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginRequestDTO u){
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
    public ResponseEntity<UserResponseDTO> getProfile(Principal principal){
        User userEntity = this.userService.getUserByUserEmail(principal.getName());
        UserResponseDTO responseDTO = convertToDTO(userEntity);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
    
    @DeleteMapping("/secure/users/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable("id") int id){
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
    
}

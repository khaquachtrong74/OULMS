/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.qtk.controllers;

import com.qtk.dto.UserCreateRequestDTO;
import com.qtk.dto.UserResponseDTO;
import com.qtk.dto.UserUpdateRequestDTO;
import com.qtk.pojo.User;
import com.qtk.services.UserService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author lossrunner
 */

@Controller
@RequestMapping("/admin")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/login")
    public String loginView(){
        return "login";
    }
    
     @GetMapping("/users")
    public String viewUserManagementPage(Model model, @RequestParam Map<String, String> params) {
        model.addAttribute("users", userService.getAllUsers(params));

        return "user";
    }
    @GetMapping("/user")
    public String createView(Model model) {
        model.addAttribute("user", new User());
        return "userCreate";
    }
    @PostMapping("/user")
    public String create(@ModelAttribute(value = "user") UserCreateRequestDTO u) {
        this.userService.addUser(u);
        return "redirect:/admin/users";
    }

    @DeleteMapping("/users/{id}")
    @ResponseBody
    public ResponseEntity<?> deleteUserForAdminPanel(@PathVariable("id") int id) {
        this.userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
    
    // Thêm vào UserController.java
    @GetMapping("/users/{id}")
    public String updateView(Model model, @PathVariable("id") int id) {
        UserResponseDTO user = this.userService.getUserById(id);
        if (user == null) {
            return "redirect:/admin/users";
        }
        model.addAttribute("user", user);
        return "userUpdate";
    }

    @PostMapping("/users/{id}")
    public String update(@PathVariable("id") int id, @ModelAttribute("user") UserUpdateRequestDTO u) {
        u.setId(id); // Đảm bảo ID đồng nhất
        this.userService.updateUser(u);
        return "redirect:/admin/users";
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.qtk.repositories;

import com.qtk.pojo.User;
import java.util.List;
import java.util.Map;

/**
 *
 * @author lossrunner
 */
public interface UserRepository {
    public User getUserByUsername(String username);
    public User getUserByUserEmail(String userEmail);
    public User addUser(User u);
    public List<User> getAllUsers(Map<String, String> params);
    public List<User> getLecturers();
    
    public User getUserById(int id);
    public User updateUser(User u);
    
    public void deleteUser(int id);
    public boolean authenticate(String userEmail, String password);
}

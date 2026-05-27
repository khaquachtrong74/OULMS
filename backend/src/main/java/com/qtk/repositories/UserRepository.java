/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.qtk.repositories;

import com.qtk.pojo.User;

/**
 *
 * @author lossrunner
 */
public interface UserRepository {
    public User getUserByUsername(String username);
    public User getUserByUserEmail(String userEmail);
    public User addUser(User u);
    public boolean authenticate(String userEmail, String password);
}

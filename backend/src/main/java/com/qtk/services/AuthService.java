/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.qtk.services;

import com.qtk.dto.GoogleLoginRequestDTO;

/**
 *
 * @author lossrunner
 */
public interface AuthService {
    String loginWithGoogle(GoogleLoginRequestDTO request);
}

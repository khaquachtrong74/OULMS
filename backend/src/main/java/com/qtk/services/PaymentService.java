/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.qtk.services;

import java.util.Map;

/**
 *
 * @author lossrunner
 */
public interface PaymentService {
    Map<String, Object> createOrder(int amount, String orderInfo, String appTransId);
    boolean verifyCallback(String data, String mac);
}

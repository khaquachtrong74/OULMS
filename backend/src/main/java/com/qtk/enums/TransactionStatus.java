/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package com.qtk.enums;

import lombok.Getter;

/**
 *
 * @author lossrunner
 */

@Getter
public enum TransactionStatus {
    SUCCESS("SUCCESS"),
    PENDING("PENDING"),
    FAILED("FAILED");
    
    private final String stauts;



    private TransactionStatus(String stauts) {
        this.stauts = stauts;
    }
    
    
}

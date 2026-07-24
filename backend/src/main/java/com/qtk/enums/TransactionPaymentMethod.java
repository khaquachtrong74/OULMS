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
public enum TransactionPaymentMethod {
    MOMO("MOMO"),
    ZALOPAY("ZALOPAY"),
    VNPAY("VNPAY");
    
    private final String paymentMethod;

    private TransactionPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}

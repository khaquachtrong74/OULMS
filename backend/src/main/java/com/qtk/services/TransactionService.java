/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.qtk.services;

/**
 *
 * @author lossrunner
 */
import com.qtk.pojo.Transaction;
import com.qtk.enums.TransactionPaymentMethod;

public interface TransactionService {
    Transaction createPendingTransaction(int amount, TransactionPaymentMethod paymentMethod, int courseId, int userId);
    boolean markTransactionAsSuccess(String transactionRef);
    boolean markTransactionAsFailed(String transactionRef);
    Transaction getTransactionByRef(String transactionRef);
}
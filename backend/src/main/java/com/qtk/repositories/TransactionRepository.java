/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.qtk.repositories;

/**
 *
 * @author lossrunner
 */
import com.qtk.pojo.Transaction;
//import java.util.List;

public interface TransactionRepository {
    void addTransaction(Transaction transaction);
    Transaction getTransactionById(int id);
    Transaction getTransactionByRef(String transactionRef);
    boolean updateTransactionStatus(String transactionRef, String status);
}
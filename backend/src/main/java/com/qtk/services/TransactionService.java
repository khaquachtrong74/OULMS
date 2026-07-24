/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.qtk.services;

/**
 *
 * @author lossrunner
 */
import com.qtk.dto.RevenueChartDTO;
import com.qtk.pojo.Transaction;
import com.qtk.enums.TransactionPaymentMethod;
import java.util.List;
import java.util.Map;

public interface TransactionService {
    Transaction createPendingTransaction(int amount, TransactionPaymentMethod paymentMethod, int courseId, int userId);
    boolean markTransactionAsSuccess(String transactionRef);
    boolean markTransactionAsFailed(String transactionRef);
    Transaction getTransactionByRef(String transactionRef);
    
    Long getTotalRevenue();
    Long countSuccessTransactions();
    Long countPendingTransactions();
    List<RevenueChartDTO> getRevenueStatistics(int lastMonths);
    List<Transaction> getTransactions(Map<String, String> params);

}
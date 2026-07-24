/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.qtk.dto;

/**
 *
 * @author lossrunner
 */
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RevenueChartDTO {
    private String period; 
    private Long totalRevenue;
    private Long transactionCount;
    
    public RevenueChartDTO(Integer month, Integer year, Long totalRevenue, Long transactionCount) {
        this.period = month + "/" + year; 
        this.totalRevenue = totalRevenue;
        this.transactionCount = transactionCount;
    }
}
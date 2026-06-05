/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.qtk.controllers;

/**
 *
 * @author lossrunner
 */
import com.qtk.dto.RevenueChartDTO;
import com.qtk.services.TransactionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/charts")
public class ApiChartController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/revenue")
    public ResponseEntity<List<RevenueChartDTO>> getRevenueChartData() {
        List<RevenueChartDTO> data = this.transactionService.getRevenueStatistics(6);
        return ResponseEntity.ok(data); 
    }
}
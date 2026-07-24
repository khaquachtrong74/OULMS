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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin/reports")
public class ReportController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/revenue")
    public String revenueReport(Model model) {

        List<RevenueChartDTO> stats = transactionService.getRevenueStatistics(2);

        long currentRevenue = 0;
        long previousRevenue = 0;
        double growthRate = 0.0;

        if (!stats.isEmpty()) {
            currentRevenue = stats.get(0).getTotalRevenue();
            if (stats.size() > 1) {
                previousRevenue = stats.get(1).getTotalRevenue();
            }
        }

        if (previousRevenue > 0) {
            growthRate = ((double) (currentRevenue - previousRevenue) / previousRevenue) * 100;
        } else if (currentRevenue > 0) {
            growthRate = 100.0;
        }

        model.addAttribute("currentMonthRevenue", currentRevenue);
        model.addAttribute("growthRate", Math.round(growthRate * 100.0) / 100.0);

        return "revenue";
    }

    @GetMapping("/transactions")
    public String transactionReport(Model model, @RequestParam Map<String, String> params) {
        int currentPage = Integer.parseInt(params.getOrDefault("page", "1"));
        params.put("page", String.valueOf(currentPage));

        List<com.qtk.pojo.Transaction> transactions = this.transactionService.getTransactions(params);
        model.addAttribute("transactions", transactions);

        Map<String, String> countParams = new java.util.HashMap<>(params);
        countParams.remove("page");
        long totalItems = this.transactionService.getTransactions(countParams).size();

        int pageSize = 10; 
        int totalPages = (int) Math.ceil((double) totalItems / pageSize);

        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("kw", params.get("kw"));

        return "transactions";
    }

    @GetMapping("/revenue/monthly")
    public ResponseEntity<List<Map<String, Object>>> getMonthlyRevenue() {
        List<RevenueChartDTO> stats = transactionService.getRevenueStatistics(6);

        List<Map<String, Object>> response = new ArrayList<>();

        for (int i = stats.size() - 1; i >= 0; i--) {
            RevenueChartDTO dto = stats.get(i);

            Map<String, Object> item = new HashMap<>();

            item.put("month", dto.getPeriod());
            item.put("amount", dto.getTotalRevenue());

            response.add(item);
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

package com.qtk.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qtk.enums.TransactionPaymentMethod;
import com.qtk.pojo.Transaction;
import com.qtk.services.PaymentService;
import com.qtk.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/payment/zalopay")
@CrossOrigin(origins = {"http://localhost:3000", "https://quietly-untrashed-darby.ngrok-free.dev"}, allowCredentials = "true")
public class ApiPaymentController {

    @Autowired
    private PaymentService paymentService; // ZaloPay logic (đã viết ở bước 1)

    @Autowired
    private TransactionService transactionService; // DB logic (vừa viết)

    @PostMapping("/create")
    public ResponseEntity<?> createPayment(@RequestBody Map<String, Object> payload) {
        try {
            int amount = Integer.parseInt(payload.get("amount").toString());
            int courseId = Integer.parseInt(payload.get("courseId").toString());
            int userId = Integer.parseInt(payload.get("userId").toString());

            // 1. Tạo Transaction PENDING trong Database
            Transaction tx = transactionService.createPendingTransaction(amount, TransactionPaymentMethod.ZALOPAY, courseId, userId);

            // 2. Gọi sang ZaloPay bằng mã transactionRef vừa sinh ra
            Map<String, Object> result = paymentService.createOrder(amount, "Thanh toan khoa hoc " + courseId, tx.getTransactionRef());
            
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/callback")
    public ResponseEntity<?> callback(@RequestBody Map<String, Object> payload) {
        Map<String, Object> response = new HashMap<>();
        try {
            String data = (String) payload.get("data");
            String mac = (String) payload.get("mac");

            // 1. Xác thực nguồn gốc (ZaloPay gọi hay hacker gọi?)
            boolean isValid = paymentService.verifyCallback(data, mac);
            if (!isValid) {
                response.put("return_code", -1);
                response.put("return_message", "mac not equal");
                return ResponseEntity.badRequest().body(response);
            }

            // 2. Parse dữ liệu ZaloPay gửi về
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> parsedData = mapper.readValue(data, new TypeReference<Map<String, Object>>(){});
            
            String appTransId = (String) parsedData.get("app_trans_id");
            int returnCode = (int) parsedData.getOrDefault("return_code", -1);

            // 3. Cập nhật Database
            if (returnCode == 1) {
                transactionService.markTransactionAsSuccess(appTransId);
            } else {
                transactionService.markTransactionAsFailed(appTransId);
            }

            response.put("return_code", 1);
            response.put("return_message", "success");
            return ResponseEntity.ok(response);
            
        } catch (JsonProcessingException e) {
            response.put("return_code", 0);
            response.put("return_message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
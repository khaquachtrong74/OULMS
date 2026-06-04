/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.qtk.services.impl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qtk.services.PaymentService;
import com.qtk.utils.HmacUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.util.HashMap;
import java.util.Map;
/**
 *
 * @author lossrunner
 */
@Service
public class ZaloPayServiceImpl implements PaymentService {
    
    // Đưa các config này vào file properties trong thực tế
    private final String APP_ID = "2554";
    private final String KEY1 = "sdngKKJmqEMzvh5QQcdD2A9XBSKUNaYn"; 
    private final String KEY2 = "trMrHtvjo6myautxDUiAcYsVtaeQ8nhf";
    private final String CREATE_URL = "https://sb-openapi.zalopay.vn/v2/create";
    private final String CALLBACK_URL = "https://quietly-untrashed-darby.ngrok-free.dev/api/payment/zalopay/callback";

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Map<String, Object> createOrder(int amount, String orderInfo, String appTransId) {
        long appTime = System.currentTimeMillis();
        String embedData = "{\"redirecturl\": \"http://localhost:3000/payment/success\"}";
        String item = "[]"; 

        // 1. Tạo chuỗi Data (Theo đúng quy tắc của ZaloPay)
        String rawData = APP_ID + "|" + appTransId + "|lms_user|" + amount + "|" + appTime + "|" + embedData + "|" + item;
        
        // 2. Tính MAC với KEY1
        String mac = HmacUtils.HMacHexString(rawData, KEY1);

        // 3. Build payload
        Map<String, Object> order = new HashMap<>();
        order.put("app_id", Integer.parseInt(APP_ID));
        order.put("app_trans_id", appTransId);
        order.put("app_user", "lms_user");
        order.put("app_time", appTime);
        order.put("amount", amount);
        order.put("embed_data", embedData);
        order.put("item", item);
        order.put("description", orderInfo);
        order.put("mac", mac);
        order.put("callback_url", CALLBACK_URL);

        // 4. Bắn Request sang ZaloPay
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(order, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(CREATE_URL, entity, Map.class);
        return response.getBody();
    }

    @Override
    public boolean verifyCallback(String data, String mac) {
        // Tính lại MAC từ data ZaloPay gửi sang bằng KEY2
        String calcMac = HmacUtils.HMacHexString(data, KEY2);
        return calcMac.equals(mac);
    }
}
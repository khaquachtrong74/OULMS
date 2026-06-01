/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.qtk.services;

import com.qtk.pojo.Document;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

/**
 *
 * @author lossrunner
 */


public interface DocumentService {
    List<Document> getDocuments(Map<String, String> params);
    
    List<Document> getDocumentsByUserId(int userId, Map<String, String> params);
}

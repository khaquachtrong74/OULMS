/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.qtk.services;

import com.qtk.pojo.Document;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

/**
 *
 * @author ngodo
 */
public interface DocumentService {
    List<Document> getDocument(Map<String, String> params);
    void addOrUpdateDocument(Document d);
    Document getDocumentById(int id);
    void deleteDocument(int id);
}

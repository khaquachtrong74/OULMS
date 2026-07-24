/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.qtk.repositories;

import com.qtk.enums.DocumentStatus;
import com.qtk.pojo.Document;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ngodo
 */
public interface DocumentRepository {
    List<Document> getDocuments(Map<String, String> params);
    List<Document> getDocumentsByUserId(int userId, Map<String, String> params);
    void addOrUpdateDocument(Document d);
    Document getDocumentById(int id);
    void deleteDocument(int id);
    void updateDocumentStatus(int id, DocumentStatus status);
    
    long countDocuments(Map<String, String> params);
    int getDocumentPageSize();

    public List<Document> getPendingDocuments(int limit);
    public Long countPendingDocuments();

}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.qtk.services;

import com.qtk.dto.DocumentRequestDTO;
import com.qtk.dto.DocumentResponseDTO;
import com.qtk.enums.DocumentStatus;
import com.qtk.pojo.Document;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ngodo
 */
public interface DocumentService {
    List<Document> getAdminDocuments(Map<String, String> params);
    List<DocumentResponseDTO> getPublicDocument(Map<String, String> params);
    List<DocumentResponseDTO> getDocumentsByUserId(int userId, Map<String, String> params);
    DocumentResponseDTO getDocumentDetail(int id);
    void createDocument(DocumentRequestDTO d);
    void addOrUpdateDocument(Document d);
    Document getDocumentById(int id);
    void updateDocumentStatus(int id, DocumentStatus status);
}

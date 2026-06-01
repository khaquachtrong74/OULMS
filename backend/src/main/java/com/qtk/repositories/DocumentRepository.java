/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.qtk.repositories;

import com.qtk.pojo.Document;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ngodo
 */
public interface DocumentRepository {
    List<Document> getDocument(Map<String, String> params);
    void addOrUpdateDocument(Document d);
    Document getDocumentById(int id);
    void deleteDocument(int id);
}

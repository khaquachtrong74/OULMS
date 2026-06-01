package com.qtk.services.impl;

import com.qtk.pojo.Document;
import com.qtk.repositories.DocumentRepository;
import com.qtk.services.DocumentService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author lossrunner
 */

@Service
public class DocumentServiceImpl implements DocumentService{

    @Autowired
    private DocumentRepository docRepo;

    
    @Override
    public List<Document> getDocuments(Map<String, String> params) {
        return this.docRepo.getDocuments(params);
    }

    @Override
    public List<Document> getDocumentsByUserId(int userId, Map<String, String> params) {
        return this.docRepo.getDocumentsByUserId(userId, params);
    }
    
}

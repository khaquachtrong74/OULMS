/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.qtk.services.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.qtk.pojo.Document;
import com.qtk.repositories.DocumentRepository;
import com.qtk.repositories.UserRepository;
import com.qtk.services.DocumentService;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 *
 * @author ngodo
 */
@Service
public class DocumentServiceImpl implements DocumentService{
    
    @Autowired
    private DocumentRepository docRepo;
    
    @Autowired
    private UserRepository userRepo;
    
    @Autowired
    private Cloudinary cloudinary;

    @Override
    public List<Document> getDocument(Map<String, String> params) {
        return this.docRepo.getDocument(params);
    }

    @Override
    public void addOrUpdateDocument(Document d) {
        d.setUploadedBy(this.userRepo.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName()));
        if(!d.getFile().isEmpty()) {
            try {
                Map res =this.cloudinary.uploader().upload(d.getFile().getBytes(), 
                        ObjectUtils.asMap("resource_type", "auto", "folder" , "Document"));
                d.setUrl(res.get("secure_url").toString());
            } catch (IOException ex) {
                Logger.getLogger(DocumentServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        this.docRepo.addOrUpdateDocument(d);
    }

    @Override
    public Document getDocumentById(int id) {
        return this.docRepo.getDocumentById(id);
    }

    @Override
    public void deleteDocument(int id) {
        this.docRepo.deleteDocument(id);
    }
    
}

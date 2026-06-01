/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package com.qtk.controllers;

import com.qtk.pojo.Document;
import com.qtk.services.DocumentService;
import com.qtk.services.UserService;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author lossrunner
 */

@RestController
@RequestMapping("/api")
@CrossOrigin
public class DocumentController {
    @Autowired
    private DocumentService docService;
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/documents")
    public ResponseEntity<List<Document>> getAllPublicDocuments(@RequestParam 
            Map<String, String> params) {
        try {
            List<Document> docs = this.docService.getDocuments(params);
            return new ResponseEntity<>(docs, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/secure/documents")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'LECTURER')")
    public ResponseEntity<List<Document>> getLecturerDocuments(
            @RequestParam Map<String, String> params, 
            Principal principal) {
        
        if (principal == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED); 
        }
        
        try {
            String username = principal.getName();
            
            int userId = this.userService.getUserByUserEmail(username).getId(); 
            
            List<Document> myDocs = this.docService.getDocumentsByUserId(userId, params);
            
            return new ResponseEntity<>(myDocs, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

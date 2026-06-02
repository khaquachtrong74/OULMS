/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.qtk.controllers;

import com.qtk.dto.DocumentRequestDTO;
import com.qtk.dto.DocumentResponseDTO;
import com.qtk.enums.DocumentStatus;
import com.qtk.pojo.Document;
import com.qtk.services.DocumentService;
import com.qtk.services.UserService;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author ngodo
 */
@RestController
@RequestMapping("/api")
@CrossOrigin
public class ApiDocumentController {
    
    @Autowired
    private DocumentService docService;
    
    @Autowired
    private UserService userService;
            
    @GetMapping("/documents")
    public ResponseEntity<List<DocumentResponseDTO>> getPublicDocument(@RequestParam Map<String, String> params) {
        params.put("status", "ACTIVE");
        try {
            List<DocumentResponseDTO> docs = this.docService.getPublicDocument(params);
            return new ResponseEntity<>(docs, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PostMapping(
            path = "/secure/document",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @PreAuthorize("hasAnyAuthority('ADMIN', 'LECTURER')")
    public ResponseEntity<?> createDocument(@ModelAttribute DocumentRequestDTO dto) {
        try {
            this.docService.createDocument(dto);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (IllegalArgumentException i) {
            return new ResponseEntity<>(i.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    @GetMapping("/documents/{documentId}")
    public ResponseEntity<DocumentResponseDTO> getDocumentDetail(@PathVariable(value = "documentId") int id) {
        try {
            return new ResponseEntity<>(this.docService.getDocumentDetail(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); 
        }
    }
    
    @GetMapping("/secure/documents")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'LECTURER')")
    public ResponseEntity<List<DocumentResponseDTO>> getDocumentsUser(@RequestParam Map<String, String> params, Principal principal) {
     
        if (principal == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED); 
        }
        
        try {
            String username = principal.getName();
            
            int userId = this.userService.getUserByUserEmail(username).getId(); 
            
            List<DocumentResponseDTO> myDocs = this.docService.getDocumentsByUserId(userId, params);
            
            return new ResponseEntity<>(myDocs, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PostMapping("/documents/{documentId}/{status}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateStatus(@PathVariable(value = "documentId") int id, @PathVariable("status") DocumentStatus status) {
        this.docService.updateDocumentStatus(id, status);
    }
}

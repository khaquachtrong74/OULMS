/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.qtk.controllers;

import com.qtk.dto.DocumentRequestDTO;
import com.qtk.dto.DocumentResponseDTO;
import com.qtk.enums.DocumentStatus;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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

    @GetMapping("/secure/documents/{uploadBy}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'LECTURER')")
    public ResponseEntity<List<DocumentResponseDTO>> getDocumentsByCreatedUser(
            @RequestParam Map<String, String> params, @PathVariable(value = "uploadBy") int id) {
        try {

            List<DocumentResponseDTO> docs = this.docService.getDocumentsByUserId(id, params);
            return new ResponseEntity<>(docs, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/secure/documents/{documentId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @PreAuthorize("hasAnyAuthority('ADMIN', 'LECTURER')")
    public ResponseEntity<?> updateDocument(
            @PathVariable int documentId,
            @RequestParam Map<String, String> params,
            @RequestPart(value = "file", required = false) MultipartFile file) {

        try {
            DocumentRequestDTO dto = new DocumentRequestDTO();
            dto.setTitle(params.get("title"));
            dto.setDescription(params.get("description"));

            if (params.get("topicId") != null && !params.get("topicId").isEmpty()) {
                dto.setTopicId(Integer.valueOf(params.get("topicId")));
            }
            if (params.get("levelId") != null && !params.get("levelId").isEmpty()) {
                dto.setLevelId(Integer.valueOf(params.get("levelId")));
            }

            dto.setFile(file);

            this.docService.updateDocument(documentId, dto);

            return ResponseEntity.ok("Cập nhật học liệu thành công!");
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping(
            path = "/secure/document",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @PreAuthorize("hasAnyAuthority('ADMIN', 'LECTURER')")
    public ResponseEntity<?> createDocument(@ModelAttribute DocumentRequestDTO dto
    ) {
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
    public ResponseEntity<DocumentResponseDTO> getDocumentDetail(@PathVariable(value = "documentId") int id
    ) {
        try {
            return new ResponseEntity<>(this.docService.getDocumentDetail(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/secure/documents")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'LECTURER')")
    public ResponseEntity<List<DocumentResponseDTO>> getDocumentsUser(@RequestParam Map<String, String> params, Principal principal
    ) {

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
    
    @DeleteMapping("/secure/documents/{uploadBy}")
    @PreAuthorize("hasAuthority('LECTURER')")
    public ResponseEntity<?> deleteDocument(@PathVariable("uploadBy") int id){
        this.docService.deleteDocument(id);
        return ResponseEntity.noContent().build();
    }
}

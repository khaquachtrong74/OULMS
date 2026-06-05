/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.qtk.controllers;

import com.qtk.dto.ProgressRequestDTO;
import com.qtk.services.ProgressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author ngodo
 */
@RestController
@RequestMapping("/api/secure")
@CrossOrigin
public class ApiProgressController {
    @Autowired
    private ProgressService progressService;

    @PostMapping("/documents/{documentId}/progress/start")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> startProgress(@PathVariable("documentId") int documentId) {
        try {
            return new ResponseEntity<>(this.progressService.startProgress(documentId), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/documents/{documentId}/progress/time")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> updateTimeSpent(
            @PathVariable("documentId") int documentId,
            @RequestBody ProgressRequestDTO dto) {
        try {
            return new ResponseEntity<>(
                    this.progressService.updateTimeSpent(documentId, dto.getSeconds()),
                    HttpStatus.OK
            );
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/documents/{documentId}/progress/complete")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> completeProgress(@PathVariable("documentId") int documentId) {
        try {
            return new ResponseEntity<>(
                    this.progressService.completeProgress(documentId),
                    HttpStatus.OK
            );
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}

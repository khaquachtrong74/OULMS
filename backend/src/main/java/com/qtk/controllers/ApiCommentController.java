/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.qtk.controllers;

import com.qtk.pojo.Comment;
import com.qtk.services.CommentService;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
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
@RequestMapping("/api")
@CrossOrigin
public class ApiCommentController {
    
    @Autowired
    private CommentService commentService;
    
    @GetMapping("/documents/{documentId}/comments")
    public ResponseEntity<List<Comment>> list(@PathVariable(value = "documentId") int documentId) {
        return new ResponseEntity<>(this.commentService.getCommemts(documentId), HttpStatus.OK);
    }
    
    @PostMapping("/secure/documents/{documentId}/comments")
    public ResponseEntity<Comment> addComment(@RequestBody Map<String, String> p, @PathVariable(value = "documentId") int documentId, Principal principal) {
        return new ResponseEntity<>(this.commentService.addComment(documentId, p.get("content")), HttpStatus.CREATED);
    }
}

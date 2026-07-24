/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.qtk.controllers;

import com.qtk.dto.ForumPostResponseDTO;
import com.qtk.dto.PageResponseDTO;
import com.qtk.services.ForumPostService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author ngodo
 */
@RestController
@RequestMapping("/api")
@CrossOrigin
public class ApiForumPostController {
    
    @Autowired
    private ForumPostService frService;
    
    @GetMapping("/forumPosts")
    public ResponseEntity<?> getForumPost(@RequestParam Map<String, String> params) {
        try {
            int currentPage = Integer.parseInt(params.getOrDefault("page", "1"));
            int pageSize = this.frService.getForumPostPageSize();

            long totalItems = this.frService.countForumPost(params);
            int totalPages = (int) Math.ceil((double) totalItems / pageSize);

            List<ForumPostResponseDTO> docs = this.frService.getForumPost(params);

            PageResponseDTO<ForumPostResponseDTO> res = 
                    new PageResponseDTO<>(docs, currentPage, pageSize, totalPages, totalItems);

            return new ResponseEntity<>(res, HttpStatus.OK);
        } catch (NumberFormatException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR); 
        }
    }
    
    @PostMapping("/secure/forumPosts")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> createForumPost(@RequestBody Map<String, String> p) {
        try {
            return new ResponseEntity<>(this.frService.createForumPost(p.get("title"), p.get("content")), HttpStatus.CREATED);
        } catch (IllegalArgumentException i) {
            return new ResponseEntity<>(i.getMessage(), HttpStatus.BAD_REQUEST);
        } 
    }
    
    @GetMapping("/secure/forumPosts/{forumPostId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getForumPostDetail(@PathVariable(value = "forumPostId") int forumPostId) {
        try {
            return new ResponseEntity<>(this.frService.getForumPostDetail(forumPostId), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } 
    }
    
    @DeleteMapping(
            value = "/secure/forumPosts/{forumPostId}",
            produces = "text/plain;charset=UTF-8"
    )
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> deleteForumPost(@PathVariable(value = "forumPostId") int forumPostId) {
        try {
            this.frService.deteleForumPost(forumPostId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException i) {
            return new ResponseEntity<>(i.getMessage(), HttpStatus.BAD_REQUEST);
        } 
    }
}

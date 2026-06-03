/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.qtk.services.impl;

import com.qtk.pojo.Comment;
import com.qtk.repositories.CommentRepository;
import com.qtk.repositories.DocumentRepository;
import com.qtk.repositories.UserRepository;
import com.qtk.services.CommentService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 *
 * @author ngodo
 */
@Service
public class CommentServiceImpl implements CommentService{
    
    @Autowired
    private CommentRepository commentRepo;
    
    @Autowired
    private DocumentRepository docRepo;
    
    @Autowired
    private UserRepository userRepo;

    @Override
    public List<Comment> getCommemts(int documentId) {
        return this.commentRepo.getCommemts(documentId);
    }

    @Override
    public Comment addComment(int documentId, String content) {
        Comment c = new Comment();
        c.setContent(content);
        c.setDocumentId(this.docRepo.getDocumentById(documentId));
        c.setUserId(this.userRepo.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName()));
        
        return this.commentRepo.addComment(c);
    }
    
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.qtk.services.impl;

import com.qtk.dto.CommentResponseDTO;
import com.qtk.pojo.Comment;
import com.qtk.pojo.User;
import com.qtk.repositories.CommentRepository;
import com.qtk.repositories.DocumentRepository;
import com.qtk.repositories.UserRepository;
import com.qtk.services.CommentService;
import java.util.List;
import java.util.Objects;
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
    
    private CommentResponseDTO todto(Comment c) {
        CommentResponseDTO dto = new CommentResponseDTO();
        dto.setContent(c.getContent());
        dto.setUsername(c.getUserId().getEmail());
        dto.setDocumentId(c.getDocumentId().getId());
        return dto;
    }

    @Override
    public List<CommentResponseDTO> getCommemts(int documentId) {
        return this.commentRepo.getCommemts(documentId).stream().map(this::todto).toList();
    }

    @Override
    public CommentResponseDTO addComment(int documentId, String content) {
        Comment c = new Comment();
        if (content == null && content.isEmpty())
            throw new IllegalArgumentException("Chưa có nội dung");
        c.setContent(content);
        c.setDocumentId(this.docRepo.getDocumentById(documentId));
        c.setUserId(this.userRepo.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName()));
        
        this.commentRepo.addComment(c);      
        return  this.todto(c);
    }

    @Override
    public void deleteComment(int commentId) {
        Comment c = this.commentRepo.getCommentById(commentId);
        User u = this.userRepo.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        
        if (!Objects.equals(c.getUserId().getId(), u.getId()))
            throw new IllegalArgumentException("Không thể xoá bình luận của người khác");
        
        this.commentRepo.deleteComment(commentId);
    }
    
}

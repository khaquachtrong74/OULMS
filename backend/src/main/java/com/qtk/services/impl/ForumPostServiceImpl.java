/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.qtk.services.impl;

import com.qtk.dto.ForumPostResponseDTO;
import com.qtk.pojo.ForumPost;
import com.qtk.pojo.User;
import com.qtk.repositories.ForumPostRepository;
import com.qtk.repositories.UserRepository;
import com.qtk.services.ForumPostService;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 *
 * @author ngodo
 */
@Service
public class ForumPostServiceImpl implements ForumPostService{
    
    @Autowired
    private ForumPostRepository frPortRepo;
    
    @Autowired
    private UserRepository userRepo;
    
    private ForumPostResponseDTO toDTO(ForumPost f) {
        ForumPostResponseDTO dto = new ForumPostResponseDTO();
        dto.setId(f.getId());
        dto.setTitle(f.getTitle());
        dto.setContent(f.getContent());
        dto.setUserEmail(f.getUserId().getEmail());
        dto.setCreatedAt(f.getCreatedAt());
        return dto;
    }

    @Override
    public List<ForumPostResponseDTO> getForumPost(Map<String, String> params) {
        return this.frPortRepo.getForumPost(params).stream().map(this::toDTO).toList();
    }

    @Override
    public ForumPostResponseDTO createForumPost(String title, String content) {
        if (title == null || content == null)
            throw  new IllegalArgumentException("Chưa nhập đủ thông tin");
        
        ForumPost f = new ForumPost();
        f.setTitle(title);
        f.setContent(content);
        f.setUserId(this.userRepo.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName()));
        
        this.frPortRepo.createForumPost(f);
        return toDTO(f);
    }

    @Override
    public ForumPostResponseDTO getForumPostDetail(int forumPostId) {
        return toDTO(this.frPortRepo.getForumPostById(forumPostId));
    }

    @Override
    public void deteleForumPost(int forumPostId) {
        ForumPost f = this.frPortRepo.getForumPostById(forumPostId);
        User u = this.userRepo.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if (!Objects.equals(f.getUserId().getId(), u.getId()))
            throw new IllegalArgumentException("Không thể xoá diễn dàn của người khác");
        
        this.frPortRepo.deteleForumPost(forumPostId);
    }

    @Override
    public long countForumPost(Map<String, String> params) {
        return this.frPortRepo.countForumPost(params);
    }

    @Override
    public int getForumPostPageSize() {
        return this.frPortRepo.getForumPostPageSize();
    }
    
}

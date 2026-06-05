/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.qtk.services;

import com.qtk.dto.ForumPostResponseDTO;
import com.qtk.pojo.ForumPost;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ngodo
 */
public interface ForumPostService {
    List<ForumPostResponseDTO> getForumPost(Map<String, String> params);
    ForumPostResponseDTO createForumPost(String title, String content);
    ForumPostResponseDTO getForumPostDetail(int forumPostId);
    void deteleForumPost(int forumPostId);
    
    long countForumPost(Map<String, String> params);
    int getForumPostPageSize();
}

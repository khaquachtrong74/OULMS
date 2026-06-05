/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.qtk.repositories;

import com.qtk.pojo.ForumPost;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ngodo
 */
public interface ForumPostRepository {
    void createForumPost(ForumPost f);
    List<ForumPost> getForumPost(Map<String, String> params);
    ForumPost getForumPostById(int id);
    void deteleForumPost(int id);
    
    long countForumPost(Map<String, String> params);
    int getForumPostPageSize();
}

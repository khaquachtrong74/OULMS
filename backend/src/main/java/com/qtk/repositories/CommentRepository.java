/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.qtk.repositories;

import com.qtk.pojo.Comment;
import java.util.List;

/**
 *
 * @author ngodo
 */
public interface CommentRepository {
    List<Comment> getCommemts(int documentId);
    Comment addComment(Comment c);
}

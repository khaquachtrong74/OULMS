/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.qtk.services;

import com.qtk.dto.CommentResponseDTO;
import com.qtk.pojo.Comment;
import java.util.List;

/**
 *
 * @author ngodo
 */
public interface CommentService {
    List<CommentResponseDTO> getCommemts(int documentId);
    CommentResponseDTO addComment(int documentId, String content);
    void deleteComment(int commentId);
}

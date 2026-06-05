/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.qtk.dto;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author ngodo
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ForumPostResponseDTO {
    private int id;
    private String title;
    private String content;
    private String userEmail;
    private Date createdAt;
}

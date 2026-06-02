/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.qtk.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author ngodo
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DocumentRequestDTO {
    private String title;
    private String description;
    private Integer topicId;
    private Integer levelId;
    private MultipartFile file;
}

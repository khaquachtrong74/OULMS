/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.qtk.dto;

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
public class DocumentResponseDTO {
    private Integer id;
    private String title;
    private String description;
    private String fileUrl;
    private String status;

    private Integer topicId;
    private String topicName;

    private Integer levelId;
    private String levelName;
}

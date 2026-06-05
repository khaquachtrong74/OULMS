/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.qtk.dto;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author lossrunner
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseResponseDTO {
    private Integer id;
    private String name;
    private String description;
    private Integer price;
    
    private Date createdAt;
    private Date updatedAt;
    
    private Integer subjectId; 
    private String subjectName;
    
    private Integer lecturerId;
    private String lecturerName;
    
    
}

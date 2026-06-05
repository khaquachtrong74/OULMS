/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.qtk.dto;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
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
public class CourseRequestDTO {
    private Integer id;
    @NotBlank(message = "Tên khóa học không được để trống")
    private String name;
    private String description;
    @NotNull(message = "Giá không được để trống")
    @Min(value = 0, message = "Giá khóa học phải lớn hơn hoặc bằng 0")
    private Integer price;
    private Integer subjectId; 
    private Integer lecturerId;
}

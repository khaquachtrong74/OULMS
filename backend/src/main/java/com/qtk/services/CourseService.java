/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.qtk.services;

import com.qtk.dto.CourseRequestDTO;
import com.qtk.dto.CourseResponseDTO;
import com.qtk.pojo.Course;
import java.util.List;
import java.util.Map;

/**
 *
 * @author lossrunner
 */
public interface CourseService {
    List<CourseResponseDTO> getCourses(Map<String, String> params);
    void addOrUpdateCourse(CourseRequestDTO dto);
    Course getCourseById(int id);
    void deleteCourse(int id);
    long countCourses(Map<String, String> params);
    int getCoursePageSize();
    CourseResponseDTO getCourseDetailById(int id);
}

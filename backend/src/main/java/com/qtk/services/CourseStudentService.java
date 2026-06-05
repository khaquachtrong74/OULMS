/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.qtk.services;

import com.qtk.dto.CourseResponseDTO;
import java.util.List;

/**
 *
 * @author lossrunner
 */
public interface CourseStudentService {
    void enrollStudent(int courseId, int studentId);
    
    List<CourseResponseDTO> getEnrolledCoursesByUser(int userId);
}

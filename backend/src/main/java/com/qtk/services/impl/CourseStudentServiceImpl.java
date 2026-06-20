/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.qtk.services.impl;

import com.qtk.dto.CourseResponseDTO;
import com.qtk.pojo.Course;
import com.qtk.pojo.CourseStudent;
import com.qtk.pojo.User;
import com.qtk.repositories.CourseStudentRepository;
import com.qtk.services.CourseStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
/**
 *
 * @author lossrunner
 */


@Service
@Transactional
public class CourseStudentServiceImpl implements CourseStudentService {

    @Autowired
    private CourseStudentRepository courseStudentRepository;

    
    private CourseResponseDTO toDTO(Course c) {
        return CourseResponseDTO.builder().id(c.getId())
                .name(c.getName())
                .description(c.getDescription())
                .price(c.getPrice())
                .subjectId(c.getSubjectId().getId())
                .lecturerId(c.getLecturerId().getId())
                .subjectName(c.getSubjectId().getName())
                .lecturerName(c.getLecturerId().getFirstName() + " "
                        + c.getLecturerId().getLastName()).build();
    }
    @Override
    public void enrollStudent(int courseId, int studentId) {
        if (this.courseStudentRepository.isStudentEnrolled(courseId, studentId)) {
            throw new IllegalStateException("Sinh viên đã được ghi danh vào khóa học này rồi!");
        }

        CourseStudent enrollment = new CourseStudent();
        
        Course courseProxy = new Course();
        courseProxy.setId(courseId);
        
        User userProxy = new User();
        userProxy.setId(studentId);

        enrollment.setCourseId(courseProxy);
        enrollment.setStudentId(userProxy);
        enrollment.setEnrolledAt(new Date());

        this.courseStudentRepository.addCourseStudent(enrollment);
    }
    
    @Override
    public List<CourseResponseDTO> getEnrolledCoursesByUser(int userId) {
        List<Course> enrolledCourses = this.courseStudentRepository.getCoursesByUserId(userId);
        return enrolledCourses.stream().map(this::toDTO).toList();
    }
    
    @Override
    public Long countTotalEnrollments() {
        return this.courseStudentRepository.countTotalEnrollments();
    }
}
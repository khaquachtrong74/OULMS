/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.qtk.services.impl;

import com.qtk.dto.CourseRequestDTO;
import com.qtk.dto.CourseResponseDTO;
import com.qtk.pojo.Course;
import com.qtk.pojo.Subject;
import com.qtk.pojo.User;
import com.qtk.repositories.CourseRepository;
import com.qtk.services.CourseService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author lossrunner
 */
@Service
@Transactional
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepository courseRepository;

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
    public List<CourseResponseDTO> getCourses(Map<String, String> params) {
        return this.courseRepository.getCourses(params).stream().map(this::toDTO).toList();
    }

    @Override
    public void addOrUpdateCourse(CourseRequestDTO dto) {
        Course c = new Course();
        c.setId(dto.getId());
        c.setName(dto.getName());
        c.setDescription(dto.getDescription());
        c.setPrice(dto.getPrice());

        if (dto.getSubjectId() != null) {
            Subject s = new Subject();
            s.setId(dto.getSubjectId());
            c.setSubjectId(s);
        }

        if (dto.getLecturerId() != null) {
            User u = new User();
            u.setId(dto.getLecturerId());
            c.setLecturerId(u);
        }
        this.courseRepository.addOrUpdateCourse(c);
    }

    @Override
    public Course getCourseById(int id) {
        return this.courseRepository.getCourseById(id);
    }

    @Override
    public void deleteCourse(int id) {
        this.courseRepository.deleteCourse(id);
    }

    @Override
    public CourseResponseDTO getCourseDetailById(int id) {
        Course c = this.courseRepository.getCourseById(id);
        if (c == null) {
            return null;
        }
        return toDTO(c);
    }
}

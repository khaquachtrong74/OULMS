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

    @Override
    public void enrollStudent(int courseId, int studentId) {
        // 1. Guard Clause (Bảo vệ dữ liệu): Kiểm tra xem sinh viên đã có trong lớp chưa
        // Mục đích: Tránh việc Hibernate ném lỗi SQLIntegrityConstraintViolationException làm sập luồng
        if (this.courseStudentRepository.isStudentEnrolled(courseId, studentId)) {
            throw new IllegalStateException("Sinh viên đã được ghi danh vào khóa học này rồi!");
        }

        // 2. Tạo đối tượng ghi danh (Dùng kỹ thuật Proxy để tránh truy vấn thừa)
        CourseStudent enrollment = new CourseStudent();
        
        // Chỉ cần gán ID vào object vỏ rỗng (không cần query lại cả User và Course từ DB)
        Course courseProxy = new Course();
        courseProxy.setId(courseId);
        
        User userProxy = new User();
        userProxy.setId(studentId);

        enrollment.setCourseId(courseProxy);
        enrollment.setStudentId(userProxy);
        enrollment.setEnrolledAt(new Date());

        // 3. Đẩy xuống Repository để lưu
        this.courseStudentRepository.addCourseStudent(enrollment);
    }
    
    @Override
    public List<CourseResponseDTO> getEnrolledCoursesByUser(int userId) {
        // 1. Gọi Repository để lấy danh sách Entity Course đã được JOIN sẵn tối ưu từ DB
        List<Course> enrolledCourses = this.courseStudentRepository.getCoursesByUserId(userId);

        // 2. Tiến hành Mapping dữ liệu Entity sang DTO (Data Transfer Object) bằng Stream API
        return enrolledCourses.stream().map(c -> 
            CourseResponseDTO.builder()
                .id(c.getId())
                .name(c.getName())
                .description(c.getDescription())
                .price(c.getPrice())
                .subjectName(c.getSubjectId() != null ? c.getSubjectId().getName() : "Chưa phân loại")
                .lecturerName(c.getLecturerId() != null ? 
                    c.getLecturerId().getFirstName() + " " + c.getLecturerId().getLastName() : "Đang cập nhật")
                .build()
        ).collect(Collectors.toList());
    }
}
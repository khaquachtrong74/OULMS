/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.qtk.controllers;

/**
 *
 * @author lossrunner
 */

import com.qtk.dto.CourseDetailResponseDTO;
import com.qtk.dto.CourseResponseDTO;
import com.qtk.pojo.Course;
import com.qtk.services.CourseService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = {"http://localhost:3000", "https://quietly-untrashed-darby.ngrok-free.dev"}, allowCredentials = "true")
public class ApiCourseController {

    @Autowired
    private CourseService courseService;

    @GetMapping("/courses")
    public ResponseEntity<List<CourseResponseDTO>> getCourses(@RequestParam Map<String, String> params){
        try {
            return new ResponseEntity<>(this.courseService.getCourses(params), HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }
    
    
    @PostMapping(value = "/courses/{id}/enroll", produces = "application/json")
    public ResponseEntity<?> enrollCourse(@PathVariable(value = "id") int id) {
        try {

            Map<String, String> responseBody = new HashMap<>();
            responseBody.put("message", "Ghi danh khóa học thành công!");
            
            return new ResponseEntity<>(responseBody, HttpStatus.OK);
            
        } catch (Exception e) {
            Map<String, String> errorBody = new HashMap<>();
            errorBody.put("message", "Hệ thống đang gặp lỗi khi xử lý thanh toán/ghi danh.");
            
            return new ResponseEntity<>(errorBody, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/courses/{id}")
    public ResponseEntity<CourseResponseDTO> getCourseDetailed(@PathVariable(value = "id") int id) {
        try {
            CourseResponseDTO courseDetail = this.courseService.getCourseDetailById(id);
            if (courseDetail == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(courseDetail, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @DeleteMapping("/courses/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT) 
    public void deleteCourse(@PathVariable(value = "id") int id) {
        this.courseService.deleteCourse(id);
    }
}
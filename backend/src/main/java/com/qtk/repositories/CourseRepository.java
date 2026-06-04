/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.qtk.repositories;

import com.qtk.pojo.Course;
import java.util.List;
import java.util.Map;

/**
 *
 * @author lossrunner
 */
public interface CourseRepository {
    public List<Course> getCourses(Map<String, String> params);
    public void addOrUpdateCourse(Course c);
    public Course getCourseById(int id);
    public void deleteCourse(int id);
}

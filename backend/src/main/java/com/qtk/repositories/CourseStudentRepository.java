/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.qtk.repositories;

import com.qtk.pojo.CourseStudent;

/**
 *
 * @author lossrunner
 */
public interface CourseStudentRepository {
    void addCourseStudent(CourseStudent courseStudent);
    boolean isStudentEnrolled(int courseId, int studentId);
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.qtk.repositories.impl;

import com.qtk.pojo.CourseStudent;
import com.qtk.repositories.CourseStudentRepository;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author lossrunner
 */

@Repository
@Transactional
public class CourseStudentRepositoryImpl implements CourseStudentRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public void addCourseStudent(CourseStudent courseStudent) {
        Session session = this.factory.getObject().getCurrentSession();
        session.save(courseStudent);
    }

    @Override
    public boolean isStudentEnrolled(int courseId, int studentId) {
        Session session = this.factory.getObject().getCurrentSession();
        
        // Truy vấn xem cặp course_id và student_id đã tồn tại chưa
        Query query = session.createQuery(
            "SELECT COUNT(c.id) FROM CourseStudent c WHERE c.courseId.id = :courseId AND c.studentId.id = :studentId"
        );
        query.setParameter("courseId", courseId);
        query.setParameter("studentId", studentId);
        
        long count = (long) query.uniqueResult();
        return count > 0;
    }
}
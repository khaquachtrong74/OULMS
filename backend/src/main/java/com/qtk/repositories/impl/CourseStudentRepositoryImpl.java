/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.qtk.repositories.impl;

import com.qtk.pojo.Course;
import com.qtk.pojo.CourseStudent;
import com.qtk.repositories.CourseStudentRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;
import java.util.List;
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
    
    @Override
    public List<Course> getCoursesByUserId(int userId) {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        
        // 1. Tạo câu truy vấn có kiểu trả về là Course
        CriteriaQuery<Course> query = builder.createQuery(Course.class);
        
        // 2. Chọn gốc (Root) để bắt đầu quét dữ liệu là bảng trung gian CourseStudent
        Root<CourseStudent> root = query.from(CourseStudent.class);
        
        // 3. Thực hiện phép JOIN từ CourseStudent sang Course (dựa vào thuộc tính "courseId" khai báo trong POJO)
        Join<CourseStudent, Course> courseJoin = root.join("courseId", JoinType.INNER);
        
        // [Phỏng đoán] Thuộc tính subjectId và lecturerId trong Course là FetchType.LAZY
        // Ta dùng fetch để Hibernate lấy dữ liệu ngay trong 1 câu lệnh SQL, tránh lỗi N+1 Query.
        courseJoin.fetch("subjectId", JoinType.LEFT);
        courseJoin.fetch("lecturerId", JoinType.LEFT);
        
        // 4. Chỉ định kết quả lấy ra là thực thể Course đã được JOIN
        query.select(courseJoin);
        
        // 5. Điều kiện lọc: studentId.id trong CourseStudent phải bằng userId truyền vào
        query.where(builder.equal(root.get("studentId").get("id"), userId));
        
        // 6. Thực thi truy vấn
        Query q = session.createQuery(query);
        return q.getResultList();
    }
}
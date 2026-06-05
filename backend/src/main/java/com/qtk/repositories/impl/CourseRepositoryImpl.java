/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.qtk.repositories.impl;

import com.qtk.pojo.Course;
import com.qtk.pojo.CourseStudent;
import com.qtk.repositories.CourseRepository;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author lossrunner
 */
@Repository
@Transactional
@PropertySource("classpath:pagination.properties")
public class CourseRepositoryImpl implements CourseRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Autowired
    private Environment env;

    @Override
    public int getCoursePageSize() {
        return this.env.getProperty("courses.pageSize", Integer.class, 9);
    }

    @Override
    public long countCourses(Map<String, String> params) {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();

        // 1. Khởi tạo cây AST (Abstract Syntax Tree) để lấy giá trị Long (đếm số lượng)
        CriteriaQuery<Long> query = builder.createQuery(Long.class);
        Root<Course> root = query.from(Course.class);

        // 2. Định nghĩa hành động: SELECT COUNT(c.id)
        query.select(builder.count(root));

        // 3. THAY THẾ PLACEHOLDER: Bắt đầu bê nguyên giỏ điều kiện lọc từ getCourses xuống
        List<Predicate> predicates = new ArrayList<>();

        if (params != null) {
            // Lọc theo từ khóa
            String kw = params.get("kw");
            if (kw != null && !kw.isEmpty()) {
                predicates.add(builder.like(root.get("name"), String.format("%%%s%%", kw)));
            }

            // Lọc theo danh mục môn học
            String subjectId = params.get("subjectId");
            if (subjectId != null && !subjectId.isEmpty()) {
                predicates.add(builder.equal(root.get("subjectId").get("id"), Integer.valueOf(subjectId)));
            }

            // Lọc bỏ những khóa đã đăng ký (Core Logic)
            String currentUserId = params.get("currentUserId");
            if (currentUserId != null && !currentUserId.isEmpty()) {
                Subquery<Integer> subquery = query.subquery(Integer.class);
                Root<CourseStudent> subRoot = subquery.from(CourseStudent.class);

                subquery.select(subRoot.get("courseId").get("id"));
                subquery.where(builder.equal(subRoot.get("studentId").get("id"), Integer.valueOf(currentUserId)));

                predicates.add(builder.not(root.get("id").in(subquery)));
            }
        }

        // Nhúng giỏ điều kiện vào mệnh đề WHERE
        if (!predicates.isEmpty()) {
            query.where(predicates.toArray(new Predicate[0]));
        }

        // 4. Thực thi và trả về kết quả duy nhất (tổng số lượng)
        return session.createQuery(query).getSingleResult();
    }

    @Override
    public List<Course> getCourses(Map<String, String> params) {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Course> query = builder.createQuery(Course.class);
        Root<Course> root = query.from(Course.class);

        // Tối ưu hóa truy vấn, tránh N+1 Query
        root.fetch("subjectId", JoinType.LEFT);
        root.fetch("lecturerId", JoinType.LEFT);

        // Cần thêm distinct vì fetch join có thể làm nhân bản dòng dữ liệu
        query.select(root).distinct(true);

        List<Predicate> predicates = new ArrayList<>();

        if (params != null) {
            String kw = params.get("kw");
            if (kw != null && !kw.isEmpty()) {
                predicates.add(builder.like(root.get("name"), String.format("%%%s%%", kw)));
            }

            String subjectId = params.get("subjectId");
            if (subjectId != null && !subjectId.isEmpty()) {
                predicates.add(builder.equal(root.get("subjectId").get("id"), Integer.valueOf(subjectId)));
            }

            String currentUserId = params.get("currentUserId");
            if (currentUserId != null && !currentUserId.isEmpty()) {
                Subquery<Integer> subquery = query.subquery(Integer.class);
                Root<CourseStudent> subRoot = subquery.from(CourseStudent.class);

                subquery.select(subRoot.get("courseId").get("id"));
                subquery.where(builder.equal(subRoot.get("studentId").get("id"), Integer.valueOf(currentUserId)));

                predicates.add(builder.not(root.get("id").in(subquery)));
            }
        }

        if (!predicates.isEmpty()) {
            query.where(predicates.toArray(new Predicate[0]));
        }

        query.orderBy(builder.desc(root.get("id")));
        Query q = session.createQuery(query);

        if (params != null) {
            int page = Integer.parseInt(params.getOrDefault("page", "1"));
            int pageSize = getCoursePageSize();

            int start = (page - 1) * pageSize;

            q.setMaxResults(pageSize);
            q.setFirstResult(start);
        }
        return q.getResultList();
    }

    @Override
    public void addOrUpdateCourse(Course c) {
        Session session = this.factory.getObject().getCurrentSession();
        if (c.getId() != null) {
            session.merge(c);
        } else {
            session.persist(c);
        }
    }

    @Override
    public Course getCourseById(int id) {
        Session session = this.factory.getObject().getCurrentSession();
        return session.get(Course.class, id);
    }

    @Override
    public void deleteCourse(int id) {
        Session session = this.factory.getObject().getCurrentSession();
        Course c = this.getCourseById(id);
        if (c != null) {
            session.remove(c);
        } else {
            throw new RuntimeException("Không tìm thấy khóa học với id: " + id);
        }
    }

}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.qtk.repositories.impl;

import com.qtk.pojo.Course;
import com.qtk.repositories.CourseRepository;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.hibernate.Session;
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
public class CourseRepositoryImpl implements CourseRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<Course> getCourses(Map<String, String> params) {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Course> query = builder.createQuery(Course.class);
        Root<Course> root = query.from(Course.class);
        root.fetch("subjectId", JoinType.LEFT);
        root.fetch("lecturerId", JoinType.LEFT);
        query.select(root);
        List<Predicate> predicates = new ArrayList<>();

        if (params != null) {
            String kw = params.get("kw");
            if (kw != null && !kw.isEmpty()) {
                Predicate p = builder.like(root.get("name"), String.format("%%%s%%", kw));
                query.where(p);
            }
            
            String subjectId = params.get("subjectId");
            if (subjectId != null && !subjectId.isEmpty()) {
                predicates.add(
                        builder.equal(
                                root.get("subjectId").get("id"),
                                Integer.valueOf(subjectId)
                        )
                );
            }
        }

        if (!predicates.isEmpty()) {
            query.where(predicates.toArray(new Predicate[0])); 
        }
        query.orderBy(builder.desc(root.get("id")));
        Query q = session.createQuery(query);
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

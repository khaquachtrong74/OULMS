/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.qtk.repositories.impl;

import com.qtk.pojo.Subject;
import com.qtk.repositories.SubjectRepository;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.List;
import java.util.Map;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author ngodo
 */
@Repository
@Transactional
public class SubjectRepositoryImpl implements  SubjectRepository{
    
    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<Subject> getSubject(Map<String, String> params) {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Subject> query = builder.createQuery(Subject.class);
        Root root = query.from(Subject.class);
        query.select(root);
        if (params != null) {
            String kw = params.get("kw");
            if (kw != null && !kw.isEmpty()) {
                Predicate p = builder.like(root.get("name"), String.format("%%%s%%", kw));
                query.where(p);
            }
        }       
        Query q = session.createQuery(query);
        return q.getResultList();
    }

    @Override
    public void addOrUpdateSubject(Subject s) {
        Session session = this.factory.getObject().getCurrentSession();
        if(s.getId() != null)
            session.merge(s);
        else
            session.persist(s);
    }
    
    @Override
    public Subject getSubjectById(int id) {
        Session session = this.factory.getObject().getCurrentSession();
        return session.get(Subject.class, id);
    }

    @Override
    public void deleteSubject(int id) {
        Session session = this.factory.getObject().getCurrentSession();
        Subject s = this.getSubjectById(id);
        session.remove(s);
    } 
}

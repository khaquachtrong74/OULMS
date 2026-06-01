/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.qtk.repositories.impl;

import com.qtk.pojo.Document;
import com.qtk.repositories.DocumentRepository;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
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
public class DocumentRepositoryImpl implements DocumentRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<Document> getDocuments(Map<String, String> params) {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Document> query = builder.createQuery(Document.class);
        Root root = query.from(Document.class);
        query.select(root);
        
        List<Predicate> predicates = new ArrayList<>();
        
        predicates.add(builder.equal(root.get("status").as(String.class), "ACTIVE"));
        
        if (params != null) {
            String kw = params.get("kw");
            if (kw != null && !kw.isEmpty()) {
                predicates.add(builder.like(builder.lower(root.get("title")), String.format("%%%s%%", kw.toLowerCase())));
            }
            
            String type = params.get("type");
            if (type != null && !type.isEmpty() && !type.equals("Tất cả")) {
                predicates.add(builder.equal(root.get("type").as(String.class), type));
            }
        }
        
        query.where(predicates.toArray(Predicate[]::new));
        query.orderBy(builder.desc(root.get("id"))); 
        
        Query q = session.createQuery(query);
        return q.getResultList();
    }

    @Override
    public List<Document> getDocumentsByUserId(int userId, Map<String, String> params) {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Document> query = builder.createQuery(Document.class);
        Root root = query.from(Document.class);
        query.select(root);
        
        List<Predicate> predicates = new ArrayList<>();
        
        predicates.add(builder.equal(root.get("uploadedBy"), userId));
        predicates.add(root.get("status").as(String.class).in("ACTIVE", "HIDDEN"));
        
        if (params != null) {
            String kw = params.get("kw");
            if (kw != null && !kw.isEmpty()) {
                predicates.add(builder.like(builder.lower(root.get("title")), String.format("%%%s%%", kw.toLowerCase())));
            }
            
            String type = params.get("type");
            if (type != null && !type.isEmpty() && !type.equals("Tất cả")) {
                predicates.add(builder.equal(root.get("type").as(String.class), type));
            }
        }
        
        query.where(predicates.toArray(Predicate[]::new));
        query.orderBy(builder.desc(root.get("id")));
        
        Query q = session.createQuery(query);
        return q.getResultList();
    }
}

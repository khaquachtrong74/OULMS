/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.qtk.repositories.impl;

import com.qtk.enums.DocumentStatus;
import com.qtk.pojo.Document;
import com.qtk.repositories.DocumentRepository;
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
 * @author ngodo
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
        
        root.fetch("topicId", JoinType.LEFT);
        root.fetch("levelId", JoinType.LEFT);
        root.fetch("uploadedBy", JoinType.LEFT);
        
        query.select(root).distinct(true);
        
        List<Predicate> predicates = new ArrayList<>();

        if (params != null) {
            String kw = params.get("kw");
            if (kw != null && !kw.isEmpty()) {
                predicates.add(builder.like(root.get("title"), String.format("%%%s%%", kw)));
            }

            String status = params.get("status");
            if (status != null && !status.isEmpty()) {
                predicates.add(builder.equal(root.get("status"), DocumentStatus.valueOf(status.trim().toUpperCase())));
            }

            String topicId = params.get("topicId");
            if (topicId != null && !topicId.isEmpty()) {
                predicates.add(builder.equal(root.get("topicId").get("id"), Integer.valueOf(topicId)));
            }

            String levelId = params.get("levelId");
            if (levelId != null && !levelId.isEmpty()) {
                predicates.add(builder.equal(root.get("levelId").get("id"), Integer.valueOf(levelId)));
            }
            String subjectId = params.get("subjectId");
            if (subjectId != null && !subjectId.isEmpty()) {
                predicates.add(
                        builder.equal(
                                root.get("topicId")
                                        .get("subjectId")
                                        .get("id"),
                                Integer.valueOf(subjectId)
                        )
                );
            }
        }
        if (!predicates.isEmpty()) {
            query.where(predicates.toArray(Predicate[]::new));
        }

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
        root.fetch("topicId", JoinType.LEFT);
        root.fetch("levelId", JoinType.LEFT);
        root.fetch("uploadedBy", JoinType.LEFT);
        query.select(root);

        List<Predicate> predicates = new ArrayList<>();

        predicates.add(builder.equal(root.get("uploadedBy").get("id"), userId));

        if (params != null) {
            String kw = params.get("kw");
            if (kw != null && !kw.isEmpty()) {
                predicates.add(builder.like(root.get("title"), String.format("%%%s%%", kw)));
            }

            String topicId = params.get("topicId");
            if (topicId != null && !topicId.isEmpty()) {
                predicates.add(builder.equal(root.get("topicId").get("id"), Integer.valueOf(topicId)));
            }

            String levelId = params.get("levelId");
            if (levelId != null && !levelId.isEmpty()) {
                predicates.add(builder.equal(root.get("levelId").get("id"), Integer.valueOf(levelId)));
            }

        }
        if (!predicates.isEmpty()) {
            query.where(predicates.toArray(Predicate[]::new));
        }
        query.orderBy(builder.desc(root.get("id")));
        Query q = session.createQuery(query);
        return q.getResultList();
    }

    @Override
    public void addOrUpdateDocument(Document d) {
        Session session = this.factory.getObject().getCurrentSession();
        if (d.getId() != null) {
            session.merge(d);
        } else {
            session.persist(d);
        }
    }

    @Override
    public Document getDocumentById(int id) {
        Session session = this.factory.getObject().getCurrentSession();
        return session.get(Document.class, id);
    }

    @Override
    public void updateDocumentStatus(int id, DocumentStatus status) {
        Session session = this.factory.getObject().getCurrentSession();
        Document d = this.getDocumentById(id);
        if (d != null) {
            d.setStatus(status);
            session.merge(d);
        }
    }

    @Override
    public void deleteDocument(int id) {
        Session session = this.factory.getObject().getCurrentSession();
        Document d = this.getDocumentById(id);
        if (d != null) {
            session.remove(d);
        } else {

            throw new RuntimeException("Document not found with id: " + id);
        }

    }

}

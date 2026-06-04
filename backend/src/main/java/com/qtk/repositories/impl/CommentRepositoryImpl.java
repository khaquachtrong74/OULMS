/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.qtk.repositories.impl;

import com.qtk.pojo.Comment;
import com.qtk.repositories.CommentRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
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
 * @author ngodo
 */
@Repository
@Transactional
public class CommentRepositoryImpl implements CommentRepository{
    
    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<Comment> getCommemts(int documentId) {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Comment> query = builder.createQuery(Comment.class);
        Root root = query.from(Comment.class);
        root.fetch("userId", JoinType.LEFT);
        
        query.select(root).distinct(true);
        
        Predicate p = builder.equal(root.get("documentId").get("id"), documentId);
        query.where(p);
        query.orderBy(builder.desc(root.get("id")));
        
        Query q = session.createQuery(query);
        return q.getResultList();
    }

    @Override
    public Comment addComment(Comment c) {
        Session session = this.factory.getObject().getCurrentSession();
        session.persist(c);
        return c;
    }

    @Override
    public Comment getCommentById(int id) {
        Session session = this.factory.getObject().getCurrentSession();
        return session.get(Comment.class, id);
    }

    @Override
    public void deleteComment(int id) {
        Session session = this.factory.getObject().getCurrentSession();
        Comment c = this.getCommentById(id);
        session.remove(c);
    }
    
    
    
}

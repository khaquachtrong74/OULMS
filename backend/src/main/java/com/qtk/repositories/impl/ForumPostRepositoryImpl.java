/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.qtk.repositories.impl;

import com.qtk.pojo.ForumPost;
import com.qtk.repositories.ForumPostRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author ngodo
 */
@Repository
@PropertySource("classpath:pagination.properties")
@Transactional
public class ForumPostRepositoryImpl implements ForumPostRepository{
    
    @Autowired
    private Environment env;
    
    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<ForumPost> getForumPost(Map<String, String> params) {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<ForumPost> query = builder.createQuery(ForumPost.class);
        Root root = query.from(ForumPost.class);
        root.fetch("userId", JoinType.LEFT);
        
        query.select(root);
        if (params != null && !params.isEmpty()) {
            String kw = params.get("kw");
            if (kw != null && !kw.isEmpty()) {
                Predicate p = builder.equal(root.get("title"), String.format("%%%s%%", kw));
                query.where(p);
            }          
        }
        query.orderBy(builder.desc(root.get("id")));
        Query q = session.createQuery(query);
        
        if (params != null) {
            int page = Integer.parseInt(params.getOrDefault("page", "1"));
            int pageSize = getForumPostPageSize();
            int start = (page - 1) * pageSize;

            q.setMaxResults(pageSize);
            q.setFirstResult(start);
        }
        
        return q.getResultList();
    }
    
    @Override
    public void createForumPost(ForumPost f) {
        Session session = this.factory.getObject().getCurrentSession();
        session.persist(f);
    }

    @Override
    public ForumPost getForumPostById(int id) {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<ForumPost> query = builder.createQuery(ForumPost.class);
        Root root = query.from(ForumPost.class);
        root.fetch("userId", JoinType.LEFT);

        query.select(root).where(builder.equal(root.get("id"), id));

        return session.createQuery(query).getSingleResult();
    }

    @Override
    public void deteleForumPost(int id) {
        Session session = this.factory.getObject().getCurrentSession();
        ForumPost t = this.getForumPostById(id);
        session.remove(t);
    }

    @Override
    public long countForumPost(Map<String, String> params) {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Long> query = builder.createQuery(Long.class);
        Root<ForumPost> root = query.from(ForumPost.class);
        query.select(builder.count(root));

        List<Predicate> predicates = new ArrayList<>();

        if (params != null) {
            String kw = params.get("kw");
            if (kw != null && !kw.isEmpty()) {
                predicates.add(builder.like(root.get("title"), String.format("%%%s%%", kw)));
            }
        }

        if (!predicates.isEmpty()) {
            query.where(predicates.toArray(Predicate[]::new));
        }

        return session.createQuery(query).getSingleResult();
    }

    @Override
    public int getForumPostPageSize() {
        return this.env.getProperty("forumPost.pageSize", Integer.class);
    }
    
    
    
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.qtk.repositories.impl;

import com.qtk.pojo.Tag;
import com.qtk.repositories.TagRepository;
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
 * @author lossrunner
 */

@Repository
@Transactional
public class TagRepositoryImpl implements TagRepository{

        
    @Autowired
    private LocalSessionFactoryBean factory;

    
    @Override
    public List<Tag> getTag(Map<String, String> params) {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Tag> query = builder.createQuery(Tag.class);
        Root root = query.from(Tag.class);
        query.select(root);
        if (params != null) {
            String kw = params.get("kw");
            if (kw != null && !kw.isEmpty()) {
                Predicate p = builder.like(root.get("name"), String.format("%%%s%%", kw));
                query.where(p);
            }
        }
        query.orderBy(builder.desc(root.get("id")));
        Query q = session.createQuery(query);
        return q.getResultList();
    }

    @Override
    public void addOrUpdateTag(Tag t) {
        Session session = this.factory.getObject().getCurrentSession();
        if (t.getId() != null)
            session.merge(t);
        else
            session.persist(t);
    }

    @Override
    public Tag getTagById(int id) {
        Session session = this.factory.getObject().getCurrentSession();
        return session.get(Tag.class, id);
    }

    @Override
    public void deleteTag(int id) {
        Session session = this.factory.getObject().getCurrentSession();
        Tag t = this.getTagById(id);
        session.remove(t);
    }
    
}

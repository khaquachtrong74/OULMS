/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.qtk.repositories.impl;

import com.qtk.pojo.Level;
import com.qtk.repositories.LevelRepository;
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

/**
 *
 * @author ngodo
 */
@Repository
public class LevelRepositoryImpl implements LevelRepository{
    
    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<Level> getLevel(Map<String, String> params) {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Level> query = builder.createQuery(Level.class);
        Root root = query.from(Level.class);
        query.select(root);
        
        if (params!= null) {
            String kw = params.get("kw");
            Predicate p = builder.like(root.get("name"), String.format("%%%s%%", kw));
            query.where(p);
        }
        
        Query q = session.createQuery(query);
        return q.getResultList();
    }
    
}

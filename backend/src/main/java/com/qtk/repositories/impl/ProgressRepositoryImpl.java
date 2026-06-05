/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.qtk.repositories.impl;

import com.qtk.pojo.Progress;
import com.qtk.repositories.ProgressRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.util.List;
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
public class ProgressRepositoryImpl implements ProgressRepository{
    
    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public Progress getProgressByUserAndDocument(int userId, int documentId) {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Progress> query = builder.createQuery(Progress.class);
        Root root = query.from(Progress.class);

        query.select(root).where(
                builder.and(
                        builder.equal(root.get("userId").get("id"), userId),
                        builder.equal(root.get("documentId").get("id"), documentId)
                )
        );

        List<Progress> result = session.createQuery(query).getResultList();

        return result.isEmpty() ? null : result.get(0);
    }

    @Override
    public void addOrUpdateProgress(Progress p) {
        Session session = this.factory.getObject().getCurrentSession();

        if (p.getId() == null)
            session.persist(p);
        else
            session.merge(p);
    }
    
}

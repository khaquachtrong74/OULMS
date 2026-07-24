/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.qtk.repositories.impl;

import com.qtk.enums.UserRole;
import com.qtk.pojo.User;
import com.qtk.repositories.UserRepository;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author lossrunner
 */
@Repository
@Transactional
public class UserRepositoryImpl implements UserRepository {

    @Autowired
    private LocalSessionFactoryBean factory;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public User getUserByUsername(String username) {
        Session s = this.factory.getObject().getCurrentSession();
        Query query = s.createNamedQuery("User.findByEmail", User.class);
        query.setParameter("email", username);

        return (User) query.getSingleResult();

    }

    @Override
    public User addUser(User u) {
        Session s = this.factory.getObject().getCurrentSession();
        s.persist(u);

        return u;
    }

    @Override
    public boolean authenticate(String username, String password) {
        User u = this.getUserByUsername(username);
        return this.passwordEncoder.matches(password, u.getPassword());
    }

    @Override
    public User getUserByUserEmail(String userEmail) {
        Session s = this.factory.getObject().getCurrentSession();
        Query query = s.createNamedQuery("User.findByEmail", User.class);
        query.setParameter("email", userEmail);

        return (User) query.getResultStream().findFirst().orElse(null);
    }

    @Override
    public void deleteUser(int id) {
        Session s = this.factory.getObject().getCurrentSession();

        try {
            User userProxy = s.getReference(User.class, id);
            if (userProxy.getRole() == UserRole.ADMIN) {
                throw new IllegalStateException("Hệ thống từ chối: Không được phép xóa tài khoản Quản trị viên (ADMIN)!");
            }
            s.remove(userProxy);
        } catch (Exception e) {

        }

    }

    @Override
    public List<User> getAllUsers(Map<String, String> params) {
        Session session = this.factory.getObject().getCurrentSession();

        try {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<User> query = builder.createQuery(User.class);

            Root<User> root = query.from(User.class);
            query.select(root);

            if (params != null) {
                String kw = params.get("kw");
                if (kw != null && !kw.isEmpty()) {
                    String searchPattern = String.format("%%%s%%", kw);

                    Predicate pFirstName = builder.like(root.get("firstName"), searchPattern);
                    Predicate pLastName = builder.like(root.get("lastName"), searchPattern);
                    Predicate pEmail = builder.like(root.get("email"), searchPattern);

                    query.where(builder.or(pFirstName, pLastName, pEmail));
                }
            }

            query.orderBy(builder.desc(root.get("id")));

            var q = session.createQuery(query);
            return q.getResultList();

        } catch (Exception e) {
            System.err.println("Lỗi khi truy vấn danh sách User (Criteria API): " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public User getUserById(int id) {
        Session s = this.factory.getObject().getCurrentSession();
        return s.get(User.class, id);
    }

    @Override
    public User updateUser(User u) {
        Session s = this.factory.getObject().getCurrentSession();
        s.merge(u);
        return u;
    }

    @Override
    public List<User> getLecturers() {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<User> query = builder.createQuery(User.class);
        Root<User> root = query.from(User.class);
        query.select(root);

        Predicate p = builder.equal(root.get("role"), com.qtk.enums.UserRole.LECTURER);
        query.where(p);

        query.orderBy(builder.asc(root.get("firstName")));

        var q = session.createQuery(query);
        return q.getResultList();
    }
    
    @Override
    public List<User> getNewUsers(int limit) {
        Session session = this.factory.getObject().getCurrentSession();
        Query query = session.createNamedQuery("User.findAll", User.class);
        return  query.setMaxResults(limit).getResultList();
    }
    
    @Override
    public Long countTotalUsers() {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        
        CriteriaQuery<Long> query = builder.createQuery(Long.class);
        Root<User> root = query.from(User.class);
        
        query.select(builder.count(root.get("id")));
        
        return session.createQuery(query).getSingleResult();
    }
}

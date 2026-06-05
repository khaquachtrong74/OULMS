/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.qtk.repositories.impl;

/**
 *
 * @author lossrunner
 */
import com.qtk.pojo.Transaction;
import com.qtk.repositories.TransactionRepository;
import jakarta.persistence.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional 
public class TransactionRepositoryImpl implements TransactionRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public void addTransaction(Transaction transaction) {
        Session session = this.factory.getObject().getCurrentSession();
        // Chuyện gì xảy ra ở đây?
        // session.save() chưa gửi SQL INSERT xuống DB ngay lập tức.
        // Nó đưa đối tượng 'transaction' vào Persistence Context (L1 Cache).
        // Chỉ khi giao dịch (transaction) commit, Hibernate mới "flush" dữ liệu và tạo lệnh INSERT.
        session.save(transaction);
    }

    @Override
    public Transaction getTransactionById(int id) {
        Session session = this.factory.getObject().getCurrentSession();
        return session.get(Transaction.class, id);
    }

    @Override
    public Transaction getTransactionByRef(String transactionRef) {
        Session session = this.factory.getObject().getCurrentSession();
        Query query = session.createNamedQuery("Transaction.findByTransactionRef");
        query.setParameter("transactionRef", transactionRef);
        try {
            return (Transaction) query.getSingleResult();
        } catch (Exception e) {
            return null; // Không tìm thấy
        }
    }

    @Override
    public boolean updateTransactionStatus(String transactionRef, String status) {
        Session session = this.factory.getObject().getCurrentSession();
        Transaction t = this.getTransactionByRef(transactionRef);
        if (t != null) {
            t.setStatus(status);
            session.update(t); // Đánh dấu dirty, Hibernate sẽ sinh lệnh UPDATE khi flush
            return true;
        }
        return false;
    }
}
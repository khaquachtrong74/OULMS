/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.qtk.repositories.impl;

/**
 *
 * @author lossrunner
 */
import com.qtk.dto.RevenueChartDTO;
import com.qtk.enums.TransactionStatus;
import com.qtk.pojo.Transaction;
import com.qtk.repositories.TransactionRepository;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.util.List;
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

    @Override
    public Long getTotalRevenue() {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();

        CriteriaQuery<Long> query = builder.createQuery(Long.class);

        Root<Transaction> root = query.from(Transaction.class);

        query.select(builder.sumAsLong(root.get("amount")));

        query.where(builder.equal(root.get("status"), com.qtk.enums.TransactionStatus.SUCCESS.getStauts()));

        Long total = session.createQuery(query).getSingleResult();
        return total != null ? total : 0L;
    }

    @Override
    public Long countTransactions(String status) {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Long> query = builder.createQuery(Long.class);
        Root<Transaction> root = query.from(Transaction.class);

        query.select(builder.count(root.get("id")));
        query.where(builder.equal(root.get("status"), status));

        return session.createQuery(query).getSingleResult();
    }

    @Override
    public List<RevenueChartDTO> getRevenueStatistics(int lastMonths) {
        Session session = this.factory.getObject().getCurrentSession();
        String hql = "SELECT new com.qtk.dto.RevenueChartDTO("
                + " MONTH(t.createdAt), YEAR(t.createdAt), "
                + " SUM(CAST(t.amount AS long)), "
                + " COUNT(t.id)) "
                + "FROM Transaction t "
                + "WHERE t.status = :status "
                + "GROUP BY YEAR(t.createdAt), MONTH(t.createdAt) "
                + "ORDER BY YEAR(t.createdAt) DESC, MONTH(t.createdAt) DESC";

        Query query;
        query = session.createQuery(hql, RevenueChartDTO.class);
        query.setParameter("status", com.qtk.enums.TransactionStatus.SUCCESS.getStauts());
        query.setMaxResults(lastMonths);
        return query.getResultList();
    }

}

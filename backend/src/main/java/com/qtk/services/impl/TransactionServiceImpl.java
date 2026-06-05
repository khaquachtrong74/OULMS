/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.qtk.services.impl;

/**
 *
 * @author lossrunner
 */
import com.qtk.enums.TransactionPaymentMethod;
import com.qtk.enums.TransactionStatus;
import com.qtk.pojo.Course;
import com.qtk.pojo.CourseStudent;
import com.qtk.pojo.Transaction;
import com.qtk.pojo.User;
import com.qtk.repositories.CourseRepository;
import com.qtk.repositories.TransactionRepository;
import com.qtk.repositories.UserRepository;
import com.qtk.services.CourseStudentService;
import com.qtk.services.TransactionService;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private CourseStudentService courseStudentService;

    @Override
    public Transaction createPendingTransaction(int amount, TransactionPaymentMethod paymentMethod, int courseId, int userId) {
        // 1. Kiểm tra tính hợp lệ của dữ liệu (Validate)
        if (amount <= 0) {
            throw new IllegalArgumentException("Số tiền không hợp lệ");
        }

        Course course = courseRepository.getCourseById(courseId);
        User user = userRepository.getUserById(userId);

        if (course == null || user == null) {
            throw new IllegalArgumentException("Khóa học hoặc Người dùng không tồn tại");
        }

        // 2. Khởi tạo đối tượng
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setCourseId(course);
        transaction.setUserId(user);
        
        // Sử dụng Enum đã định nghĩa
        transaction.setPaymentMethod(paymentMethod.getPaymentMethod());
        transaction.setStatus(TransactionStatus.PENDING.getStauts()); 

        // 1. Lấy chuỗi thời gian chuẩn YYMMDD (Ví dụ: 260604)
        String timeStamp = new java.text.SimpleDateFormat("yyMMdd").format(new java.util.Date());

        // 2. Sinh chuỗi ngẫu nhiên 8 ký tự (Bỏ dấu gạch ngang của UUID cho đẹp)
        String randomId = java.util.UUID.randomUUID().toString().replace("-", "").substring(0, 8);

        // 3. Ghép lại thành mã giao dịch chuẩn
        String transRef = timeStamp + "_" + randomId; 
        transaction.setTransactionRef(transRef);

        // 3. Đẩy xuống Repository để lưu
        transactionRepository.addTransaction(transaction);
        transaction.setTransactionRef(transRef);

        
        return transaction;
    }

    @Override
    @Transactional
    public boolean markTransactionAsSuccess(String transactionRef) {
        boolean isUpdated = transactionRepository.updateTransactionStatus(transactionRef, TransactionStatus.SUCCESS.getStauts());

        if (isUpdated) {
            Transaction tx = transactionRepository.getTransactionByRef(transactionRef);
            if (tx != null) {
                courseStudentService.enrollStudent(tx.getCourseId().getId(), tx.getUserId().getId());
            }
        }
        return isUpdated;
    }

    @Override
    public boolean markTransactionAsFailed(String transactionRef) {
         // Cập nhật trạng thái thành FAILED
        return transactionRepository.updateTransactionStatus(transactionRef, TransactionStatus.FAILED.getStauts());
    }

    @Override
    public Transaction getTransactionByRef(String transactionRef) {
        return transactionRepository.getTransactionByRef(transactionRef);
    }
}
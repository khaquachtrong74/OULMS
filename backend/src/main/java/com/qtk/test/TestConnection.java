package com.qtk.test;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.hibernate.SessionFactory;
import com.qtk.configs.HibernateConfigs;

public class TestConnection {
    public static void main(String[] args) {
        System.out.println("⏳ Đang gọi Spring Container để thử kết nối...");
        
        try {
            // Nạp file cấu hình Hibernate của bạn vào
            AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(HibernateConfigs.class);
            
            // Lấy SessionFactory ra. Nếu nó lấy được tức là kết nối MySQL thành công.
            SessionFactory sessionFactory = context.getBean(SessionFactory.class);
            
            if (sessionFactory != null) {
                System.out.println("✅ THÀNH CÔNG: Đã kết nối được với database lmsdb!");
            }
            
            context.close();
        } catch (Exception e) {
            System.err.println("❌ THẤT BẠI: Không thể kết nối Database!");
            // In ra nguyên nhân gốc rễ (Root cause)
            e.printStackTrace(); 
        }
    }
}
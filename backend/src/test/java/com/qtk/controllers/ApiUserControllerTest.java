///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
// */
//package com.qtk.controllers;
//import com.qtk.pojo.User;
//import com.qtk.services.UserService;
//import jakarta.persistence.EntityNotFoundException;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.http.MediaType;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
///**
// *
// * @author lossrunner
// */
//@ExtendWith(MockitoExtension.class)
//public class ApiUserControllerTest {
//
//    private MockMvc mockMvc;
//
//    @Mock
//    private UserService userService; // Làm giả tầng Service để không chạm vào Database
//
//    @InjectMocks
//    private ApiUserController apiUserController; // Bơm Service giả vào Controller
//
//    @BeforeEach
//    public void setUp() {
//        // Khởi tạo môi trường Web giả lập chỉ chứa duy nhất ApiUserController
//        mockMvc = MockMvcBuilders.standaloneSetup(apiUserController).build();
//    }
//
//    @Test
//    public void testLogin_Success() throws Exception {
//        String testEmail = "kha@gmail.com"; 
//        String testPassword = "123456";
//        String requestBody = "{\"email\":\"" + testEmail + "\", \"password\":\"" + testPassword + "\"}";
//        
//        User mockUser = new User();
//        mockUser.setEmail(testEmail);
//        mockUser.setRole("ADMIN");
//        // Thiết lập kịch bản
//        Mockito.when(userService.authenticate(testEmail, testPassword)).thenReturn(true);
//        Mockito.when(userService.getUserByUserEmail(testEmail)).thenReturn(mockUser);
//        // (Act & Assert)
//        mockMvc.perform(post("/api/login")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(requestBody))
//                .andExpect(status().isOk()) // Kỳ vọng HTTP Status 200
//                .andExpect(jsonPath("$.token").exists()) // Kỳ vọng trong chuỗi JSON trả về phải có key "token"
//                .andExpect(jsonPath("$.token").isString());
//    }
//    
//    @Test
//    public void testDeleteUser_Success() throws Exception {
//        int targetUserId = 1;
//
//        // Vì hàm userService.deleteUser(id) có kiểu trả về là 'void', 
//        // ta không thể dùng Mockito.when(...).thenReturn(...)
//        // Thay vào đó, ta ra lệnh cho Mock Object: "Khi nhận được id = 1, đừng làm gì cả (bỏ qua I/O DB)"
//        Mockito.doNothing().when(userService).deleteUser(targetUserId);
//
//     
//        mockMvc.perform(delete("/api/secure/users/{id}", targetUserId)
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isNoContent()); 
//
//        // 4. Verify Behavior (Xác minh hành vi - Bắt buộc đối với hàm void)
//        // Bản chất: Đảm bảo rằng Controller thực sự đã đẩy luồng gọi xuống Service đúng 1 lần với đúng ID đó, 
//        // chứ không phải trả về 204 một cách ngẫu nhiên hay nuốt lệnh.
//        Mockito.verify(userService, Mockito.times(1)).deleteUser(targetUserId);
//    }
//    
//    @Test
//    public void testCreateUser_EmptyEmail_ShouldReturnBadRequest() throws Exception {
//        // 1. Arrange: Đầu vào nằm ngay tại điểm biên (biến email là chuỗi rỗng)
//        String invalidRequestBody = "{\"email\":\"\", \"password\":\"123456\"}";
//
//        // 2. Act & 3. Assert
//        mockMvc.perform(post("/api/users")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(invalidRequestBody))
//                // Kỳ vọng: Luồng code chưa kịp chạm vào UserService
//                // Đã bị Spring Validate chặn lại ở cửa Controller và quăng mã 400 Bad Request
//                .andExpect(status().isBadRequest());
//
//        // 4. Verify: Khẳng định UserService.addUser() bị cô lập, KHÔNG BAO GIỜ được gọi tới (times = 0)
//        Mockito.verify(userService, Mockito.times(0)).addUser(Mockito.anyMap(), Mockito.any());
//    }
//    @Test
//    public void testDeleteUser_NotFound() throws Exception {
//        // 1. Arrange: Gài bẫy cho Mock Object
//        int invalidUserId = 999;
//
//        // Ra lệnh: Khi service nhận ID 999, MOCK BẮT BUỘC phải ném ra lỗi EntityNotFoundException
//        Mockito.doThrow(new EntityNotFoundException("User không tồn tại"))
//               .when(userService).deleteUser(invalidUserId);
//
//        // 2. Act & 3. Assert
//        mockMvc.perform(delete("/api/secure/users/{id}", invalidUserId)
//                .contentType(MediaType.APPLICATION_JSON))
//                // Kỳ vọng hệ thống phải bắt được lỗi qua @RestControllerAdvice
//                // và ép nó thành HTTP Status 404 (Not Found)
//                .andExpect(status().isNotFound())
//                // Kỳ vọng thông báo lỗi trả về cho client không được chứa mã nguồn nội bộ
//                .andExpect(jsonPath("$.error").exists()); 
//
//        // 4. Verify: Đảm bảo service thực sự bị gọi và đã ném lỗi
//        Mockito.verify(userService, Mockito.times(1)).deleteUser(invalidUserId);
//    }
//    
//    @Test
//    // Giả lập trực tiếp một SecurityContext có quyền STUDENT thay vì phải chạy qua JwtFilter
//    @WithMockUser(username = "student_kha",roles = {"STUDENT"} , authorities = {"STUDENT"}) 
//    public void testDeleteUser_Forbidden_WrongRole() throws Exception {
//        int targetUserId = 1;
//
//        // Act & Assert
//        mockMvc.perform(delete("/api/secure/users/{id}", targetUserId)
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isForbidden());
//
//        // Khẳng định tầng nghiệp vụ bên dưới hoàn toàn an toàn
//        Mockito.verify(userService, Mockito.never()).deleteUser(Mockito.anyInt());
//    }
//}
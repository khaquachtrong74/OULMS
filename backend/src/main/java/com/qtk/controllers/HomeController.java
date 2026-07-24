/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.qtk.controllers;

import com.qtk.services.CourseStudentService;
import com.qtk.services.DocumentService;
import com.qtk.services.LevelService;
import com.qtk.services.SubjectService;
import com.qtk.services.TopicService;
import com.qtk.services.TransactionService;
import com.qtk.services.UserService;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author ngodo
 */
@Controller
@ControllerAdvice
public class HomeController {
    
    @Autowired
    private SubjectService subService;
    
    @Autowired
    private LevelService levelService;
    
    @Autowired
    private TopicService topicService;
    
    @Autowired
    private TransactionService transactionService;

    @Autowired
    private CourseStudentService courseStudentService;
    
    @Autowired
    private DocumentService documentService;
    @Autowired
    private UserService userService;
    
    @ModelAttribute
    public void subjectView(Model model) {
        model.addAttribute("subjects", subService.getSubject(null));
    }
    
    @ModelAttribute
    public void topicView(Model model) {
        model.addAttribute("topics", topicService.getTopic(null));
    }
    
    @ModelAttribute
    public void levelView(Model model) {
        model.addAttribute("levels", levelService.getLevel(null));
    }
    
    @RequestMapping("/")
    public String index(Model model){
        model.addAttribute("totalRevenue", this.transactionService.getTotalRevenue());
        model.addAttribute("totalSuccessTransactions", this.transactionService.countSuccessTransactions());
        model.addAttribute("totalEnrolledStudents", this.courseStudentService.countTotalEnrollments());
        model.addAttribute("totalPendingTransactions", this.transactionService.countPendingTransactions());
        Map<String, String> docParams = new HashMap<>();
        docParams.put("status", "PENDING"); 
        docParams.put("page", "1");
        model.addAttribute("pendingDocs", this.documentService.getAdminDocuments(docParams));
        model.addAttribute("newUsers", this.userService.getNewUsers(5));
        model.addAttribute("totalUsers", this.userService.countTotalUsers());
        model.addAttribute("totalPendingDocs", this.documentService.countPendingDocuments());
        return "index";
    }
}

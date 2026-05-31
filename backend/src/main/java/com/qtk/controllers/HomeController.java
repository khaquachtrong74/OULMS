/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.qtk.controllers;

import com.qtk.services.LevelService;
import com.qtk.services.SubjectService;
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
    
    @ModelAttribute
    public void subjectView(Model model) {
        model.addAttribute("subjects", subService.getSubject(null));
    }
    
    @ModelAttribute
    public void levelView(Model model) {
        model.addAttribute("levels", levelService.getLevel(null));
    }
    
    @RequestMapping("/")
    public String index(){
        return "index";
    }
}

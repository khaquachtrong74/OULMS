/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.qtk.controllers;

import com.qtk.pojo.Subject;
import com.qtk.services.SubjectService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author ngodo
 */
@Controller
@RequestMapping("/admin")
public class SubjectController {
    
    @Autowired
    private SubjectService subService;
    
    @RequestMapping("/subject")
    public String subjectView(Model model, @RequestParam Map<String, String> params) {
        model.addAttribute("subjects", subService.getSubject(params));
        return "subjectView";
    }
    
    @GetMapping("/subject/create")
    public String createView(Model model) {
        model.addAttribute("product", new Subject());
        return "subject";
    }
    
    @PostMapping("/subject/create")
    public String create( @ModelAttribute(value = "product") Subject s) {
        this.subService.addOrUpdateSubject(s);
        return "redirect:/subject";
    }
    
    @GetMapping("/subject/create/{subjectId}")
    public String updateView(Model model, @PathVariable(value = "subjectId") int id) {
        model.addAttribute("product", this.subService.getSubjectById(id));
        return "subject";
    }
}



/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.qtk.controllers;

import com.qtk.pojo.Document;
import com.qtk.services.DocumentService;
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
public class DocumentController {
    
    @Autowired
    private DocumentService docService;
    
    @RequestMapping("/documents")
    public String documentView(Model model, @RequestParam Map<String, String> params) {
        model.addAttribute("documents", this.docService.getDocument(params));
        return "document";
    }
    
    @GetMapping("/document")
    public String createView(Model model) {
        model.addAttribute("document", new Document());
        return "documentCreate";
        
    }
    
    @PostMapping("/document")
    public String create(@ModelAttribute(value = "document") Document d) {
        this.docService.addOrUpdateDocument(d);
        return "redirect:/admin/documents";
    }
    
    @GetMapping("/documents/{documentId}")
    public String updateView(Model model, @PathVariable(value = "documentId") int id) {
        model.addAttribute("document", this.docService.getDocumentById(id));
        return "documentCreate";
    }
}

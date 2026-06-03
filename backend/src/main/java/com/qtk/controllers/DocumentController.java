/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.qtk.controllers;

import com.qtk.enums.DocumentStatus;
import com.qtk.pojo.Document;
import com.qtk.services.DocumentService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author ngodo
 */
@Controller
@RequestMapping("/admin")
public class DocumentController {
    
    @Autowired
    private DocumentService docService;
    
    @RequestMapping("/documents/active")
    public String documentActiveView(Model model, @RequestParam Map<String, String> params) {
        params.put("status", "ACTIVE");
        model.addAttribute("documents", this.docService.getDocuments(params));
        model.addAttribute("status", "ACTIVE");
        return "document";
    }
    
    @RequestMapping("/documents/pending")
    public String documentPendingView(Model model, @RequestParam Map<String, String> params) {
        params.put("status", "PENDING");
        model.addAttribute("documents", this.docService.getDocuments(params));
        model.addAttribute("status", "PENDING");
        return "document";
    }
    
    @RequestMapping("/documents/rejected")
    public String documentRejectedView(Model model, @RequestParam Map<String, String> params) {
        params.put("status", "REJECTED");
        model.addAttribute("documents", this.docService.getDocuments(params));
        model.addAttribute("status", "REJECTED");
        return "document";
    }
    
    @RequestMapping("/documents/hidden")
    public String documentHiddenView(Model model, @RequestParam Map<String, String> params) {
        params.put("status", "HIDDEN");
        model.addAttribute("documents", this.docService.getDocuments(params));
        model.addAttribute("status", "HIDDEN");
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
        return "redirect:/admin/documents/pending";
    }
    
    @GetMapping("/documents/{documentId}")
    public String updateView(Model model, @PathVariable(value = "documentId") int id) {
        model.addAttribute("document", this.docService.getDocumentById(id));
        return "documentCreate";
    }
      
}

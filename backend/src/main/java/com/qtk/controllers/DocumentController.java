/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.qtk.controllers;

import com.qtk.enums.DocumentStatus;
import com.qtk.pojo.Document;
import com.qtk.services.DocumentService;
import com.qtk.services.LevelService;
import com.qtk.services.TopicService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
    
    @Autowired
    private TopicService topicService;
    
    @Autowired
    private LevelService levelService;
    
    @RequestMapping("/documents/active")
    public String documentActiveView(Model model, @RequestParam Map<String, String> params) {
        params.put("status", "ACTIVE");

        int currentPage = Integer.parseInt(params.getOrDefault("page", "1"));

        int pageSize = this.docService.getDocumentPageSize();

        long totalDocuments = this.docService.countDocuments(params);

        int totalPages = (int) Math.ceil((double) totalDocuments / pageSize);
        
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("kw", params.get("kw"));

        model.addAttribute("documents", this.docService.getAdminDocuments(params));
        model.addAttribute("status", "ACTIVE");        
        return "document";
    }
    
    @RequestMapping("/documents/pending")
    public String documentPendingView(Model model, @RequestParam Map<String, String> params) {
        params.put("status", "PENDING");
        
        int currentPage = Integer.parseInt(params.getOrDefault("page", "1"));

        int pageSize = this.docService.getDocumentPageSize();

        long totalDocuments = this.docService.countDocuments(params);

        int totalPages = (int) Math.ceil((double) totalDocuments / pageSize);
        
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
//        model.addAttribute("kw", params.getOrDefault("kw", ""));
        
        model.addAttribute("documents", this.docService.getAdminDocuments(params));
        model.addAttribute("status", "PENDING");
        return "document";
    }
    
    @RequestMapping("/documents/rejected")
    public String documentRejectedView(Model model, @RequestParam Map<String, String> params) {
        params.put("status", "REJECTED");
        
        int currentPage = Integer.parseInt(params.getOrDefault("page", "1"));

        int pageSize = this.docService.getDocumentPageSize();

        long totalDocuments = this.docService.countDocuments(params);

        int totalPages = (int) Math.ceil((double) totalDocuments / pageSize);
        
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
//        model.addAttribute("kw", params.getOrDefault("kw", ""));
        
        model.addAttribute("documents", this.docService.getAdminDocuments(params));
        model.addAttribute("status", "REJECTED");
        return "document";
    }
    
    @RequestMapping("/documents/hidden")
    public String documentHiddenView(Model model, @RequestParam Map<String, String> params) {
        params.put("status", "HIDDEN");
        
        int currentPage = Integer.parseInt(params.getOrDefault("page", "1"));

        int pageSize = this.docService.getDocumentPageSize();

        long totalDocuments = this.docService.countDocuments(params);

        int totalPages = (int) Math.ceil((double) totalDocuments / pageSize);
        
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
//        model.addAttribute("kw", params.getOrDefault("kw", ""));
        
        model.addAttribute("documents", this.docService.getAdminDocuments(params));
        model.addAttribute("status", "HIDDEN");
        return "document";
    }
    
    @GetMapping("/document")
    public String createView(Model model) {
        model.addAttribute("document", new Document());
        return "documentCreate";

    }
    
    @PostMapping("/document")
    public String create(@ModelAttribute("document") Document d,
                         @RequestParam("topicId") int topicId,
                         @RequestParam("levelId") int levelId) {

        d.setTopicId(this.topicService.getTopicById(topicId));
        d.setLevelId(this.levelService.getLevelById(levelId));

        this.docService.addOrUpdateDocument(d);

        return "redirect:/admin/documents/active";
    }
    
    @GetMapping("/documents/{documentId}")
    public String updateView(Model model, @PathVariable(value = "documentId") int id) {
        model.addAttribute("document", this.docService.getDocumentById(id));
        return "documentCreate";
    }
    
    @PostMapping(
            value = "/documents/{documentId}/{status}",
            produces = "text/plain;charset=UTF-8"
    )
    @ResponseBody
    public ResponseEntity<String> updateStatus(
            @PathVariable("documentId") int id,
            @PathVariable("status") DocumentStatus status) {

        this.docService.updateDocumentStatus(id, status);

        return ResponseEntity.ok("Cập nhật trạng thái thành công");
    }

}

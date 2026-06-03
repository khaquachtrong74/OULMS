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
    
    @GetMapping("/documents/view/{id}")
    public String handleDocumentClick(@PathVariable int id, Model model) {
        Document doc = docService.getDocumentById(id);
        model.addAttribute("document", doc);

        return switch (doc.getStatus()) {
            case PENDING -> "documentReview";
            case ACTIVE, REJECTED -> "documentDetail";
            default -> "documentCreate";
        }; 
    }   
    @PostMapping("/documents/{id}/{status}")
    @ResponseBody
    public ResponseEntity<String> quickUpdateStatus(
            @PathVariable("id") int id, 
            @PathVariable("status") String statusStr) {
        
        try {
            // 1. Ép kiểu String thành Enum. 
            // Nếu chữ truyền lên không khớp với Enum (VD: truyền "ABC"), nó sẽ văng ra IllegalArgumentException.
            DocumentStatus newStatus = DocumentStatus.valueOf(statusStr.toUpperCase());
            
            // 2. Gọi hàm từ Service của bạn. Vì hàm trả về void, nếu không văng lỗi (Exception) nghĩa là thành công.
            this.docService.updateDocumentStatus(id, newStatus);
            
            return ResponseEntity.ok("Cập nhật trạng thái thành công");

        } catch (IllegalArgumentException e) {
            // Lỗi do Client truyền sai tên Status
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body("Trạng thái không hợp lệ: " + statusStr);
        } catch (Exception e) {
            // Lỗi trong quá trình thao tác Database (VD: không tìm thấy ID tài liệu, mất kết nối DB)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Lỗi máy chủ khi cập nhật tài liệu");
        }
    }
}

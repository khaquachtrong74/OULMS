/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.qtk.services.impl;

import com.google.api.services.drive.model.File;
import com.qtk.dto.DocumentRequestDTO;
import com.qtk.dto.DocumentResponseDTO;
import com.qtk.enums.DocumentStatus;
import com.qtk.pojo.Document;
import com.qtk.repositories.DocumentRepository;
import com.qtk.repositories.LevelRepository;
import com.qtk.repositories.TopicRepository;
import com.qtk.repositories.UserRepository;
import com.qtk.services.DocumentService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.qtk.services.GoogleDriveService;

/**
 *
 * @author ngodo
 */
@Service
public class DocumentServiceImpl implements DocumentService {

    @Autowired
    private DocumentRepository docRepo;

    @Autowired
    private TopicRepository topicRepo;

    @Autowired
    private LevelRepository levelRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private GoogleDriveService googleDriveService;

    private DocumentResponseDTO toDTO(Document d) {
        DocumentResponseDTO dto = new DocumentResponseDTO();

        dto.setId(d.getId());
        dto.setTitle(d.getTitle());
        dto.setDescription(d.getDescription());
        dto.setFileUrl(d.getFileUrl());
        dto.setStatus(d.getStatus().name());

        dto.setTopicId(d.getTopicId().getId());
        dto.setTopicName(d.getTopicId().getName());

        dto.setLevelId(d.getLevelId().getId());
        dto.setLevelName(d.getLevelId().getName());
        
        dto.setUploadedByEmail(d.getUploadedBy().getEmail());
        return dto;
    }

    private void validateDocumentRequest(DocumentRequestDTO dto) {
        if (dto.getTitle() == null || dto.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Tên tài liệu không được để trống");
        }

        if (dto.getDescription() == null || dto.getDescription().trim().isEmpty()) {
            throw new IllegalArgumentException("Mô tả không được để trống");
        }

        if (dto.getTopicId() == null) {
            throw new IllegalArgumentException("Chủ đề không được để trống");
        }

        if (dto.getLevelId() == null) {
            throw new IllegalArgumentException("Cấp độ không được để trống");
        }

        if (dto.getFile() == null || dto.getFile().isEmpty()) {
            throw new IllegalArgumentException("File không được để trống");
        }

        long maxSize = 500L * 1024 * 1024;
        if (dto.getFile().getSize() > maxSize) {
            throw new IllegalArgumentException("File không được vượt quá 500MB");
        }

        String contentType = dto.getFile().getContentType();

        if (contentType == null
                || !(contentType.equals("application/pdf")
                || contentType.equals("video/mp4")
                || contentType.equals("image/jpeg")
                || contentType.equals("image/png")
                || contentType.equals("application/zip")
                || contentType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document")
                || contentType.equals("application/vnd.openxmlformats-officedocument.presentationml.presentation"))) {
            throw new IllegalArgumentException("Định dạng file không được hỗ trợ");
        }

        if (this.topicRepo.getTopicById(dto.getTopicId()) == null) {
            throw new IllegalArgumentException("Chủ đề không tồn tại");
        }

        if (this.levelRepo.getLevelById(dto.getLevelId()) == null) {
            throw new IllegalArgumentException("Cấp độ không tồn tại");
        }
    }

    @Override
    public List<Document> getAdminDocuments(Map<String, String> params) {
        return this.docRepo.getDocuments(params);
    }

    @Override
    public List<DocumentResponseDTO> getPublicDocument(Map<String, String> params) {
        return this.docRepo.getDocuments(params).stream().map(this::toDTO).toList();
    }

    @Override
    public List<DocumentResponseDTO> getDocumentsByUserId(int userId, Map<String, String> params) {
        return this.docRepo.getDocumentsByUserId(userId, params).stream().map(this::toDTO).toList();
    }

    @Override
    public DocumentResponseDTO getDocumentDetail(int id) {
        return this.toDTO(this.docRepo.getDocumentById(id));
    }

    @Override
    public void addOrUpdateDocument(Document d) {
        d.setUploadedBy(this.userRepo.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName()));

        if (d.getFile() != null && !d.getFile().isEmpty()) {
            File driveFile = googleDriveService.uploadFile(d.getFile());
            d.setFileUrl(driveFile.getWebViewLink());
            d.setDriveFileId(driveFile.getId());
            d.setFileType(d.getFile().getContentType());
            d.setFileSize(d.getFile().getSize());
        }
        this.docRepo.addOrUpdateDocument(d);
    }

    @Override
    public void createDocument(DocumentRequestDTO dto) {
        this.validateDocumentRequest(dto);
        
        Document d = new Document();
        d.setTitle(dto.getTitle());
        d.setDescription(dto.getDescription());
        d.setTopicId(this.topicRepo.getTopicById(dto.getTopicId()));
        d.setLevelId(this.levelRepo.getLevelById(dto.getLevelId()));
        d.setUploadedBy(this.userRepo.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName()));
        if (dto.getFile() != null && !dto.getFile().isEmpty()) {
            File driveFile = googleDriveService.uploadFile(dto.getFile());
            d.setFileUrl(driveFile.getWebViewLink());
            d.setDriveFileId(driveFile.getId());
            d.setFileType(dto.getFile().getContentType());
            d.setFileSize(dto.getFile().getSize());
        }
        this.docRepo.addOrUpdateDocument(d);
    }

    @Override
    public Document getDocumentById(int id) {
        return this.docRepo.getDocumentById(id);
    }

    @Override
    public void updateDocumentStatus(int id, DocumentStatus status) {
        this.docRepo.updateDocumentStatus(id, status);
    }
    
     @Override
    public void updateDocument(int documentId, DocumentRequestDTO dto) {
        Document doc = this.docRepo.getDocumentById(documentId);

        if (doc == null)
            throw new IllegalArgumentException("Không tìm thấy tài liệu");

        if (dto.getTitle() != null)
            doc.setTitle(dto.getTitle());

        if (dto.getDescription() != null)
            doc.setDescription(dto.getDescription());

        if (dto.getTopicId() != null)
            doc.setTopicId(this.topicRepo.getTopicById(dto.getTopicId()));

        if (dto.getLevelId() != null)
            doc.setLevelId(this.levelRepo.getLevelById(dto.getLevelId()));

        this.docRepo.addOrUpdateDocument(doc);
    }

    @Override
    public void deleteDocument(int id) {
        this.docRepo.deleteDocument(id);
    }
    
    
}

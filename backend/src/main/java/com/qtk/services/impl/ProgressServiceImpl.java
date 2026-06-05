/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.qtk.services.impl;

import com.qtk.dto.ProgressResponseDTO;
import com.qtk.enums.ProgressStatus;
import com.qtk.pojo.Document;
import com.qtk.pojo.Progress;
import com.qtk.pojo.User;
import com.qtk.repositories.DocumentRepository;
import com.qtk.repositories.ProgressRepository;
import com.qtk.repositories.UserRepository;
import com.qtk.services.ProgressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 *
 * @author ngodo
 */
@Service
public class ProgressServiceImpl implements ProgressService{
    
    @Autowired
    private ProgressRepository progressRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private DocumentRepository documentRepo;

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        return this.userRepo.getUserByUserEmail(email);
    }

    private ProgressResponseDTO toDTO(Progress p) {
        ProgressResponseDTO dto = new ProgressResponseDTO();

        dto.setId(p.getId());
        dto.setDocumentId(p.getDocumentId().getId());
        dto.setStatus(p.getStatus().name());
        dto.setTimeSpent(p.getTimeSpent());
        dto.setUpdatedAt(p.getUpdatedAt());

        return dto;
    }

    @Override
    public ProgressResponseDTO startProgress(int documentId) {
        User user = getCurrentUser();

        Document document = this.documentRepo.getDocumentById(documentId);

        if (document == null) {
            throw new IllegalArgumentException("Tài liệu không tồn tại");
        }

        Progress progress = this.progressRepo.getProgressByUserAndDocument(user.getId(), documentId);

        if (progress == null) {
            progress = new Progress();
            progress.setUserId(user);
            progress.setDocumentId(document);
            progress.setStatus(ProgressStatus.IN_PROGRESS);
            progress.setTimeSpent(0);
        } else if (progress.getStatus() == ProgressStatus.NOT_STARTED) {
            progress.setStatus(ProgressStatus.IN_PROGRESS);
        }

        this.progressRepo.addOrUpdateProgress(progress);

        return toDTO(progress);
    }

    @Override
    public ProgressResponseDTO updateTimeSpent(int documentId, int seconds) {
        if (seconds <= 0 || seconds > 3600) {
            throw new IllegalArgumentException("Thời gian học không hợp lệ");
        }

        User user = getCurrentUser();

        Progress progress = this.progressRepo.getProgressByUserAndDocument(user.getId(), documentId);

        if (progress == null) {
            throw new IllegalArgumentException("Bạn chưa bắt đầu học tài liệu này");
        }

        if (progress.getStatus() != ProgressStatus.COMPLETED) {
            progress.setStatus(ProgressStatus.IN_PROGRESS);
        }

        progress.setTimeSpent(progress.getTimeSpent() + seconds);

        this.progressRepo.addOrUpdateProgress(progress);

        return toDTO(progress);
    }

    @Override
    public ProgressResponseDTO completeProgress(int documentId) {
        User user = getCurrentUser();

        Progress progress =
                this.progressRepo.getProgressByUserAndDocument(user.getId(), documentId);

        if (progress == null) {
            throw new IllegalArgumentException("Bạn chưa bắt đầu học tài liệu này");
        }

        progress.setStatus(ProgressStatus.COMPLETED);

        this.progressRepo.addOrUpdateProgress(progress);

        return toDTO(progress);
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.qtk.services;

import com.qtk.dto.ProgressResponseDTO;

/**
 *
 * @author ngodo
 */
public interface ProgressService  {
    ProgressResponseDTO startProgress(int documentId);
    ProgressResponseDTO updateTimeSpent(int documentId, int seconds);
    ProgressResponseDTO completeProgress(int documentId);
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.qtk.repositories;

import com.qtk.pojo.Progress;

/**
 *
 * @author ngodo
 */
public interface ProgressRepository {
    Progress getProgressByUserAndDocument(int userId, int documentId);
    void addOrUpdateProgress(Progress p);
}

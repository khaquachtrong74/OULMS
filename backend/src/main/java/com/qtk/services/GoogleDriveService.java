/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.qtk.services;

import com.google.api.services.drive.model.File;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author ngodo
 */
public interface GoogleDriveService {
    public File uploadFile(MultipartFile multipartFile);
}

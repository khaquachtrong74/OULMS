/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.qtk.services.impl;

import com.google.api.client.http.InputStreamContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.qtk.services.GoogleDriveService;

/**
 *
 * @author ngodo
 */
@Service
public class GoogleDriveServiceImpl  implements GoogleDriveService{
    
    private static final String VIDEO_FOLDER_ID = "1VwUwnI_M5D_tTcX2XEp_N1dD5GX8MAp0";
    
    @Autowired
    private Drive driveService;

    @Override
    public File uploadFile(MultipartFile multipartFile) {
        try {
            File fileMetadata = new com.google.api.services.drive.model.File();

            fileMetadata.setName(multipartFile.getOriginalFilename());
            fileMetadata.setParents(List.of(VIDEO_FOLDER_ID));

            InputStreamContent mediaContent = new InputStreamContent(
                    multipartFile.getContentType(),
                    multipartFile.getInputStream()
            );

            return driveService.files()
                    .create(fileMetadata, mediaContent)
                    .setFields("id, name, webViewLink, mimeType, size")
                    .execute();

        } catch (IOException ex) {
            throw new RuntimeException("Upload video lên Google Drive thất bại", ex);
        }
    }
    
}

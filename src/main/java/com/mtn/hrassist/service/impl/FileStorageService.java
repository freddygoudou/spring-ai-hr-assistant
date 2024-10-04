package com.mtn.hrassist.service.impl;

import com.mtn.hrassist.configs.ApplicationProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;


@Slf4j
@Service
@Transactional(rollbackFor = Throwable.class)
@RequiredArgsConstructor
public class FileStorageService {


    private final ApplicationProperties applicationProperties;


    public String uploadFile(MultipartFile file) throws IOException {

        log.info("Uploading file {}", file.getOriginalFilename());

        String trackingId = UUID.randomUUID().toString();

        String fileName = file.getOriginalFilename();

        String filePath = applicationProperties.getFileUploadDir().concat(updateFilenameWithTrackingId(fileName, trackingId));

        file.transferTo(new File(filePath));

        log.info("File with name {} uploaded successfully", file.getOriginalFilename());

        return filePath;
    }




    public String updateFilenameWithTrackingId(String originalFilename, String trackingId) {
        if (originalFilename == null || trackingId == null) {
            throw new IllegalArgumentException("Original filename or tracking ID cannot be null");
        }

        // Find the last dot to extract the file extension
        int dotIndex = originalFilename.lastIndexOf('.');

        // If the file has an extension
        if (dotIndex > 0) {
            String extension = originalFilename.substring(dotIndex); // includes the dot
            return trackingId + extension;
        } else {
            // If there is no extension, simply append the tracking ID
            return originalFilename + "_" + trackingId;
        }
    }

}

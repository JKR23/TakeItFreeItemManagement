package com.takeitfree.itemmanagement.services;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UploadCareService {
    public String uploadFile(MultipartFile file) throws IOException;
}

package com.takeitfree.itemmanagement.services;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface AzureBlobStorageService {
    public String uploadFile(MultipartFile file)throws IOException;
}

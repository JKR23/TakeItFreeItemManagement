package com.takeitfree.itemmanagement.services.Impl;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobContainerClientBuilder;
import com.takeitfree.itemmanagement.services.AzureBlobStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

/**
 * Implementation of AzureBlobStorageService for uploading files to Azure Blob Storage.
 */
@Service
public class AzureBlobStorageServiceImpl implements AzureBlobStorageService {

    @Value("${azure.storage.connection-string}")
    private String connectionString;

    @Value("${azure.storage.container-name}")
    private String containerName;

    /**
     * Uploads a file to Azure Blob Storage.
     *
     * @param file the file to upload
     * @return the URL of the uploaded file
     * @throws IOException if an error occurs during file upload
     */
    @Override
    public String uploadFile(MultipartFile file) throws IOException {

        BlobContainerClient containerClient = new BlobContainerClientBuilder()
                .connectionString(connectionString)
                .containerName(containerName)
                .buildClient();

        BlobClient blobClient = containerClient.getBlobClient(file.getOriginalFilename());

        try (InputStream inputStream = file.getInputStream()) {
            blobClient.upload(inputStream, file.getSize(), true);
        }

        return blobClient.getBlobUrl();
    }

}

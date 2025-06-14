package com.takeitfree.itemmanagement.services.Impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.takeitfree.itemmanagement.services.UploadCareService;
import com.takeitfree.itemmanagement.utils.MultipartInputStreamFileResource;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;

@Service
@RequiredArgsConstructor
public class UploadCareServiceImpl implements UploadCareService {
    @Value("${uploadcare.public_key}")
    private String publicKey;

    //for reaching an external API
    private final RestTemplate restTemplate;

    @Override
    public String uploadFile(MultipartFile file) throws IOException {

        //build a link from URI
        URI uri = UriComponentsBuilder
                .fromUriString("https://upload.uploadcare.com/base/")
                .build()
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("UPLOADCARE_PUB_KEY", publicKey);
        body.add("UPLOADCARE_STORE", "1");
        body.add("file", new MultipartInputStreamFileResource(file));

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(uri, requestEntity, String.class);


        System.out.println("Status code: " + response.getStatusCode());
        System.out.println("Headers: " + response.getHeaders());
        System.out.println("Body: " + response.getBody());

        if (response.getStatusCode().is2xxSuccessful()) {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode json = mapper.readTree(response.getBody());
            return json.get("file").asText();
        } else {
            throw new RuntimeException("Upload failed with status: " + response.getStatusCode());
        }
    }
}

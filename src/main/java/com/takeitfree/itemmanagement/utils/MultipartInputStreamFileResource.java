package com.takeitfree.itemmanagement.utils;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Getter
public class MultipartInputStreamFileResource extends InputStream {

    private final String filename;

    public MultipartInputStreamFileResource(MultipartFile multipartFile) throws IOException {
        super();
        this.filename = multipartFile.getOriginalFilename();
    }

    public long contentLength() throws IOException {
        return -1;
    }

    @Override
    public int read() throws IOException {
        return 0;
    }
}

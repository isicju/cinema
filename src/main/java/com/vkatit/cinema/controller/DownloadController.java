package com.vkatit.cinema.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.FileNotFoundException;
import java.io.IOException;

@Controller
@RequestMapping("/api/v1/pdf")
public class DownloadController {

    @Value("${ticket.path}")
    private String TICKET_PATH;

    private final ResourceLoader resourceLoader;

    public DownloadController(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<Resource> download(@PathVariable String fileName) throws IOException {
        return sendResponse(loadResource(fileName), setHttpHeaders(fileName));
    }

    private Resource loadResource(String fileName) throws FileNotFoundException {
        Resource resource = resourceLoader.getResource("file:" + TICKET_PATH + fileName);
        if (!resource.exists() || !resource.isReadable()) {
            throw new FileNotFoundException("File not found or not readable");
        }
        return resource;
    }

    private HttpHeaders setHttpHeaders(String fileName) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
        headers.setContentType(MediaType.parseMediaType("application/octet-stream"));
        return headers;
    }

    private ResponseEntity<Resource> sendResponse(Resource resource, HttpHeaders httpHeaders) throws IOException {
        return ResponseEntity.ok()
                .headers(httpHeaders)
                .contentLength(resource.contentLength())
                .body(resource);
    }

}
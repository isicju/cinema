package com.vkatit.cinema.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@RequestMapping("/api/v1/pdf")
public class DownloadController {

    @Value("${ticket.path}")
    private String TICKET_PATH;

    @GetMapping("/download/{fileName}")
    public ResponseEntity<?> download(@PathVariable String fileName) {
        Path filePath = Paths.get(TICKET_PATH, fileName);
        if (Files.exists(filePath) && Files.isRegularFile(filePath)) {
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
            headers.setContentType(MediaType.parseMediaType("application/octet-stream"));
            try {
                Resource resource = new FileSystemResource(filePath.toFile());
                return ResponseEntity.ok()
                        .headers(headers)
                        .contentLength(resource.contentLength())
                        .body(resource);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("File download error");
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}

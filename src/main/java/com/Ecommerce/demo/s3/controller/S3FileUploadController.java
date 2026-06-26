package com.Ecommerce.demo.s3.controller;

import com.Ecommerce.demo.s3.service.FileUploadService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/files")
public class S3FileUploadController {

    private final FileUploadService fileUploadService;

    public S3FileUploadController(
            FileUploadService fileUploadService
    ) {
        this.fileUploadService = fileUploadService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(

            @RequestParam("file")
            MultipartFile file
    ) {

        String fileUrl =
                fileUploadService
                        .uploadFile(file);

        return ResponseEntity.ok(
                fileUrl
        );
    }
}
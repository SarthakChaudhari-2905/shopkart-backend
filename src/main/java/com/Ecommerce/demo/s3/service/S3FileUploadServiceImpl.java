package com.Ecommerce.demo.s3.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

@Service
public class S3FileUploadServiceImpl
        implements FileUploadService {

    private final S3Client s3Client;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    @Value("${aws.region}")
    private String region;

    public S3FileUploadServiceImpl(
            S3Client s3Client
    ) {
        this.s3Client = s3Client;
    }

    @Override
    public String uploadFile(
            MultipartFile file
    ) {

        try {

            String key = "products/" +
                    UUID.randomUUID() +
                    "-" +
                    file.getOriginalFilename();

            PutObjectRequest putObjectRequest =
                    PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(key)
                            .contentType(file.getContentType())
                            .build();

            s3Client.putObject(
                    putObjectRequest,
                    RequestBody.fromBytes(
                            file.getBytes()
                    )
            );

            return String.format(
                    "https://%s.s3.%s.amazonaws.com/%s",
                    bucketName,
                    region,
                    key
            );

        } catch (IOException e) {

            throw new RuntimeException(
                    "File Upload Failed",
                    e
            );
        }
    }
}
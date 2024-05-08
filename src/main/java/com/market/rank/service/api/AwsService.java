package com.market.rank.service.api;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.market.rank.config.api.AwsProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;
@Service
@RequiredArgsConstructor
public class AwsService {


    private  final AwsProperties awsProperties;
    private final AmazonS3 s3Client;


    public String uploadToAWS(MultipartFile file) {
        String type = "." + StringUtils.getFilenameExtension(file.getOriginalFilename());
        String key = "main/"+UUID.randomUUID()+type;
        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            PutObjectRequest request = new PutObjectRequest(awsProperties.getBucket(), key, file.getInputStream(), metadata);
            s3Client.putObject(request);
            return key;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }



    public String uploadToAWS(InputStream file, String originalFilename) {
        String type = "." + StringUtils.getFilenameExtension(originalFilename);
        String key = "main/" + UUID.randomUUID() + type;
        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType("image/jpeg");
            PutObjectRequest request = new PutObjectRequest(awsProperties.getBucket(), key, file, metadata);
            s3Client.putObject(request);
            return key;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void deleteToAWS(String file) {
        s3Client.deleteObject(new DeleteObjectRequest(awsProperties.getBucket(), file));
    }
}

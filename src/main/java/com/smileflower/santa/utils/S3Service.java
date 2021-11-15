package com.smileflower.santa.utils;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Component
public class S3Service {

    private final AmazonS3Client amazonS3Client;
    private final S3Component s3Component;

    public S3Service(AmazonS3Client amazonS3Client, S3Component s3Component) {
        this.amazonS3Client = amazonS3Client;
        this.s3Component = s3Component;
    }


    public void uploadFile(InputStream inputStream, ObjectMetadata objectMetadata, String fileName){
        amazonS3Client.putObject(new PutObjectRequest(s3Component.getBucket(),fileName,inputStream,
                objectMetadata).withCannedAcl(CannedAccessControlList.PublicRead));
    }

    public void deleteFile(String fileName){
        boolean isExistObject = amazonS3Client.doesObjectExist(s3Component.getBucket(), fileName);
        if (isExistObject == true) {
            amazonS3Client.deleteObject(s3Component.getBucket(), fileName);
        }
    }

    public String getFileUrl(String fileName){
        return String.valueOf(amazonS3Client.getUrl(s3Component.getBucket(),fileName));
    }
}
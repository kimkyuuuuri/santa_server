package com.smileflower.santa.flag.service;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.smileflower.santa.flag.model.GpsInfoRequest;
import com.smileflower.santa.flag.repository.FlagRepository;
import com.smileflower.santa.flag.model.UploadImageResponse;
import com.smileflower.santa.utils.S3Service;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
public class FlagsService {

    private final FlagRepository flagRepository;
    private final S3Service s3Service;

    public FlagsService(FlagRepository flagRepository, S3Service s3Service) {
        this.flagRepository = flagRepository;
        this.s3Service = s3Service;
    }


    public UploadImageResponse uploadImage(GpsInfoRequest gpsInfoRequest, MultipartFile file, int userIdx, Long mountainIdx) {

        boolean isDoubleVisited = flagRepository.findTodayFlagByIdx(userIdx)!=0;
        boolean isFlag = flagRepository.findIsFlagByLatAndLong(gpsInfoRequest.getLatitude(),gpsInfoRequest.getLongitude(),mountainIdx)==1;

        if(isFlag && !isDoubleVisited){
            String fileName = createFileName(file.getOriginalFilename());
            ObjectMetadata objectMetadata = new ObjectMetadata();

            objectMetadata.setContentType(file.getContentType());

            try (InputStream inputStream = file.getInputStream()) {
                s3Service.uploadFile(inputStream, objectMetadata, fileName);
                updateImageUrlByIdx(userIdx, mountainIdx, fileName, gpsInfoRequest.getAltitude());
            } catch (IOException e) {
                throw new IllegalArgumentException(String.format("파일 변환 중 에러가 발생하였습니다 (%s)", file.getOriginalFilename()));
            }
            return new UploadImageResponse(isFlag,isDoubleVisited,s3Service.getFileUrl(fileName));
        }
        else{
            return new UploadImageResponse(isFlag,isDoubleVisited,null);
        }

    }

    private int updateImageUrlByIdx(int userIdx, Long mountainIdx, String fileName, Double altitude){
        return flagRepository.updateImageUrlByIdx(userIdx,mountainIdx,fileName,altitude);
    }


    private String createFileName(String originalFileName){
        return
                UUID.randomUUID().toString().concat(getFileExtension(originalFileName));
    }

    private String getFileExtension(String fileName){
        try{
            return fileName.substring(fileName.lastIndexOf("."));
        }catch(StringIndexOutOfBoundsException e){
            throw new IllegalArgumentException(String.format("잘못된 형식의 파일 (%s) 입니다",fileName));
        }
    }
}

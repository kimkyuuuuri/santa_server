package com.smileflower.santa.profile.service;

import com.amazonaws.services.kms.model.NotFoundException;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.smileflower.santa.profile.model.domain.Picture;
import com.smileflower.santa.profile.model.domain.Profile;
import com.smileflower.santa.profile.model.dto.*;
import com.smileflower.santa.profile.repository.ProfileRepository;
import com.smileflower.santa.utils.S3Service;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final S3Service s3Service;

    public ProfileService(ProfileRepository profileRepository, S3Service s3Service) {
        this.profileRepository = profileRepository;
        this.s3Service = s3Service;
    }

    public UploadImageResponse deleteImage(int userIdx){
        //delete file
        Profile user = profileRepository.findByIdx(userIdx)
                .orElseThrow(() -> new NotFoundException("cannot find user"));
        s3Service.deleteFile(user.getUserImageUrl());

        deleteImageUrlByIdx(userIdx);

        return new UploadImageResponse(null);
    }

    public UploadImageResponse getUploadImage(int userIdx){
        //delete file
        Profile user = profileRepository.findByIdx(userIdx)
                .orElseThrow(() -> new NotFoundException("cannot find user"));

        if(user.getUserImageUrl()!=null) {
            return new UploadImageResponse(s3Service.getFileUrl(user.getUserImageUrl()));
        }
        else
            return new UploadImageResponse(null);
    }


    public UploadImageResponse uploadImage(MultipartFile file,int userIdx){

        //delete pre file
        Profile user = profileRepository.findByIdx(userIdx)
                .orElseThrow(() -> new NotFoundException("cannot find user"));
        if(user.getUserImageUrl()!=null){
            s3Service.deleteFile(user.getUserImageUrl());
        }
        String fileName = createFileName(file.getOriginalFilename());

        ObjectMetadata objectMetadata = new ObjectMetadata();

        objectMetadata.setContentType(file.getContentType());

        try(InputStream inputStream = file.getInputStream()){
            s3Service.uploadFile(inputStream,objectMetadata,fileName);
            updateImageUrlByIdx(userIdx,fileName);
        }catch(IOException e){
            throw new IllegalArgumentException(String.format("파일 변환 중 에러가 발생하였습니다 (%s)", file.getOriginalFilename()));
        }
        return new UploadImageResponse(s3Service.getFileUrl(fileName));
    }

    private int deleteImageUrlByEmail(String email){
        return profileRepository.deleteImageUrlByEmail(email);
    }

    private int deleteImageUrlByIdx(int userIdx){
        return profileRepository.deleteImageUrlByIdx(userIdx);
    }

    private int updateImageUrlByEmail(String email,String fileName){
        return profileRepository.updateImageUrlByEmail(email,fileName);
    }

    private int updateImageUrlByIdx(int userIdx,String fileName){
        return profileRepository.updateImageUrlByIdx(userIdx,fileName);
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

    public ProfileResponse findProfile(int userIdx) {
        List<ProfilePostsResponse> profilePostsResponses = new ArrayList<>();
        List<FlagResponse> flagsResponse = profileRepository.findFlagsByIdx(userIdx);
        List<Picture> pictures = profileRepository.findPicturesByIdx(userIdx);
        int flagsResponseCnt = flagsResponse.size();
        int level = 0;
        if (flagsResponseCnt<=2){
            level = flagsResponseCnt;
        }
        else{
            level = (flagsResponseCnt+2)/2;
        }
//        for(int i=0;i<pictures.size();i++){
//            picturesResponse.add(new PictureResponse(pictures.get(i).getPictureIdx(),pictures.get(i).getUserIdx(),pictures.get(i).getImageUrl(),pictures.get(i).getCreatedAt().now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
//        }
        for(int i=0;i<pictures.size();i++){
            profilePostsResponses.add(new ProfilePostsResponse(false,null,pictures.get(i).getPictureIdx(),pictures.get(i).getUserIdx(),0,null,null,pictures.get(i).getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),s3Service.getFileUrl(pictures.get(i).getImageUrl())));
        }
        for(int i=0;i<flagsResponse.size();i++){
            profilePostsResponses.add(new ProfilePostsResponse(true,flagsResponse.get(i).getFlagIdx(),null,flagsResponse.get(i).getUserIdx(),flagsResponse.get(i).getFlagCount(),flagsResponse.get(i).getMountainIdx(),flagsResponse.get(i).getName(),flagsResponse.get(i).getCreatedAt(),s3Service.getFileUrl(flagsResponse.get(i).getPictureUrl())));
        }
        Collections.sort(profilePostsResponses);

        ProfileResponse profileResponse = new ProfileResponse(userIdx,profileRepository.findNameByIdx(userIdx),level,flagsResponseCnt,flagsResponseCnt+pictures.size(),getUploadImage(userIdx).getFileUrl(),profilePostsResponses);

        return profileResponse;
    }

    public PostsResponse findPosts(int userIdx) {
        List<ProfilePostsResponse> profilePostsResponses = new ArrayList<>();
        List<FlagResponse> flagsResponse = profileRepository.findFlagsByIdx(userIdx);
        List<Picture> pictures = profileRepository.findPicturesByIdx(userIdx);

        for(int i=0;i<pictures.size();i++){
            profilePostsResponses.add(new ProfilePostsResponse(false,null,pictures.get(i).getPictureIdx(),pictures.get(i).getUserIdx(),0,null,null,pictures.get(i).getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),s3Service.getFileUrl(pictures.get(i).getImageUrl())));
        }
        for(int i=0;i<flagsResponse.size();i++){
            profilePostsResponses.add(new ProfilePostsResponse(true,flagsResponse.get(i).getFlagIdx(),null,flagsResponse.get(i).getUserIdx(),flagsResponse.get(i).getFlagCount(),flagsResponse.get(i).getMountainIdx(),flagsResponse.get(i).getName(),flagsResponse.get(i).getCreatedAt(),s3Service.getFileUrl(flagsResponse.get(i).getPictureUrl())));
        }
        Collections.sort(profilePostsResponses);

        return new PostsResponse(userIdx,profileRepository.findNameByIdx(userIdx),profilePostsResponses);

    }

    public List<FlagsForMapResponse> findFlagsForMap(int userIdx) {
        List<FlagsForMapResponse> flagsForMapResponses = profileRepository.findFlagsForMapByIdx(userIdx);
        for(int i=0;i<flagsForMapResponses.size();i++){
            if(flagsForMapResponses.get(i).getImageUrl()!=null)
                flagsForMapResponses.get(i).setImageUrl(s3Service.getFileUrl(flagsForMapResponses.get(i).getImageUrl()));
        }
        return flagsForMapResponses;
    }

    public DeleteFlagResponse deleteFlag(Long flagIdx) {
        return new DeleteFlagResponse(profileRepository.deleteFlagByIdx(flagIdx));

    }

    public ReportFlagResponse reportFlag(int userIdx,Long flagIdx) {
        profileRepository.report(flagIdx,userIdx);
        return new ReportFlagResponse(flagIdx,profileRepository.reportCountByIdx(flagIdx));
    }


    public DeletePictureResponse deletePicture(Long pictureIdx) {
        return new DeletePictureResponse(profileRepository.deletePictureByIdx(pictureIdx));
    }

    public CreatePictureResponse createPicture(int userIdx,MultipartFile file) {
        String fileName = createFileName(file.getOriginalFilename());

        ObjectMetadata objectMetadata = new ObjectMetadata();

        objectMetadata.setContentType(file.getContentType());

        try (InputStream inputStream = file.getInputStream()) {
            s3Service.uploadFile(inputStream, objectMetadata, fileName);

        } catch (IOException e) {
            throw new IllegalArgumentException(String.format("파일 변환 중 에러가 발생하였습니다 (%s)", file.getOriginalFilename()));
        }
        return new CreatePictureResponse(profileRepository.createPicture(userIdx, fileName));
    }

    public ResultResponse findResult(int userIdx) {
        int flagCount = profileRepository.findFlagCountByIdx(userIdx);
        int diffFlagCount = profileRepository.findDiffFlagCountByIdx(userIdx);
        int highCount = profileRepository.findHighSumByIdx(userIdx);
        return new ResultResponse(flagCount>0,flagCount>2,flagCount>6,flagCount>9,
                highCount>4999, highCount>9999,highCount>19999,diffFlagCount>99,
                diffFlagCount>2,diffFlagCount>6,diffFlagCount>9,(double)highCount/1000);
    }
}
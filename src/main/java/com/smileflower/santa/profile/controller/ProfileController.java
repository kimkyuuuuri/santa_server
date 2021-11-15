package com.smileflower.santa.profile.controller;

import com.smileflower.santa.apple.utils.AppleJwtUtils;
import com.smileflower.santa.exception.ApiResult;
import com.smileflower.santa.profile.model.dto.*;
import com.smileflower.santa.profile.service.ProfileService;
import com.smileflower.santa.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequestMapping("/api/profile")
@RestController
public class ProfileController {

    @Autowired
    AppleJwtUtils appleJwtUtils;
    @Autowired
    ProfileService profileService;
    @Autowired
    JwtService jwtService;

    @PatchMapping("/upload")
    public ApiResult<UploadImageResponse> uploadImage(@RequestPart(required = false) MultipartFile file){
        int userIdx=-1;
        if(jwtService.validateToken()){
            userIdx = jwtService.getUserIdxV2();
        }
        if(file != null){
            return ApiResult.OK(
                    profileService.uploadImage(file,userIdx)
            );
        }
        else {
            return ApiResult.OK(
                    profileService.deleteImage(userIdx)
            );
        }
    }
    @GetMapping("/upload")
    public ApiResult<UploadImageResponse> uploadImage(){
        int userIdx=-1;
        if(jwtService.validateToken()){
            userIdx = jwtService.getUserIdxV2();
        }
        return ApiResult.OK(
                profileService.getUploadImage(userIdx)
        );

    }


    @GetMapping("/{userIdx}")
    public ApiResult<ProfileResponse> profile(@PathVariable("userIdx") int userIdx) {
        jwtService.validateToken();
        return ApiResult.OK(
                profileService.findProfile(userIdx)
        );
    }

    @GetMapping("/{userIdx}/posts")
    public ApiResult<PostsResponse> flags(@PathVariable("userIdx") int userIdx) {
        jwtService.validateToken();
        return ApiResult.OK(
                profileService.findPosts(userIdx)
        );
    }

    @GetMapping("/{userIdx}/result")
    public ApiResult<ResultResponse> result(@PathVariable("userIdx") int userIdx) {
        jwtService.validateToken();
        return ApiResult.OK(
                profileService.findResult(userIdx)
        );
    }

    @GetMapping("/{userIdx}/flags/map")
    public ApiResult<List<FlagsForMapResponse>> flagsForMap(@PathVariable("userIdx") int userIdx) {

        jwtService.validateToken();
        return ApiResult.OK(
                profileService.findFlagsForMap(userIdx)
        );

    }

    @DeleteMapping("/{userIdx}/flags/{flagIdx}")
    public ApiResult<DeleteFlagResponse> deleteFlag(@PathVariable("flagIdx") Long flagIdx) {
        jwtService.validateToken();
        return ApiResult.OK(
                profileService.deleteFlag(flagIdx)
        );

    }

    @DeleteMapping("/{userIdx}/pictures/{pictureIdx}")
    public ApiResult<DeletePictureResponse> deletePicture(@PathVariable("pictureIdx") Long pictureIdx) {
        jwtService.validateToken();
        return ApiResult.OK(
                profileService.deletePicture(pictureIdx)
        );

    }

    @PostMapping("/picture")
    public ApiResult<CreatePictureResponse> createPicture(@RequestPart(required = false) MultipartFile file) {
        int userIdx=-1;
        if(jwtService.validateToken()){
            userIdx = jwtService.getUserIdxV2();
        }
        return ApiResult.OK(
                profileService.createPicture(userIdx,file)
        );

    }

    @PostMapping("/{userIdx}/flags/{flagIdx}/report")
    public ApiResult<ReportFlagResponse> reportFlag(@PathVariable("userIdx") int userIdx, @PathVariable("flagIdx") Long flagIdx) {
        jwtService.validateToken();
        return ApiResult.OK(
                profileService.reportFlag(userIdx,flagIdx)
        );

    }




}

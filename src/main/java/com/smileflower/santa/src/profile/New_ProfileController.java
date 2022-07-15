package com.smileflower.santa.src.profile;

import com.smileflower.santa.config.BaseException;
import com.smileflower.santa.config.BaseResponse;

import com.smileflower.santa.src.profile.model.*;
import com.smileflower.santa.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.smileflower.santa.config.BaseResponseStatus.EMPTY_JWT;

@RestController
@RequestMapping("/app/profiles")
public class New_ProfileController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final New_ProfileProvider newProfileProvider;
    @Autowired
    private final New_ProfileService newProfileService;
    @Autowired
    private final JwtService jwtService;


    public New_ProfileController(New_ProfileProvider newProfileProvider, New_ProfileService newProfileService, JwtService jwtService) {
        this.newProfileProvider = newProfileProvider;
        this.newProfileService = newProfileService;
        this.jwtService = jwtService;
    }

    @ResponseBody
    @GetMapping("")
    public BaseResponse<GetProfileRes> getProfileRes() throws BaseException {

        try{
            if(jwtService.getJwt()==null){
                return new BaseResponse<>(EMPTY_JWT);
            }

            else{
                int userIdx=jwtService.getUserIdx();
                GetProfileRes getProfileRes= newProfileProvider.getProfileRes(userIdx);
                return new BaseResponse<>(getProfileRes);
            }

        }catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }



    @ResponseBody
    @GetMapping("/map")
    public BaseResponse<List<GetMapRes>> getMapRes() throws BaseException {

        try{
            if(jwtService.getJwt()==null){
                return new BaseResponse<>(EMPTY_JWT);
            }

            else{
                int userIdx=jwtService.getUserIdx();
                List<GetMapRes> getMapRes= newProfileProvider.getMapRes(userIdx);
                return new BaseResponse<>(getMapRes);
            }

        }catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @GetMapping("/list")
    public BaseResponse<GetListRes> geListRes(@RequestParam int order) throws BaseException {

        try{
            if(jwtService.getJwt()==null){
                return new BaseResponse<>(EMPTY_JWT);
            }

            else{
                int userIdx=jwtService.getUserIdx();
                GetListRes getListRes= newProfileProvider.getListRes(userIdx,order);
                return new BaseResponse<>(getListRes);
            }

        }catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @GetMapping("/{userIdx}/posts")
    public BaseResponse<GetMyPostRes> getMyPostsRes(@PathVariable("userIdx")int userIdx) throws BaseException {

        try{
            if(jwtService.getJwt()==null){
                return new BaseResponse<>(EMPTY_JWT);
            }

            else{
                int userIdxByJwt=jwtService.getUserIdx();

                GetMyPostRes getMyPostsRes= newProfileProvider.getMyPostsRes(userIdx,userIdxByJwt);
                return new BaseResponse<>(getMyPostsRes);
            }

        }catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @PostMapping("/picture")
    public BaseResponse<PostPictureRes> postPictureRes(@RequestPart(required = false) MultipartFile file ) throws BaseException {
        try{
            if(jwtService.getJwt()==null){
                return new BaseResponse<>(EMPTY_JWT);
            }

            else{
                int userIdx=jwtService.getUserIdx();
                PostPictureRes postPictureRes = newProfileService.postPictureRes(userIdx,file);
                return new BaseResponse<>(postPictureRes);
            }

        }catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }

    }

    @ResponseBody
    @GetMapping("/profile-img")
    public BaseResponse<GetProfileImgRes> getProfileImgRes() throws BaseException {

        try{
            if(jwtService.getJwt()==null){
                return new BaseResponse<>(EMPTY_JWT);
            }

            else{
                int userIdx=jwtService.getUserIdx();
                GetProfileImgRes getProfileImgRes= newProfileProvider.getProfileImgRes(userIdx);
                return new BaseResponse<>(getProfileImgRes);
            }

        }catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
    @PatchMapping("/profile-img")
    public BaseResponse<GetProfileImgRes> patchProfileImg(@RequestPart(required = false) MultipartFile file) throws BaseException{
        int userIdx=-1;
        if(jwtService.validateToken()){
            userIdx = jwtService.getUserIdxV2();
        }
        if(file != null){
            GetProfileImgRes getProfileImgRes= newProfileService.patchProfileImg(file,userIdx);
            return new BaseResponse<>(getProfileImgRes);

        }
        else {
            GetProfileImgRes getProfileImgRes= newProfileService.deleteProfileImg(userIdx);
           return new BaseResponse<>(getProfileImgRes);
        }
    }

    @ResponseBody
    @GetMapping("/result")
    public BaseResponse<GetResultRes> getResultRes() throws BaseException {

        try{
            if(jwtService.getJwt()==null){
                return new BaseResponse<>(EMPTY_JWT);
            }

            else{
                int userIdx=jwtService.getUserIdx();
                GetResultRes getResultRes= newProfileProvider.getResultRes(userIdx);
                return new BaseResponse<>(getResultRes);
            }

        }catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @GetMapping("/login-info")
    public BaseResponse<GetUserLoginInfoRes> getUserLoginInfo() throws BaseException {

        try{
            if(jwtService.getJwt()==null){
                return new BaseResponse<>(EMPTY_JWT);
            }

            else{
                int userIdx=jwtService.getUserIdx();
                GetUserLoginInfoRes getUserLoginInfoRes= newProfileProvider.getUserLoginInfo(userIdx);
                return new BaseResponse<>(getUserLoginInfoRes);
            }

        }catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }



}
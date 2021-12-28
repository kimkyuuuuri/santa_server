package com.smileflower.santa.src.profile;

import com.smileflower.santa.config.BaseException;
import com.smileflower.santa.config.BaseResponse;
import com.smileflower.santa.src.profile.model.*;
import com.smileflower.santa.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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
    @GetMapping("/posts")
    public BaseResponse<GetMyPostsRes> getMyPostsRes() throws BaseException {

        try{
            if(jwtService.getJwt()==null){
                return new BaseResponse<>(EMPTY_JWT);
            }

            else{
                int userIdx=jwtService.getUserIdx();
                GetMyPostsRes getMyPostsRes= newProfileProvider.getMyPostsRes(userIdx);
                return new BaseResponse<>(getMyPostsRes);
            }

        }catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

}
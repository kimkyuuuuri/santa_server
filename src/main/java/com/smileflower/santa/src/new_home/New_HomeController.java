package com.smileflower.santa.src.new_home;

import com.smileflower.santa.config.BaseException;
import com.smileflower.santa.config.BaseResponse;
import com.smileflower.santa.src.new_home.model.*;
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
@RequestMapping("/app/new-homes")
public class New_HomeController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final New_HomeProvider newHomeProvider;
    @Autowired
    private final New_HomeService newHomeService;
    @Autowired
    private final JwtService jwtService;


    public New_HomeController(New_HomeProvider newHomeProvider, New_HomeService newHomeService, JwtService jwtService) {
        this.newHomeProvider = newHomeProvider;
        this.newHomeService = newHomeService;
        this.jwtService = jwtService;
    }

    @ResponseBody
    @GetMapping("")
    public BaseResponse<GetHomeRes> getHome() throws BaseException {

        try{
            if(jwtService.getJwt()==null){
                return new BaseResponse<>(EMPTY_JWT);
            }

            else{
                int userIdx=jwtService.getUserIdx();
                GetHomeRes getHomeRes= newHomeProvider.getHome(userIdx);
                return new BaseResponse<>(getHomeRes);
            }

        }catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @GetMapping("/pictures")
    public BaseResponse <List<GetFlagsRes>> getPicturesRes() throws BaseException {

        try{
            if(jwtService.getJwt()==null){
                return new BaseResponse<>(EMPTY_JWT);
            }

            else{
                int userIdx=jwtService.getUserIdx();
                List<GetFlagsRes> getFlagsRes = newHomeProvider.getFlagsRes();
                return new BaseResponse<>(getFlagsRes);
            }

        }catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @GetMapping("/users")
    public BaseResponse <List<GetUsersRes>> getUsersRes() throws BaseException {

        try{
            if(jwtService.getJwt()==null){
                return new BaseResponse<>(EMPTY_JWT);
            }

            else{
                int userIdx=jwtService.getUserIdx();
                List<GetUsersRes> getUsersRes= newHomeProvider.getUsersRes();
                return new BaseResponse<>(getUsersRes);
            }

        }catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @GetMapping("/mountains")
    public BaseResponse <List<GetMountainsRes>> getMountainsRes() throws BaseException {

        try{
            if(jwtService.getJwt()==null){
                return new BaseResponse<>(EMPTY_JWT);
            }

            else{
                int userIdx=jwtService.getUserIdx();
                List<GetMountainsRes> getMountainsRes= newHomeProvider.getMountainsRes();
                return new BaseResponse<>(getMountainsRes);
            }

        }catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

}
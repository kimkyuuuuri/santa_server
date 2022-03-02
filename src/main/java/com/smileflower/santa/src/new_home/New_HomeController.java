package com.smileflower.santa.src.new_home;

import com.smileflower.santa.config.BaseException;
import com.smileflower.santa.config.BaseResponse;
import com.smileflower.santa.src.new_home.model.*;
import com.smileflower.santa.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    @GetMapping("/flags")
    public BaseResponse <List<GetFlagsMoreRes>> getFlagsRes() throws BaseException {

        try{
            if(jwtService.getJwt()==null){
                return new BaseResponse<>(EMPTY_JWT);
            }

            else{
                int userIdx=jwtService.getUserIdx();
                List<GetFlagsMoreRes> getFlagsMoreRes = newHomeProvider.getFlagsMoreRes(userIdx);
                return new BaseResponse<>(getFlagsMoreRes);
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
    public BaseResponse <List<GetMountainsRes>> getMountainsRes(@RequestParam int order) throws BaseException {

        try{
            if(jwtService.getJwt()==null){
                return new BaseResponse<>(EMPTY_JWT);
            }

            else{
                int userIdx=jwtService.getUserIdx();
                List<GetMountainsRes> getMountainsRes= newHomeProvider.getMountainsRes(order);
                return new BaseResponse<>(getMountainsRes);
            }

        }catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @GetMapping("/notification")
    public BaseResponse<List<GetNotificationRes>> getNotification() throws BaseException {

        try{
            if(jwtService.getJwt()==null){
                return new BaseResponse<>(EMPTY_JWT);
            }

            else{

                int userIdx=jwtService.getUserIdx();

                List<GetNotificationRes> getNotificationRes= newHomeProvider.getNotification(userIdx);
                return new BaseResponse<>(getNotificationRes);
            }

        }catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @PatchMapping("/notification/{notificationIdx}")
    public BaseResponse<String> modifyNotificationStatus(@PathVariable("notificationIdx")int notificationIdx) throws BaseException {

        if(jwtService.getJwt()==null){
            return new BaseResponse<>(EMPTY_JWT);
        }

        else{

             newHomeService.modifyNotificationStatus(notificationIdx);
            return new BaseResponse<>("확인 완료");
        }

    }

}
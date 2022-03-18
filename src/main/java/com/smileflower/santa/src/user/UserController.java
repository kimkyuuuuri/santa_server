package com.smileflower.santa.src.user;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.smileflower.santa.config.BaseException;
import com.smileflower.santa.config.BaseResponse;
import com.smileflower.santa.src.user.model.*;
import com.smileflower.santa.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;



import static com.smileflower.santa.config.BaseResponseStatus.*;
import static com.smileflower.santa.utils.ValidationRegex.*;


@RestController
@RequestMapping("/app/users")
public class UserController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final UserProvider userProvider;
    @Autowired
    private final UserService userService;
    @Autowired
    private final JwtService jwtService;




    public UserController(UserProvider userProvider, UserService userService, JwtService jwtService){
        this.userProvider = userProvider;
        this.userService = userService;
        this.jwtService = jwtService;
    }


    /**
     * 회원가입 API
     * [POST] /users
     * @return BaseResponse<PostUserRes>
     */
    // Body
    @ResponseBody
    @PostMapping("")
    public BaseResponse<PostUserRes> createUser(@RequestBody PostUserReq postUserReq) throws BaseException {
        //req에 입력하지 않은 경우
        if(postUserReq.getEmailId() == null || postUserReq.getPassword()==null || postUserReq.getName()==null){
            return new BaseResponse<>(POST_USERS_EMPTY);
        }

        //이메일 정규표현
        if(!isRegexEmail(postUserReq.getEmailId())){
            return new BaseResponse<>(POST_USERS_INVALID_EMAIL);
        }

        if(postUserReq.getPassword().length()<8){
            return new BaseResponse<>(INSUFFICIENT_PW_RANGE);
        }
        if(postUserReq.getPassword().length()>16){
            return new BaseResponse<>(EXCEED_PW_RANGE);
        }

        try{
            String forLoginPw = postUserReq.getPassword();

            PostUserRes postUserRes = userService.createUser(postUserReq);

            PostUserLoginReq postUserLoginReq = new PostUserLoginReq(postUserReq.getEmailId(),forLoginPw,postUserReq.getPushToken(),postUserReq.getTokenType());

            PostUserLoginRes postUserLoginRes = userService.loginUser(postUserLoginReq);

            return new BaseResponse<>(postUserRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }






    @ResponseBody
    @PostMapping("/login") 
    public BaseResponse<PostUserLoginRes> loginUser(@RequestBody PostUserLoginReq postUserLoginReq) throws BaseException {  // json으로 받아오는데 알아서 객체가 되어 받아짐 -> PostUserReq를 보면 받아올 것에 대한 객체가 구성되어 있고
        if(postUserLoginReq.getEmailId() == null){   //validation처리
            return new BaseResponse<>(POST_USERS_EMPTY_EMAIL);
        }
        if(!isRegexEmail(postUserLoginReq.getEmailId())){   // 형식적 validation
            return new BaseResponse<>(POST_USERS_INVALID_EMAIL);
        }
        try{
            PostUserLoginRes postUserLoginRes = userService.loginUser(postUserLoginReq);  // 조회가 아닌 행위는 service에서 진행하므로 UserService의 객체 userService에서 가져옴, 그래서 위에서 받아서 createUser로 넘김
            return new BaseResponse<>(postUserLoginRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }




    @ResponseBody
    @PatchMapping ("/logout")
    public BaseResponse<PatchUserLogoutRes> logoutUser() throws BaseException{
    /*
        int userIdxx=0;
        try{
            userIdxx = Integer.parseInt(userIdx);
        } catch(NumberFormatException e){ } */

        try{
            if(jwtService.getJwt()==null){
                return new BaseResponse<>(EMPTY_JWT);
            }
            else if(userProvider.checkJwt(jwtService.getJwt())==1){
                return new BaseResponse<>(INVALID_JWT);

            }

            else{
                PatchUserLogoutRes patchUserLogoutRes = userService.patchLogout(jwtService.getUserIdx());
                return new BaseResponse<>(patchUserLogoutRes);
            }
            /*
            else{
                return new BaseResponse<>(USERS_INVALID_ACCESS);
            } */
        }catch(BaseException exception){
                return new BaseResponse<>((exception.getStatus()));
            }
    }

    @ResponseBody
    @PostMapping("/name-check")
    public BaseResponse<PostNameRes> checkName(@RequestBody PostNameReq postNameReq) throws BaseException {
        try{

            PostNameRes postNameRes = userService.checkName(postNameReq);

            return new BaseResponse<>(postNameRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @GetMapping("/auto-login")
    public BaseResponse<GetAutoRes> autologin() throws BaseException{
        try{
            if(jwtService.getJwt()==null){
                return new BaseResponse<>(EMPTY_JWT);
            }
            else if(userProvider.checkJwt(jwtService.getJwt())==1){
                return new BaseResponse<>(INVALID_JWT);

            }

            else{
                String jwt=jwtService.getJwt();
                int userIdx=jwtService.getUserIdx();
                GetAutoRes getAutoRes = userProvider.getAuto(jwt,userIdx);
                return new BaseResponse<>(getAutoRes);
            }

        }catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }

    }


    @ResponseBody
    @GetMapping("/new-auto-login")
    public BaseResponse<GetAutoLoginRes> newAutologin() throws BaseException{
        try{
            if(jwtService.getJwt()==null){
                return new BaseResponse<>(EMPTY_JWT);
            }
            else if(userProvider.checkJwt(jwtService.getJwt())==1){
                return new BaseResponse<>(INVALID_JWT);

            }

            else{
                String jwt=jwtService.getJwt();
                int userIdx=jwtService.getUserIdx();
                GetAutoLoginRes getAutoLoginRes=new GetAutoLoginRes(userIdx);
                return new BaseResponse<>(getAutoLoginRes);
            }

        }catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }

    }

    @ResponseBody
    @DeleteMapping("/{userIdx}")
    public BaseResponse<DeleteUserRes> deleteUser(@PathVariable("userIdx") int userIdx) {

        try {
            int userIdxByJwt = jwtService.getUserIdx();

            if (userIdx != userIdxByJwt) {

                return new BaseResponse<>(INVALID_USER_JWT);
            }

            DeleteUserRes deleteUserRes = userService.deleteUser(userIdx);

            return new BaseResponse<>(deleteUserRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }


    @ResponseBody
    @PatchMapping("/{userIdx}/status")
    public BaseResponse<String> patchUserStatus(@PathVariable("userIdx") int userIdx) {

        try {
            int userIdxByJwt = jwtService.getUserIdx();

            if (userIdx != userIdxByJwt) {

                return new BaseResponse<>(INVALID_USER_JWT);
            }

             userService.patchUserStatus(userIdx);
            return new BaseResponse<>("삭제에 성공했습니다.");

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }
    @ResponseBody
    @PatchMapping("/nickname/{userIdx}")
    public BaseResponse modifyNickname(@PathVariable("userIdx") int userIdx,@RequestBody PostNameReq postNameReq){
        try{
            String name=postNameReq.getName();
            int userIdxByJWt = jwtService.getUserIdx();
            if (userIdx != userIdxByJWt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            userService.modifyNickname(userIdx,name);
            return new BaseResponse();
        }catch (BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @PatchMapping ("agree")
    public BaseResponse<String> agree(@RequestBody PostUserAgreeRes postUserAgreeRes) throws BaseException {
        try{
            if(jwtService.getJwt()==null){
                return new BaseResponse<>(EMPTY_JWT);
            }

            else{
                int userIdx=jwtService.getUserIdx();
                userService.agree(userIdx,postUserAgreeRes);
                return new BaseResponse<>("수정 완료");
            }

        }catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }

    }

    @ResponseBody
    @PatchMapping ("restore-user")
    public BaseResponse<String> restoreUser(@RequestBody PatchUserStatusReq patchUserStatusReq) throws BaseException {
        try{
            if(patchUserStatusReq.getEmailId() == null || patchUserStatusReq.getPassword()==null||patchUserStatusReq.getName()==null){
                return new BaseResponse<>(POST_USERS_EMPTY);
            }

            //이메일 정규표현
            if(!isRegexEmail(patchUserStatusReq.getEmailId())){
                return new BaseResponse<>(POST_USERS_INVALID_EMAIL);
            }



            if(patchUserStatusReq.getPassword().length()<8){
                return new BaseResponse<>(INSUFFICIENT_PW_RANGE);
            }
            if(patchUserStatusReq.getPassword().length()>16){
                return new BaseResponse<>(EXCEED_PW_RANGE);
            }

            else{

                userService.restoreUser(patchUserStatusReq);
                return new BaseResponse<>("복구 완료");
            }

        }catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }

    }

}

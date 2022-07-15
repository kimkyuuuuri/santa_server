package com.smileflower.santa.src.email;


import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.smileflower.santa.config.BaseException;
import com.smileflower.santa.config.BaseResponse;
import com.smileflower.santa.src.email.model.PostAuthReq;
import com.smileflower.santa.src.email.model.PostEmailReq;

import static com.smileflower.santa.config.BaseResponseStatus.*;
import static com.smileflower.santa.utils.ValidationRegex.isRegexEmail;

@RequiredArgsConstructor
@RestController
@RequestMapping("/app/email")
public class EmailController {

    @Autowired
    private final EmailService emailService;
    @Autowired
    private final EmailProvider emailProvider;
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @PostMapping("/password")
    public BaseResponse<String> emailPassword(@RequestBody PostEmailReq postEmailReq) {
        if(postEmailReq.getEmail() == null){
            return new BaseResponse<>(POST_USERS_EMPTY_EMAIL);
        }
        //이메일 정규표현
        if(!isRegexEmail(postEmailReq.getEmail())){
            return new BaseResponse<>(POST_USERS_INVALID_EMAIL);
        }
        try{
            emailService.emailpassword(postEmailReq.getEmail());
            return new BaseResponse<>("");
        }
        catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @PostMapping("/code")
    public BaseResponse<String> emailAuth(@RequestBody PostEmailReq postEmailReq) {
        if(postEmailReq.getEmail() == null){
            return new BaseResponse<>(POST_USERS_EMPTY_EMAIL);
        }
        //이메일 정규표현
        if(!isRegexEmail(postEmailReq.getEmail())){
            return new BaseResponse<>(POST_USERS_INVALID_EMAIL);
        }
        try{
            emailService.sendEmailMessage(postEmailReq.getEmail());
            return new BaseResponse<>("");
        }
        catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }


    @PostMapping("/verify")
    public BaseResponse<String> verifyCode(@RequestBody PostAuthReq postAuthReq) {
        if(postAuthReq.getEmail() == null){
            return new BaseResponse<>(POST_USERS_EMPTY_EMAIL);
        }
        //이메일 정규표현
        if(!isRegexEmail(postAuthReq.getEmail())){
            return new BaseResponse<>(POST_USERS_INVALID_EMAIL);
        }
        try{
            if(postAuthReq.getCode() == null){
                return new BaseResponse<>(POST_AUTH_EMPTY_CODE);
            }
            emailProvider.checkAuth(postAuthReq);
            return new BaseResponse<>("");
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}
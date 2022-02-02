package com.smileflower.santa.src.social_login;

import com.smileflower.santa.apple.model.domain.AppleUser;
import com.smileflower.santa.apple.model.domain.Email;
import com.smileflower.santa.apple.model.dto.*;
import com.smileflower.santa.utils.*;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring5.SpringTemplateEngine;

import java.io.IOException;

@RequiredArgsConstructor
@Service
public class Social_loginService {

    @Autowired
    private final Social_loginDao social_loginDao;
    @Autowired
    private final Social_loginProvider social_loginProvider;
    @Autowired
    private final JavaMailSender emailSender;
    private final SpringTemplateEngine templateEngine;
    @Autowired
    private final new_AppleJwtUtils appleJwtUtils;

    final Logger logger = LoggerFactory.getLogger(this.getClass());



    public CheckUserResponse checkUser(String id_token) {
        Claims claim = appleJwtUtils.getClaimsBy(id_token);
        Email email = new Email((String)claim.get("email"));
        if(social_loginDao.findByEmail(email))
            return new CheckUserResponse(email,true);
        else
            return new CheckUserResponse(email,false);

    }

    public AppleSigninResponse createUser(AppleSigninRequest appleSigninRequest){
        if(social_loginDao.findByEmail(appleSigninRequest.getUserEmail()))
            social_loginDao.save(new AppleUser(appleSigninRequest.getUserEmail(),appleSigninRequest.getName()));
        AppleToken.Response appleLoginResponse = new AppleToken.Response();
        try {
            appleLoginResponse = appleJwtUtils.getTokenByCode(appleJwtUtils.makeClientSecret(), appleSigninRequest.getAuthorizationCode());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new AppleSigninResponse(appleLoginResponse.getRefresh_token(),appleSigninRequest.getUserEmail().getEmail(),appleSigninRequest.getName());
    }

    public AppleLoginResponse loginUser(AppleLoginRequest appleLoginRequest){
        AppleToken.Response tokenResponse = new AppleToken.Response();
        try {
            tokenResponse = appleJwtUtils.getTokenByRefreshToken(appleJwtUtils.makeClientSecret(), appleLoginRequest.getRefreshToken());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new AppleLoginResponse(appleJwtUtils.getEmail(tokenResponse.getId_token()).getEmail(),appleLoginRequest.getRefreshToken());
    }


    public String getAppleClientSecret(String id_token) {

        if (appleJwtUtils.getClaimsBy(id_token)!=null) {
            try {
                return appleJwtUtils.makeClientSecret();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
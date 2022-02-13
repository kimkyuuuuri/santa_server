package com.smileflower.santa.src.social_login;

import com.smileflower.santa.config.BaseException;
import com.smileflower.santa.src.social_login.model.*;

import com.smileflower.santa.src.social_login.model.Email;
import com.smileflower.santa.src.social_login.utils.NewAppleJwtUtils;
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
    private final NewAppleJwtUtils newAppleJwtUtils;

    final Logger logger = LoggerFactory.getLogger(this.getClass());



    public CheckUserRes checkUser(String id_token) throws BaseException {
        Claims claim = newAppleJwtUtils.getClaimsBy(id_token);
        Email email = new Email((String)claim.get("email"));
        if(social_loginDao.findByEmail(email))
            return new CheckUserRes(email,true);
        else
            return new CheckUserRes(email,false);

    }

    public ApplePostUserRes createUser(ApplePostUserReq applePostUserReq) throws BaseException {
        if(social_loginDao.findByEmail(applePostUserReq.getUserEmail()))
            social_loginDao.insertUser(new AppleUser(applePostUserReq.getUserEmail(),applePostUserReq.getName()));

        AppleToken.Response appleLoginRes = new AppleToken.Response();
        try {
            appleLoginRes = newAppleJwtUtils.getTokenByCode(newAppleJwtUtils.makeClientSecret(), applePostUserReq.getAuthorizationCode());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ApplePostUserRes(appleLoginRes.getRefresh_token(),applePostUserReq.getUserEmail().getEmail(),applePostUserReq.getName());
    }

    public AppleLoginRes loginUser(AppleLoginReq appleLoginReq) throws BaseException {
        AppleToken.Response tokenResponse = new AppleToken.Response();
        try {
            tokenResponse = newAppleJwtUtils.getTokenByRefreshToken(newAppleJwtUtils.makeClientSecret(), appleLoginReq.getRefreshToken());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new AppleLoginRes(newAppleJwtUtils.getEmail(tokenResponse.getId_token()).getEmail(),appleLoginReq.getRefreshToken());
    }


    public String getAppleClientSecret(String id_token) throws BaseException {

        if (newAppleJwtUtils.getClaimsBy(id_token)!=null) {
            try {
                return newAppleJwtUtils.makeClientSecret();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
package com.smileflower.santa.src.social_login;

import com.smileflower.santa.config.BaseException;
import com.smileflower.santa.config.secret.Secret;
import com.smileflower.santa.src.social_login.model.*;

import com.smileflower.santa.src.social_login.utils.NewAppleJwtUtils;


import com.smileflower.santa.utils.JwtService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.transaction.Transactional;
import java.io.IOException;

import static com.smileflower.santa.config.BaseResponseStatus.*;


@RequiredArgsConstructor
@Service
public class Social_loginService {

    @Autowired
    private final Social_loginDao social_loginDao;
    private final JwtService jwtService;
    @Autowired
    private final Social_loginProvider social_loginProvider;
    @Autowired
    private final JavaMailSender emailSender;
    private final SpringTemplateEngine templateEngine;
    @Autowired
    private final NewAppleJwtUtils newAppleJwtUtils;

    final Logger logger = LoggerFactory.getLogger(this.getClass());



    @Transactional
    public PostUserLoginRes kakaoLogin(String id) throws BaseException {
        //System.out.println("테스트1:"+postUserLoginReq.getEmailId());
        int userIdx = social_loginProvider.checkKakaoAccount(id);
      //카카오 계정 체크해서 idx 리턴하는 함수 만들기
        String name2;
        if (social_loginProvider.checkLogExist(userIdx) != 1) {  // 신규로 처음 로그인하는 사람을 위한
            name2 = social_loginDao.recordLog(userIdx, "I");

        } else {
            name2 = social_loginDao.recordLog(userIdx, "I");

        }

        //jwt발급
        String jwt = jwtService.createJwt(userIdx);
        int jwtIdx = social_loginDao.postJwt(jwt);

        return new PostUserLoginRes(jwt, userIdx, name2);
    }
    public CheckUserRes checkUser(String id_token) throws BaseException {
        Claims claim = newAppleJwtUtils.getClaimsBy(id_token);
        Email email = new Email((String)claim.get("email"));
        if(social_loginDao.findByEmail(email))
            return new CheckUserRes(email,true);
        else
            return new CheckUserRes(email,false);

    }

    public int checkKakaoName(String name){
        return social_loginDao.checkKakaoName(name);
    }



    public int checkEmailId(String email){
        return social_loginDao.checkEmail(email);
    }

    public ApplePostUserRes createUser(ApplePostUserReq applePostUserReq,int appleId) throws BaseException {
        if(social_loginDao.findByEmail(applePostUserReq.getUserEmail()))
            social_loginDao.insertUser(new AppleUser(applePostUserReq.getUserEmail(),applePostUserReq.getName()+appleId, applePostUserReq.getUserIdentifier()));

        AppleToken.Response appleLoginRes = new AppleToken.Response();
        try {
            appleLoginRes = newAppleJwtUtils.getTokenByCode(newAppleJwtUtils.makeClientSecret(), applePostUserReq.getAuthorizationCode());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ApplePostUserRes(appleLoginRes.getRefresh_token(),applePostUserReq.getUserEmail().getEmail(),applePostUserReq.getName());
    }

    public AppleLoginRes loginUser(ApplePostUserReq applePostUserReq) throws BaseException {


        String jwt="";

            int userIdx = social_loginProvider.checkAppleAccount(applePostUserReq.getUserIdentifier());
            jwt = jwtService.createJwt(userIdx);

        return new AppleLoginRes(userIdx,jwt);
    }
    //POST
    public PostUserRes createKakaoUser(String name,String Email,String id) throws BaseException {
        //중복



            int userIdx = social_loginDao.createKakaoUser(name,Email,id);

            //jwt발급
            String jwt = jwtService.createJwt(userIdx);
            return new PostUserRes(jwt, userIdx);


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
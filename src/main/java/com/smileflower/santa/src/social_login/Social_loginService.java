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
    public PostUserLoginRes kakaoLogin(String id,String pushToken,String tokenType) throws BaseException {
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
        patchUserPushToken(userIdx,pushToken,tokenType);
        int jwtIdx = social_loginDao.postJwt(jwt);
       // patchUserStatus(userIdx);
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


    public AppleLoginRes loginUser(ApplePostUserReq applePostUserReq) throws BaseException {

    int userIdx=0;
        String name2;
    if(applePostUserReq.getUserIdentifier()!=null) {
         userIdx = social_loginProvider.checkAppleAccount(applePostUserReq.getUserIdentifier());
    }
    else{
        userIdx = social_loginProvider.checkAppleAccountByCode(applePostUserReq.getAuthorizationCode());
    }
        if (social_loginProvider.checkLogExist(userIdx) != 1) {  // 신규로 처음 로그인하는 사람을 위한
            name2 = social_loginDao.recordLog(userIdx, "I");

        } else {
            name2 = social_loginDao.recordLog(userIdx, "I");

        }
       patchUserPushToken(userIdx,applePostUserReq.getPushToken(),applePostUserReq.getTokenType());
        String jwt = jwtService.createJwt(userIdx);
     // patchUserStatus(userIdx);
        return new AppleLoginRes(userIdx,jwt);
    }

    //POST
    public PostUserRes createKakaoUser(String name,String Email,String id) throws BaseException {
        //중복


            int userNumber=social_loginDao.getKakaoUserNumber();
            int userIdx = social_loginDao.createKakaoUser(name+userNumber,Email,id);

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
    public void patchUserPushToken(int userIdx,String pushToken,String tokenType) throws BaseException{

        social_loginDao.updateUserToken(userIdx,pushToken,tokenType);

    }

    public void patchUserStatus(int userIdx) throws BaseException{

        social_loginDao.patchUserIsFirst(userIdx);

    }
    public ApplePostUserRes createUser(ApplePostUserReq applePostUserReq) throws BaseException {
        int userNumber=social_loginDao.getAppleUserNumber();
        social_loginDao.insertUser( applePostUserReq.getUserIdentifier(),"apple"+applePostUserReq.getName()+userNumber,applePostUserReq.getAuthorizationCode());

        AppleToken.Response appleLoginRes = new AppleToken.Response();

        return new ApplePostUserRes(appleLoginRes.getRefresh_token(),applePostUserReq.getUserEmail(),applePostUserReq.getName());
    }
}
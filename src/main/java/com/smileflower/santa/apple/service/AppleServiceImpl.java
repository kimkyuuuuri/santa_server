package com.smileflower.santa.apple.service;

import com.smileflower.santa.apple.model.domain.AppleUser;
import com.smileflower.santa.apple.model.domain.Email;
import com.smileflower.santa.apple.model.dto.*;
import com.smileflower.santa.apple.repository.AppleJdbcRepository;
import com.smileflower.santa.apple.repository.AppleRepository;
import com.smileflower.santa.apple.utils.AppleJwtUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class AppleServiceImpl implements AppleService {

    @Autowired
    AppleJwtUtils appleJwtUtils;
    @Autowired
    AppleRepository appleRepository;

    @Override
    public CheckUserResponse checkUser(String id_token) {
        Claims claim = appleJwtUtils.getClaimsBy(id_token);
        Email email = new Email((String)claim.get("email"));
        if(appleRepository.findByEmail(email))
            return new CheckUserResponse(email,true);
        else
            return new CheckUserResponse(email,false);

    }

    public AppleSigninResponse createUser(AppleSigninRequest appleSigninRequest){
        if(appleRepository.findByEmail(appleSigninRequest.getUserEmail()))
            appleRepository.save(new AppleUser(appleSigninRequest.getUserEmail(),appleSigninRequest.getName()));
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

    /**
     * 유효한 id_token인 경우 client_secret 생성
     *
     * @param id_token
     * @return
     */
    @Override
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

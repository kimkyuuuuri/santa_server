package com.smileflower.santa.src.social_login;



import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.smileflower.santa.config.BaseException;
import com.smileflower.santa.config.BaseResponse;

import com.smileflower.santa.src.social_login.model.*;

import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.UnsupportedEncodingException;


@RequiredArgsConstructor
@RestController
@RequestMapping("/app")
public class Social_loginController {

    @Autowired
    private final Social_loginService socialloginService;
    @Autowired
    private final Social_loginProvider socialloginProvider;
    private static int kakaoId=1;
    private static int appleId=1;
    final Logger logger = LoggerFactory.getLogger(this.getClass());




    @ResponseBody
    @PostMapping ("/kakao-login")
    public  BaseResponse<PostUserLoginRes> kakaologin( @RequestBody PostUserReq postUserReq) throws UnsupportedEncodingException, BaseException {
        RestTemplate rt2 = new RestTemplate();

        // HttpHeader 오브젝트 생성
        HttpHeaders headers2 = new HttpHeaders();
        headers2.add("Authorization", "Bearer "+ postUserReq.getAccessToken());
        headers2.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HttpHeader와 HttpBody를 하나의 오브젝트에 담기
        HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest2 =
                new HttpEntity<>(headers2);
        //System.out.println(kakaoProfileRequest2);
        // Http 요청하기 - Post방식으로 - 그리고 response 변수의 응답 받음.
        ResponseEntity<String> response2 = rt2.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoProfileRequest2,
                String.class
        );


        ObjectMapper objectMapper2 = new ObjectMapper();
        KakaoProfile kakaoProfile = null;

        objectMapper2.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);


        try {

            kakaoProfile = objectMapper2.readValue(response2.getBody(), KakaoProfile.class);

        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }



        try{
            if(socialloginProvider.checkKakaoId(Integer.toString(kakaoProfile.getId())) == 0) {
                PostUserRes postUserRes = socialloginService.createKakaoUser( "kakao"+kakaoProfile.getProperties().getNickname()+kakaoId,"",Integer.toString(kakaoProfile.getId()));
                kakaoId+=1;

            }

                PostUserLoginRes postUserLoginRes = socialloginService.kakaoLogin(Integer.toString(kakaoProfile.getId()),postUserReq.getPushToken(), postUserReq.getTokenType());

            return new BaseResponse<>(postUserLoginRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }



    }

    @GetMapping(value = "/new-apple")
    @ResponseBody
    public BaseResponse<CheckUserRes>  checkUser(@RequestBody CheckUserReq checkUserReq) {
        try{
        CheckUserRes checkUserRes=socialloginService.checkUser((checkUserReq.getIdentifyToken()));
        return new BaseResponse<>(checkUserRes);
    } catch(BaseException exception){
        return new BaseResponse<>((exception.getStatus()));
    }
    }

    /**
     * Sign in with Apple
     *
     *
     * @return AppleLoginResponse
     */
    @PostMapping(value = "/new-apple/login")
    @ResponseBody
    public BaseResponse<AppleLoginRes> appleLogin(@RequestBody ApplePostUserReq applePostUserReq) {

        try{
            if(socialloginProvider.checkAppleId(applePostUserReq.getUserIdentifier()) != 1) {

                ApplePostUserRes applePostUserRes=socialloginService.createUser(applePostUserReq,appleId);
                appleId+=1;


            }
            AppleLoginRes appleLoginRes=socialloginService.loginUser(applePostUserReq);

                return new BaseResponse<>(appleLoginRes);

        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @PostMapping(value = "/new-apple/loginCallBackApple")
    @ResponseBody
    public BaseResponse<AppleToken> androidAppleLogin(@RequestBody ServicesResponse serviceResponse) throws BaseException {

    System.out.println(serviceResponse);
            if (serviceResponse == null) {
                return null;
            }
        try {
    String code = serviceResponse.getCode();
    String client_secret = socialloginService.getAppleClientSecret(serviceResponse.getId_token());
           return new BaseResponse<>(new AppleToken(code,client_secret));
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }


    }

    /**
     * Sign up with Apple
     *
     *
     * @return AppleLoginResponse
     */

    }
//    /**
//     * Logout with Apple
//     *
//     * @param appleLoginRequest
//     * @return AppleLoginResponse
//     */
//    @PatchMapping(value = "/api/apple/logout")
//    @ResponseBody
//    public ApiResult<AppleLoginResponse> appleLogout(@RequestBody AppleLoginRequest appleLoginRequest) {
//        String client_secret = appleService.getAppleClientSecret(appleLoginRequest.getIdentifyToken());
//        return ApiResult.OK(
//                appleService.requestCodeValidations(client_secret, appleLoginRequest.getAuthorizationCode(), appleLoginRequest.getRefreshToken())
//        );
//    }



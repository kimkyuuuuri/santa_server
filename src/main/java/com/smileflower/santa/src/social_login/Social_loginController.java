package com.smileflower.santa.src.social_login;



import com.smileflower.santa.apple.model.dto.*;
import com.smileflower.santa.config.BaseException;
import com.smileflower.santa.exception.ApiResult;
import com.smileflower.santa.src.social_login.model.KakaoProfile;
import com.smileflower.santa.src.social_login.model.OAuthToken;
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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;


@RequiredArgsConstructor
@RestController
@RequestMapping("/app")
public class Social_loginController {

    @Autowired
    private final Social_loginService socialloginService;
    @Autowired
    private final Social_loginProvider socialloginProvider;
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @ResponseBody
    @GetMapping ("/kakao-login")
    public String kakaoLogin() throws BaseException {
      return "hi";
    }

    @ResponseBody
    @GetMapping ("/kakao-createUser")
    public String kakaoCreateUser() throws BaseException {
        return "hi";
    }

    public KakaoProfile kakaologin( String code ) throws UnsupportedEncodingException, BaseException {
        RestTemplate rt = new RestTemplate();

        // HttpHeader 오브젝트 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HttpBody 오브젝트 생성
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", "371e1933fdfb4d5cc659db8323d89ee6");
        params.add("redirect_uri", "http://localhost:8080/users/kakao-login");
        params.add("code", code);
        //code를 어떻게 동적으로 받지?


        // HttpHeader와 HttpBody를 하나의 오브젝트에 담기
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
                new HttpEntity<>(params, headers);

        // Http 요청하기 - Post방식으로 - 그리고 response 변수의 응답 받음.
        ResponseEntity<String> response = rt.exchange(

                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        // Gson, Json Simple, ObjectMapper
        ObjectMapper objectMapper = new ObjectMapper();
        OAuthToken oauthToken = null;
        try {
            oauthToken = objectMapper.readValue(response.getBody(), OAuthToken.class);
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        System.out.println("카카오 엑세스 토큰 : "+oauthToken.getAccess_token());


        RestTemplate rt2 = new RestTemplate();

        // HttpHeader 오브젝트 생성
        HttpHeaders headers2 = new HttpHeaders();
        headers2.add("Authorization", "Bearer "+oauthToken.getAccess_token());
        headers2.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        System.out.println(oauthToken.getAccess_token());
    //토큰에서 아이디를 빼줌
        // HttpHeader와 HttpBody를 하나의 오브젝트에 담기
        HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest2 =
                new HttpEntity<>(headers2);

        // Http 요청하기 - Post방식으로 - 그리고 response 변수의 응답 받음.
        ResponseEntity<String> response2 = rt2.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoProfileRequest2,
                String.class
        );
        System.out.println(response2.getBody());
        //이걸 user로 받을까?

        ObjectMapper objectMapper2 = new ObjectMapper();
        KakaoProfile kakaoProfile = null;
        try {
            System.out.println(response2.getBody());
            kakaoProfile = objectMapper2.readValue(response2.getBody(), KakaoProfile.class);
            System.out.println(kakaoProfile.getId());
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        // User 오브젝트 : username, password, email - email넣어야됨
        System.out.println("카카오 아이디(번호) : "+kakaoProfile.getId());
        System.out.println("카카오 이메일 : "+kakaoProfile.getKakao_account().getEmail());

        System.out.println("카카오 아이디(번호) : "+kakaoProfile.getProperties().getNickname());
        // System.out.println("카카오 아이디(번호) : "+kakaoProfile.getProperties().getProfile_image());


        // 가입자 혹은 비가입자 체크 해서 처리

        //id를 넣으면 idx를 return?
        String a;
        String userId=Integer.toString(kakaoProfile.getId());
        System.out.println(kakaoProfile.getKakao_account().getGender());
        if(kakaoProfile.getKakao_account().getGender().equals("female"))
            a="F";
        else
            a="M";

     /*   if(userProvider.checkuserId("kakao_"+userId) == 0) {
            System.out.println("기존 회원이 아니기에 자동 회원가입을 진행합니다");
            userService.kakaoCreateUser("kakao_"+userId,kakaoProfile.getKakao_account().getEmail(),kakaoProfile.getProperties().getNickname(),a,kakaoProfile.getKakao_account().getBirthday());

        }
        else
        {
            userProvider.kakaologIn("kakao_"+userId);
        }

*/
        return kakaoProfile;

    }

    @GetMapping(value = "/new-apple")
    @ResponseBody
    public ApiResult<CheckUserResponse> checkUser(@RequestBody CheckUserRequest checkUserRequest) {
        return ApiResult.OK(
                socialloginService.checkUser((checkUserRequest.getIdentifyToken()))
        );
    }
    /**
     * Sign in with Apple
     *
     * @param appleLoginRequest
     * @return AppleLoginResponse
     */
    @PostMapping(value = "/new-apple/login")
    @ResponseBody
    public ApiResult<AppleLoginResponse> appleLogin(@RequestBody AppleLoginRequest appleLoginRequest) {
        return ApiResult.OK(
                socialloginService.loginUser(appleLoginRequest)
        );
    }
    /**
     * Sign up with Apple
     *
     * @param appleSigninRequest
     * @return AppleLoginResponse
     */
    @PostMapping(value = "/new-apple/join")
    @ResponseBody
    public ApiResult<AppleSigninResponse> appleJoin(@RequestBody AppleSigninRequest appleSigninRequest) {

        return ApiResult.OK(
                socialloginService.createUser(appleSigninRequest)
        );
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


}
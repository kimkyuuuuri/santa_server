package com.smileflower.santa.apple.controller;

import com.smileflower.santa.apple.model.dto.*;
import com.smileflower.santa.apple.service.AppleService;
import com.smileflower.santa.exception.ApiResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class AppleController {

    private Logger logger = LoggerFactory.getLogger(AppleController.class);

    @Autowired
    AppleService appleService;

    /**
     * Check user with Apple
     *
     * @param checkUserRequest
     * @return CheckUserResponse
     */
    @GetMapping(value = "/api/apple")
    @ResponseBody
    public ApiResult<CheckUserResponse> checkUser(@RequestBody CheckUserRequest checkUserRequest) {
        return ApiResult.OK(
                appleService.checkUser((checkUserRequest.getIdentifyToken()))
        );
    }
    /**
     * Sign in with Apple
     *
     * @param appleLoginRequest
     * @return AppleLoginResponse
     */
    @PostMapping(value = "/api/apple/login")
    @ResponseBody
    public ApiResult<AppleLoginResponse> appleLogin(@RequestBody AppleLoginRequest appleLoginRequest) {
        return ApiResult.OK(
                appleService.loginUser(appleLoginRequest)
        );
    }
    /**
     * Sign up with Apple
     *
     * @param appleSigninRequest
     * @return AppleLoginResponse
     */
    @PostMapping(value = "/api/apple/join")
    @ResponseBody
    public ApiResult<AppleSigninResponse> appleJoin(@RequestBody AppleSigninRequest appleSigninRequest) {

        return ApiResult.OK(
                appleService.createUser(appleSigninRequest)
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

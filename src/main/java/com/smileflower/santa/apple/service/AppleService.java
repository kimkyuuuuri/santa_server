package com.smileflower.santa.apple.service;

import com.smileflower.santa.apple.model.dto.*;

public interface AppleService {

    CheckUserResponse checkUser(String id_token);

    AppleSigninResponse createUser(AppleSigninRequest appleSigninRequest);

    AppleLoginResponse loginUser(AppleLoginRequest appleLoginRequest);

    String getAppleClientSecret(String id_token);

}

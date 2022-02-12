package com.smileflower.santa.src.social_login.model;

import com.smileflower.santa.apple.model.domain.Email;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CheckUserReq {
    private String authorizationCode;
    private String identifyToken;

}

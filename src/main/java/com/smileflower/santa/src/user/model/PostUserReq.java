package com.smileflower.santa.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostUserReq {
    private String emailId;
    private String name;
    private String password;
    private String passwordCheck;
}

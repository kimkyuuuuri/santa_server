package com.smileflower.santa.src.social_login.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CheckUserRes {
    private Email userEmail;
    private boolean isUser;


}

package com.smileflower.santa.src.profile.model;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class GetUserRes {

    private String userImageUrl;

    private String userName;

    public GetUserRes(String userImageUrl, String userName) {

        this.userImageUrl=userImageUrl;

        this.userName=userName;

    }


}

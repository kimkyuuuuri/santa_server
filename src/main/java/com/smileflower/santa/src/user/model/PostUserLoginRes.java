package com.smileflower.santa.src.user.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class PostUserLoginRes {
    private String jwt;
    private int userIdx;
    private String name;
    private String status = "로그인 성공!";

    public PostUserLoginRes(String jwt,int userIdx,String name){
        this.jwt=jwt;
        this.userIdx=userIdx;
        this.name=name;
    }
}
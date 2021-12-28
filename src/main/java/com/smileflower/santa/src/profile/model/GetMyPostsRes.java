package com.smileflower.santa.src.profile.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter



public class GetMyPostsRes {
    private int userIdx;
    private String name;
    private List<GetPostsRes> getPostsRes;


    public GetMyPostsRes(int userIdx, String name, List<GetPostsRes> getPostsRes) {
        this.userIdx=userIdx;
        this.name=name;
        this.getPostsRes=getPostsRes;
    }
}


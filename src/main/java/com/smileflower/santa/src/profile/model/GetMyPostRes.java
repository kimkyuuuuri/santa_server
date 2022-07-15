package com.smileflower.santa.src.profile.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter

@AllArgsConstructor

public class GetMyPostRes {
    private String isMyPost;
    private int userIdx;

    private String name;
    private List<GetPostRes> getPostsRes;



    public GetMyPostRes(int userIdx, String name, List<GetPostRes> getPostsRes) {
        this.userIdx=userIdx;
        this.name=name;
        this.getPostsRes=getPostsRes;
    }
}


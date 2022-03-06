package com.smileflower.santa.src.profile.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter

@AllArgsConstructor

public class GetMyPostsRes {
    private String isMyPost;
    private int userIdx;

    private String name;
    private List<GetPostsRes> getPostsRes;



    public GetMyPostsRes(int userIdx, String name, List<GetPostsRes> getPostsRes) {
        this.userIdx=userIdx;
        this.name=name;
        this.getPostsRes=getPostsRes;
    }
}


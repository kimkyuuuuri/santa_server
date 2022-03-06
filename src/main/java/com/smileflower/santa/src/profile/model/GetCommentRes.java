package com.smileflower.santa.src.profile.model;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class GetCommentRes {
    private int userIdx;
    private String userImageUrl;
    private String userName;
    private String contents;
    private int count;



    public GetCommentRes(int userIdx, String userImageUrl, String userName, String contents, Integer count) {
        this.userIdx=userIdx;
        this.userImageUrl=userImageUrl;
        this.userName=userName;
        this.contents=contents;
        this.count=count;
    }

}
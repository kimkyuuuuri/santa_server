package com.smileflower.santa.src.new_home.model;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class GetUserRes {
    private int userIdx;
    private String userImageUrl;
    private String level;
    private String userName;
    private String agoTime;
    private String height;
    public GetUserRes(int userIdx, String userImageUrl, String level, String userName, String agoTime, String height) {
        this.userIdx=userIdx;
        this.userImageUrl=userImageUrl;
        this.level=level;
        this.userName=userName;
        this.agoTime=agoTime;
        this.height=height;
    }


}

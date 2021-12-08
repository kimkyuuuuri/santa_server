package com.smileflower.santa.src.new_home.model;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class GetPicturesRes {
    private int userIdx;
    private String userImageUrl;
    private String level;
    private String userName;
    private int commentCount;
    private int saveCount;
    private int pictureIdx;
    private String pictureImageUrl;


    public GetPicturesRes(int userIdx, String userImageUrl, String level, String userName, int commentCount, int saveCount, int pictureIdx, String pictureImageUrl) {
        this.userIdx=userIdx;
        this.userImageUrl=userImageUrl;
        this.level=level;
        this.userName=userName;
        this.commentCount=commentCount;
        this.saveCount=saveCount;
        this.pictureIdx=pictureIdx;
        this.pictureImageUrl=pictureImageUrl;
    }
}

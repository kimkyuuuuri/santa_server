package com.smileflower.santa.src.new_home.model;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter

public class GetFlagsMoreRes {
    private int userIdx;
    private String userImageUrl;
    private String level;
    private String userName;
    private String isFlag;
    private int commentCount;
    private int saveCount;
    private int flagIdx;
    private String flagImageUrl;
    private List<GetCommentRes> getCommentRes;



    public GetFlagsMoreRes(int userIdx, String userImageUrl, String level, String userName, String isFlag,int commentCount, int saveCount, int flagIdx, String flagImageUrl
    ,List<GetCommentRes> getCommentRes) {
        this.userIdx=userIdx;
        this.userImageUrl=userImageUrl;
        this.level=level;
        this.userName=userName;
        this.isFlag=isFlag;
        this.commentCount=commentCount;
        this.saveCount=saveCount;
        this.flagIdx=flagIdx;
        this.flagImageUrl=flagImageUrl;
        this.getCommentRes=getCommentRes;



    }
}

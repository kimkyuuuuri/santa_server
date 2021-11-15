package com.smileflower.santa.src.home.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class GetHomeMountainRes {
    private int mountainIdx;
    private String mountainName;
    private String mountainImage;
    private int difficulty;
    private int userIdx;
    private String userName;
    private String userImage;
    private int flagCount;
    public GetHomeMountainRes(int mountainIdx,String mountainName,String mountainImage,
                              int difficulty,int userIdx, String userName,String userImage,int flagCount){
        this.mountainIdx=mountainIdx;
        this.mountainName=mountainName;
        this.mountainImage=mountainImage;
        this.difficulty=difficulty;
        this.userIdx=userIdx;
        this.userName= userName;
        this.userImage=userImage;
        this.flagCount=flagCount;
    }
}

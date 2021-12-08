package com.smileflower.santa.src.new_home.model;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class GetMountainsRes {
    private int mountainIdx;
    private String mountainImageUrl;
    private String isHot;
    private int difficulty;
    private String mountainName;
    private String high;
    private int userIdx;
    private String userImageUrl;


    public GetMountainsRes(int mountainIdx, String mountainImageUrl, String isHot, int difficulty, String mountainName, String high, int userIdx, String userImageUrl) {
    this.mountainIdx=mountainIdx;
    this.mountainImageUrl=mountainImageUrl;
    this.isHot=isHot;
    this.difficulty=difficulty;
    this.mountainName=mountainName;
    this.high=high;
    this.userIdx=userIdx;
    this.userImageUrl=userImageUrl;
       }
}

package com.smileflower.santa.src.profile.model;


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
    private String isSaved;


    public GetMountainsRes(int mountainIdx, String mountainImageUrl, String isHot, int difficulty, String mountainName, String high, String isSaved) {
    }
}

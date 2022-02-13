package com.smileflower.santa.src.profile.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetMountainsRes {
    private int mountainIdx;
    private String mountainImageUrl;
    private String isHot;
    private int difficulty;
    private String mountainName;
    private String high;
    private String isSaved;



}

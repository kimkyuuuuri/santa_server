package com.smileflower.santa.src.profile.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetFlagRes {
    private Long flagIdx;
    private String userImageUrl;
    private int userIdx;
    private String level;
    private String userName;

    private String createdAt;
    private String pictureUrl;


    private String isSaved;
    private int commentCount;
    private int  saveCount;



}

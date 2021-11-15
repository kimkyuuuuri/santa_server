package com.smileflower.santa.src.flags.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostFlagPictureRes {
    private int flagIdx;
    private String status="정복 완료";
    public PostFlagPictureRes(int flagIdx){
        this.flagIdx=flagIdx;
    }
}
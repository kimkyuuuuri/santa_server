package com.smileflower.santa.src.profile.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter



public class PostPictureRes {
    private int pictureIdx;


    public PostPictureRes(int pictureIdx) {
        this.pictureIdx=pictureIdx;
    }
}


package com.smileflower.santa.src.user.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class GetFlagRes {
    private String pictureImgUrl;

    public GetFlagRes(String pictureImgUrl) {
        this.pictureImgUrl=pictureImgUrl;
    }
}

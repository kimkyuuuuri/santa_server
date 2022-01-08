package com.smileflower.santa.src.user.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class GetPictureRes {
    private String pictureImgUrl;
    public GetPictureRes(String pictureImgUrl) {
        this.pictureImgUrl=pictureImgUrl;
    }
}

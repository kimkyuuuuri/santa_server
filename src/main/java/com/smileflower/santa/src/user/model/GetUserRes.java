package com.smileflower.santa.src.user.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class GetUserRes {
    private String pictureImgUrl;
    public GetUserRes(String pictureImgUrl) {
        this.pictureImgUrl=pictureImgUrl;
    }
}

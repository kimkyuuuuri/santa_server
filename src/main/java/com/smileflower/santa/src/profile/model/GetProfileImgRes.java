package com.smileflower.santa.src.profile.model;

import lombok.Getter;
import lombok.Setter;



@Getter
@Setter

public class GetProfileImgRes {

    private String fileUrl;

    public GetProfileImgRes(String fileUrl) {
        this.fileUrl = fileUrl;
    }

}

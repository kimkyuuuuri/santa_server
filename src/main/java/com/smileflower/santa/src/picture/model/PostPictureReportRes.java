package com.smileflower.santa.src.picture.model;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class PostPictureReportRes {
    private Long pictureIdx;
    private int reportCnt;

    public PostPictureReportRes(Long pictureIdx, int reportCnt) {
        this.pictureIdx = pictureIdx;
        this.reportCnt = reportCnt;
    }


}

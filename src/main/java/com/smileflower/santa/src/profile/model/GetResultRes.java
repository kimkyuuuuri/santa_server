package com.smileflower.santa.src.profile.model;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GetResultRes {
    private boolean isFirstFlag;
    private boolean isThirdFlag;
    private boolean isSeventhFlag;
    private boolean isTenthFlag;
    private boolean isFiveHigh;
    private boolean isTenHigh;
    private boolean isTwentyHigh;
    private boolean isMaster;
    private boolean isThirdMountain;
    private boolean isSeventhMountain;
    private boolean isTenthMountain;
    private double high;

    public GetResultRes(boolean isFirstFlag, boolean isThirdFlag, boolean isSeventhFlag, boolean isTenthFlag, boolean isFiveHigh, boolean isTenHigh, boolean isTwentyHigh, boolean isMaster, boolean isThirdMountain, boolean isSeventhMountain, boolean isTenthMountain, double high) {
        this.isFirstFlag = isFirstFlag;
        this.isThirdFlag = isThirdFlag;
        this.isSeventhFlag = isSeventhFlag;
        this.isTenthFlag = isTenthFlag;
        this.isFiveHigh = isFiveHigh;
        this.isTenHigh = isTenHigh;
        this.isTwentyHigh = isTwentyHigh;
        this.isMaster = isMaster;
        this.isThirdMountain = isThirdMountain;
        this.isSeventhMountain = isSeventhMountain;
        this.isTenthMountain = isTenthMountain;
        this.high = high;
    }


}


package com.smileflower.santa.profile.model.dto;

public class ResultResponse {
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

    public ResultResponse(boolean isFirstFlag, boolean isThirdFlag, boolean isSeventhFlag, boolean isTenthFlag, boolean isFiveHigh, boolean isTenHigh, boolean isTwentyHigh, boolean isMaster, boolean isThirdMountain, boolean isSeventhMountain, boolean isTenthMountain,double high) {
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

    public boolean isFirstFlag() {
        return isFirstFlag;
    }

    public void setFirstFlag(boolean firstFlag) {
        isFirstFlag = firstFlag;
    }

    public boolean isThirdFlag() {
        return isThirdFlag;
    }

    public void setThirdFlag(boolean thirdFlag) {
        isThirdFlag = thirdFlag;
    }

    public boolean isSeventhFlag() {
        return isSeventhFlag;
    }

    public void setSeventhFlag(boolean seventhFlag) {
        isSeventhFlag = seventhFlag;
    }

    public boolean isTenthFlag() {
        return isTenthFlag;
    }

    public void setTenthFlag(boolean tenthFlag) {
        isTenthFlag = tenthFlag;
    }

    public boolean isFiveHigh() {
        return isFiveHigh;
    }

    public void setFiveHigh(boolean fiveHigh) {
        isFiveHigh = fiveHigh;
    }

    public boolean isTenHigh() {
        return isTenHigh;
    }

    public void setTenHigh(boolean tenHigh) {
        isTenHigh = tenHigh;
    }

    public boolean isTwentyHigh() {
        return isTwentyHigh;
    }

    public void setTwentyHigh(boolean twentyHigh) {
        isTwentyHigh = twentyHigh;
    }

    public boolean isMaster() {
        return isMaster;
    }

    public void setMaster(boolean master) {
        isMaster = master;
    }

    public boolean isThirdMountain() {
        return isThirdMountain;
    }

    public void setThirdMountain(boolean thirdMountain) {
        isThirdMountain = thirdMountain;
    }

    public boolean isSeventhMountain() {
        return isSeventhMountain;
    }

    public void setSeventhMountain(boolean seventhMountain) {
        isSeventhMountain = seventhMountain;
    }

    public boolean isTenthMountain() {
        return isTenthMountain;
    }

    public void setTenthMountain(boolean tenthMountain) {
        isTenthMountain = tenthMountain;
    }

    public double getHigh() {
        return high;
    }

    public void setHigh(double high) {
        this.high = high;
    }
}


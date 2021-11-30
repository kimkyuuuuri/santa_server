package com.smileflower.santa.flag.repository;

public interface FlagRepository {
    int updateImageUrlByIdx(int userIdx, Long mountainIdx, String filename, Double altitude);

    int findTodayFlagByIdx(int userIdx);

    int findIsFlagByLatAndLong(double latitude, double longitude, Long mountainIdx);
    void updateUserHeight(int userIdx, double height);
    void updateFlagTotalHeight(int userIdx, Long mountainIdx,int flagIdx,double height);
}

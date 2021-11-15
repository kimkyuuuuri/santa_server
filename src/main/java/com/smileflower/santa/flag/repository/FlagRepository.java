package com.smileflower.santa.flag.repository;

public interface FlagRepository {
    int updateImageUrlByIdx(int userIdx, Long mountainIdx, String filename, Double altitude);

    int findTodayFlagByIdx(int userIdx);

    int findIsFlagByLatAndLong(double latitude, double longitude, Long mountainIdx);
}

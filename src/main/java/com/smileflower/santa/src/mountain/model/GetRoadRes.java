package com.smileflower.santa.src.mountain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetRoadRes {
    private int roadIdx;
    private String courseNum;
    private int difficulty;
    private String length;
    private String time;
    private String course;
    private double latitude;
    private double longitude;
}
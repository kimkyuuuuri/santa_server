package com.smileflower.santa.src.mountain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetMountainRes {
    private int mountainIdx;
    private String mountainImg;
    private String mountainName;
    private int difficulty;
    private String high;
    private String hot;
    private String pick;
    private String competing;
}
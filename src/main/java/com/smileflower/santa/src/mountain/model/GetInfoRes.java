package com.smileflower.santa.src.mountain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetInfoRes {
    private int mountainIdx;
    private String mountainImg;
    private String mountainName;
    private String address;
    private int difficulty;
    private String high;
    private String hot;
    private String pick;
    private int altitude;
}
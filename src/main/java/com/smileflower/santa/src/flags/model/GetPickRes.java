package com.smileflower.santa.src.flags.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetPickRes {
    private int mountainIdx;
    private String mountainName;
    private String mountainImg;
    private int difficulty;
    private String high;
    private String hot;
    private String competing;
    private int intTypeHot;
}
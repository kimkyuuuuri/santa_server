package com.smileflower.santa.src.mountain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetMapRes {
    private double latitude;
    private double longitude;
    private int high;
}
package com.smileflower.santa.src.new_home.model;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class GetMountainIdxRes {
    private int mountainIdx;

    private int userIdx;



    public GetMountainIdxRes(int mountainIdx, int userIdx) {
    this.mountainIdx=mountainIdx;

    this.userIdx=userIdx;

       }
}

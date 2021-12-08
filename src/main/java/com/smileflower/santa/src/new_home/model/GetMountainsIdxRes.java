package com.smileflower.santa.src.new_home.model;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class GetMountainsIdxRes {
    private int mountainIdx;

    private int userIdx;



    public GetMountainsIdxRes(int mountainIdx,  int userIdx) {
    this.mountainIdx=mountainIdx;

    this.userIdx=userIdx;

       }
}

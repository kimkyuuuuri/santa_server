package com.smileflower.santa.src.flag.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class GetFlagRankRes {
    private GetRankRes firstRank;
    private GetRankRes myRank;
    public GetFlagRankRes(){
    }
}
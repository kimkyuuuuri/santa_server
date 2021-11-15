package com.smileflower.santa.src.flags.model;

import lombok.AllArgsConstructor;
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
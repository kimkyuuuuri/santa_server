package com.smileflower.santa.src.mountain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter

public class GetMountainRankRes {
    private List<GetRankRes> allRank = new ArrayList<>();
    private GetRankRes myRank;
    public GetMountainRankRes(){
    }
}
package com.smileflower.santa.src.mountain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter

public class GetMapRoadRes {
    private GetMapRes mountain;
    private List<GetRoadRes> road = new ArrayList<>();
    public GetMapRoadRes(){
    }
}
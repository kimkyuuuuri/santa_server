package com.smileflower.santa.src.home.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter

public class GetmyflagMountainRes {
    private List<GetHomeMountainRes> mountain = new ArrayList<>();
    public GetmyflagMountainRes(){
    }
}
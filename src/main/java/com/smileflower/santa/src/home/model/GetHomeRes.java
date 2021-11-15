package com.smileflower.santa.src.home.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter

public class GetHomeRes {
    private int userIdx;
    private String userImage= new String();
    private String homeStatus = new String();
    private GetmyflagMountainRes myflag;
    public GetHomeRes(){
    }
}
package com.smileflower.santa.src.new_home.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter

public class GetHomeRes {
    private String notice;
    private String isFirst;
    private List<GetFlagRes> getFlagResList;
    private List<GetUserRes> getUsersResList;
    private List<GetMountainRes> getMountainsResList;

    public GetHomeRes(String notice, String isFirst, List<GetFlagRes> getFlagResList, List<GetUserRes> getUsersResList, List<GetMountainRes> getMountainsResList) {
        this.notice=notice;
        this.isFirst=isFirst;
        this.getFlagResList=getFlagResList;
        this.getUsersResList=getUsersResList;
        this.getMountainsResList=getMountainsResList;
    }
}
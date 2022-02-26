package com.smileflower.santa.src.new_home.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter

public class GetHomeRes {
    private String notice;
    private String isFirst;
    private List<GetFlagsRes> getFlagResList;
    private List<GetUsersRes> getUsersResList;
    private List<GetMountainsRes> getMountainsResList;

    public GetHomeRes(String notice, String isFirst, List<GetFlagsRes> getFlagResList, List<GetUsersRes> getUsersResList, List<GetMountainsRes> getMountainsResList) {
        this.notice=notice;
        this.getFlagResList=getFlagResList;
        this.getUsersResList=getUsersResList;
        this.getMountainsResList=getMountainsResList;
    }
}
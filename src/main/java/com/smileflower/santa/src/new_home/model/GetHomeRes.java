package com.smileflower.santa.src.new_home.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter

public class GetHomeRes {
    private String notice;
    private List<GetPicturesRes> getPicturesResList;
    private List<GetUsersRes> getUsersResList;
    private List<GetMountainsRes> getMountainsResList;

    public GetHomeRes(String notice,List<GetPicturesRes> getPicturesResList, List<GetUsersRes> getUsersResList, List<GetMountainsRes> getMountainsResList) {
        this.notice=notice;
        this.getPicturesResList=getPicturesResList;
        this.getUsersResList=getUsersResList;
        this.getMountainsResList=getMountainsResList;
    }
}
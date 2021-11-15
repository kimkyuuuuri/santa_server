package com.smileflower.santa.profile.model.dto;

import java.util.List;

public class FlagsResponse {
    private int userIdx;
    private String name;
    private List<FlagResponse> flags;

    public FlagsResponse(int userIdx, String name, List<FlagResponse> flags) {
        this.userIdx = userIdx;
        this.name = name;
        this.flags = flags;
    }

    public int getUserIdx() {
        return userIdx;
    }

    public void setUserIdx(int userIdx) {
        this.userIdx = userIdx;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<FlagResponse> getFlags() {
        return flags;
    }

    public void setFlags(List<FlagResponse> flags) {
        this.flags = flags;
    }
}

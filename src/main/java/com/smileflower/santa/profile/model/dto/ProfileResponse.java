package com.smileflower.santa.profile.model.dto;

import com.smileflower.santa.profile.model.domain.Picture;

import java.util.List;

public class ProfileResponse {
    private int userIdx;
    private String name;
    private int level;
    private int flagCount;
    private int postCount;
    private String fileUrl;
    private List<ProfilePostsResponse> posts;

    public ProfileResponse(int userIdx, String name, int level, int flagCount, int postCount,String fileUrl, List<ProfilePostsResponse> posts) {
        this.userIdx = userIdx;
        this.name = name;
        this.level = level;
        this.flagCount = flagCount;
        this.postCount = postCount;
        this.fileUrl = fileUrl;
        this.posts = posts;
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

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getFlagCount() {
        return flagCount;
    }

    public void setFlagCount(int flagCount) {
        this.flagCount = flagCount;
    }

    public int getPostCount() {
        return postCount;
    }

    public void setPostCount(int postCount) {
        this.postCount = postCount;
    }

    public List<ProfilePostsResponse> getPosts() {
        return posts;
    }

    public void setPosts(List<ProfilePostsResponse> posts) {
        this.posts = posts;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }
}

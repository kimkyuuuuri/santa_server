package com.smileflower.santa.profile.model.dto;

import java.util.List;

public class PostsResponse {
    private int userIdx;
    private String name;
    private List<ProfilePostsResponse> posts;

    public PostsResponse(int userIdx, String name, List<ProfilePostsResponse> posts) {
        this.userIdx = userIdx;
        this.name = name;
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

    public List<ProfilePostsResponse> getPosts() {
        return posts;
    }

    public void setPosts(List<ProfilePostsResponse> posts) {
        this.posts = posts;
    }
}


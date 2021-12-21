package com.smileflower.santa.src.profile.model;



import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter

public class GetProfileRes {
    private int userIdx;
    private String name;
    private int level;
    private int flagCount;
    private int postCount;
    private String fileUrl;
    private List<GetPostsRes> postsResList;

    public GetProfileRes(int userIdx, String name, int level, int flagCount, int postCount, String fileUrl, List<GetPostsRes> postsResList) {
        this.userIdx = userIdx;
        this.name = name;
        this.level = level;
        this.flagCount = flagCount;
        this.postCount = postCount;
        this.fileUrl = fileUrl;
        this.postsResList = postsResList;
    }


}

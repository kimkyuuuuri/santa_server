package com.smileflower.santa.src.flags.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetRankRes {
    private int userIdx;
    private String userName;
    private String userImage;
    private int ranking;
    private String level;
    private int flagCount;
}
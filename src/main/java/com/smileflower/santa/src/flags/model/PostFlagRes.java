package com.smileflower.santa.src.flags.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostFlagRes {
    private boolean isFlag;
    private boolean isDoubleVisited;
    private String fileUrl;

}
package com.smileflower.santa.src.flag.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostFlagHardReq {
    private String Status;
    private int difficulty;
}
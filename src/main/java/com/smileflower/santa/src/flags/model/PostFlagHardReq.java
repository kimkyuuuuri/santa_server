package com.smileflower.santa.src.flags.model;

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
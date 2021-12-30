package com.smileflower.santa.src.flags.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeleteFlagRes {
    private boolean isSuccess;


    public DeleteFlagRes(boolean isSuccess) {
        this.isSuccess=isSuccess;
    }
}

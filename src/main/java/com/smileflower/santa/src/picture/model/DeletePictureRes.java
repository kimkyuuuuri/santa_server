package com.smileflower.santa.src.picture.model;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeletePictureRes {
    private boolean isSuccess;

    public DeletePictureRes(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

}

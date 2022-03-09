package com.smileflower.santa.src.profile.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter


@AllArgsConstructor
public class GetPicturesRes {
    private  Long pictureIdx;
    private String userImageUrl;
    private int userIdx;
    private String level;
    private String userName;
    private String imageUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String status;

    //Constructor

}

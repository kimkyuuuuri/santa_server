package com.smileflower.santa.src.new_home.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetNotificationRes {
    private int notificationIdx;
    private int flagIdx;
    private int pictureIdx;
    private String contents;
    private String type;
    private String status;
    private String createdAt;





}
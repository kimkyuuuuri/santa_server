package com.smileflower.santa.profile.model.domain;

import java.time.LocalDateTime;

public class Profile {

    //Member Field
    private final int userIdx;
    private Email emailId;
    private String passwd;
    private int isKakao;
    private int isApple;
    private String userImageUrl;
    private LocalDateTime updateAt;
    private LocalDateTime createAt;
    private String name;

    //Constructor
    public Profile(int userIdx, Email emailId, String passwd, String userImageUrl, LocalDateTime updateAt, LocalDateTime createAt, String name) {
        this.userIdx = userIdx;
        this.emailId = emailId;
        this.passwd = passwd;
        this.isKakao = 0;
        this.isApple = 0;
        this.userImageUrl = userImageUrl;
        this.updateAt = null;
        this.createAt = null;
        this.name = name;
    }

    public Profile(Email emailId, String passwd, String name){
        this(0,emailId,passwd,"url",null,null,name);
    }

    //Getter
    public int getUserIdx() {
        return userIdx;
    }

    public int isKakao() {
        return isKakao;
    }

    public int isApple() {
        return isApple;
    }

    public String getUserImageUrl() {
        return userImageUrl;
    }

    public String getName() {
        return name;
    }

    public Email getEmailId() {
        return emailId;
    }

    public String getPasswd() {
        return passwd;
    }

    public LocalDateTime getUpdateAt() {
        return updateAt;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }
}

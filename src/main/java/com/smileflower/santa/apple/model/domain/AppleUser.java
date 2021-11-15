package com.smileflower.santa.apple.model.domain;


import java.time.LocalDateTime;

public class AppleUser {
    //Member Field
    private Email emailId;
    private String passwd;
    private int isKakao;
    private int isApple;
    private String userImageUrl;
    private LocalDateTime updateAt;
    private LocalDateTime createAt;
    private String status;
    private String name;

    //Constructor
    public AppleUser(Email emailId, String passwd, String userImageUrl, LocalDateTime updateAt, LocalDateTime createAt, String status, String name) {
        this.emailId = emailId;
        this.passwd = passwd;
        this.isKakao = 0;
        this.isApple = 1;
        this.userImageUrl = userImageUrl;
        this.updateAt = null;
        this.createAt = null;
        this.status = status;
        this.name = name;
    }

    public AppleUser(Email emailId, String name){
        this(emailId,null,null,null,null,null,name);
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

    public int getIsKakao() {
        return isKakao;
    }

    public int getIsApple() {
        return isApple;
    }

    public String getStatus() {
        return status;
    }
}

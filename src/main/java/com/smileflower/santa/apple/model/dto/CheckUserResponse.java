package com.smileflower.santa.apple.model.dto;


import com.smileflower.santa.apple.model.domain.Email;

public class CheckUserResponse {
    private Email userEmail;
    private boolean isUser;

    public CheckUserResponse(Email userEmail, boolean isUser) {
        this.userEmail = userEmail;
        this.isUser = isUser;
    }

    public Email getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(Email userEmail) {
        this.userEmail = userEmail;
    }

    public boolean getIsUser() {
        return isUser;
    }

    public void setIsUser(boolean isUser) {
        this.isUser = isUser;
    }
}

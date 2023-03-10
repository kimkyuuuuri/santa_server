package com.smileflower.santa.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PatchUserStatusReq {
    private String emailId;
    private String password;
    private String name;

    PatchUserStatusReq() {

    }

}
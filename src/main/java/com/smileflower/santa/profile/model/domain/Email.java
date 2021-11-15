package com.smileflower.santa.profile.model.domain;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Email {
    private final String email;
    private final String id;
    private final String address;


    //Constructor
    public Email(String email){
        //check Email Validation
        if(!checkEmailFormat(email)){
            throw new IllegalArgumentException();
        }
        this.email = email;

        //generate Id and Address
        String[] splitStr = email.split("@");
        this.id = splitStr[0];
        this.address = splitStr[1];
    }

    public boolean checkEmailFormat(String email){
        String regex = "^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(email);
        boolean isCheck = m.matches();
        return isCheck;
    }

    public String getEmail() {
        return email;
    }

    public String getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }
}
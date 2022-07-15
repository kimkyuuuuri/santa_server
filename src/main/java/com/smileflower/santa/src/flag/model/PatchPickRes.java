package com.smileflower.santa.src.flag.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class PatchPickRes {
    private int scrapIdx;
    private int userIdx;
    private int mountainIdx;
    private String status;
    private String result;
    public PatchPickRes(int scrapIdx,int userIdx,int mountainIdx,String status){
        this.scrapIdx=scrapIdx;
        this.userIdx=userIdx;
        this.mountainIdx=mountainIdx;
        this.status=status;
        this.result="상태가 "+status+"로 변경되었습니다.";
    }
}
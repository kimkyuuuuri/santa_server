package com.smileflower.santa.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class PatchUserLogoutRes {
    private int idx;
    private int userIdx;
    private String status;
    private String name;
    private String resultMes="아";
    public PatchUserLogoutRes(int idx,int userIdx,String status,String name){
        this.idx=idx;
        this.userIdx=userIdx;
        this.status=status;
        this.name=name;
        this.resultMes= name+" 사용자가 로그아웃되었습니다.";
    }
}
/*
rs.getInt("idx"),
                        rs.getInt("userIdx"),
                        rs.getString("status")
 */

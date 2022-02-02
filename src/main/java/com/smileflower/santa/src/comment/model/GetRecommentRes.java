package com.smileflower.santa.src.comment.model;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor(access=AccessLevel.PUBLIC)
@AllArgsConstructor
public class GetRecommentRes {
    private int userIdx;
    private String userImageUrl;
    private String userName;
    private String isUsersComment;
    private int recommentIdx;
    private String contents;
    private String status;
    private String createdAt;




    public GetRecommentRes(int userIdx, String userImageUrl, String userName, int recommentIdx, String contents, String status, String createdAt) {
    this.userIdx=userIdx;
    this.userImageUrl=userImageUrl;
    this.userName=userName;
    this.recommentIdx=recommentIdx;
    this.contents=contents;
    this.status=status;
    this.createdAt=createdAt;
    this.isUsersComment="f";
    }
}

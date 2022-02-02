package com.smileflower.santa.src.comment.model;
import lombok.*;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor(access=AccessLevel.PUBLIC)
@AllArgsConstructor
public class GetCommentRes {
    private int userIdx;
    private String userImageUrl;
    private String userName;
    private String isUsersComment;
    private int commentIdx;
    private String contents;
    private String status;
    private String createdAt;
    private List<GetRecommentRes> getRecommentRes;


    public GetCommentRes(int userIdx, String userImageUrl, String userName, int commentIdx, String contents, String status, String createdAt, List<GetRecommentRes> getRecommentRes) {
        this.userIdx=userIdx;
        this.userImageUrl=userImageUrl;
        this.userName=userName;
        this.commentIdx=commentIdx;
        this.contents=contents;
        this.status=status;
        this.createdAt=createdAt;
        this.getRecommentRes=getRecommentRes;
        this.isUsersComment="f";
    }
}

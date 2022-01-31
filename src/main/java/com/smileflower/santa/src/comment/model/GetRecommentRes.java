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
    private int recommentIdx;
    private String contents;
    private String status;
    private String createdAt;


}

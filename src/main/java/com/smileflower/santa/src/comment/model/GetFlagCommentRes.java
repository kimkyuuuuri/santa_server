package com.smileflower.santa.src.comment.model;
import lombok.*;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor(access=AccessLevel.PUBLIC)
@AllArgsConstructor
public class GetFlagCommentRes {
    private int userIdx;
    private String userImageUrl;
    private String userName;
    private int commentIdx;
    private String contents;
    private String createdAt;
    private List<GetFlagRecommentRes> getFlagRecommentRes;

}

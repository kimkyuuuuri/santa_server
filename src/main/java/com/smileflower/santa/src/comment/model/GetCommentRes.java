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
    private int commentIdx;
    private String contents;
    private String status;
    private String createdAt;
    private List<GetRecommentRes> getRecommentRes;


}

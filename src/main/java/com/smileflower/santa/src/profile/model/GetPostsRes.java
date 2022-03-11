package com.smileflower.santa.src.profile.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
public class GetPostsRes implements Comparable<GetPostsRes>{
    private boolean isFlag;
    private Long flagIdx;
    private Long pictureIdx;
    private String userImageUrl;
    private int userIdx;
    private String level;
    private String userName;


    private String createdAt;
    private String pictureUrl;
    private String isSaved;
    private int commentCount;
    private int  saveCount;
    private List<GetCommentRes> getCommentRes;

    public GetPostsRes(boolean isFlag, Long flagIdx, Long pictureIdx, String imgUrl, int userIdx, String level, String userName, String createdAt, String fileUrl) {
        this.isFlag = isFlag;
        this.flagIdx = flagIdx;
        this.userImageUrl = imgUrl;
        this.pictureIdx = pictureIdx;
        this.userIdx = userIdx;
        this.level = level;
        this.userName = userName;


        this.createdAt = createdAt;
        this.pictureUrl = fileUrl;

    }


    @Override
    public int compareTo(@NotNull GetPostsRes o) {
        LocalDateTime l1 = LocalDateTime.parse(o.getCreatedAt(),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime l2 = LocalDateTime.parse(this.getCreatedAt(),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        if(l1.isAfter(l2)){
            return 1;
        }
        else if(l1.isEqual(l2)){
            return 0;
        }
        else{
            return -1;
        }
    }

}
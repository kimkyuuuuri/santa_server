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
    private int userIdx;
    private int flagCount;
    private Long mountainIdx;
    private String name;
    private String createdAt;
    private String pictureUrl;
    private List<GetCommentRes> getCommentRes;

    public GetPostsRes(boolean isFlag, Long flagIdx, Long pictureIdx, int userIdx, int flagCount, Long mountainIdx, String name, String format, String fileUrl) {
    this.isFlag=isFlag;
    this.flagIdx=flagIdx;
    this.pictureIdx=pictureIdx;
    this.userIdx=userIdx;
    this.flagCount=flagCount;
    this.mountainIdx=mountainIdx;
    this.name=name;
    this.createdAt=format;
    this.pictureUrl=fileUrl;

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
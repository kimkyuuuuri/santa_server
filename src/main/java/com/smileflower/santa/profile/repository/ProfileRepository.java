package com.smileflower.santa.profile.repository;

import com.smileflower.santa.profile.model.domain.Email;
import com.smileflower.santa.profile.model.domain.Flag;
import com.smileflower.santa.profile.model.domain.Picture;
import com.smileflower.santa.profile.model.domain.Profile;
import com.smileflower.santa.profile.model.dto.FlagResponse;
import com.smileflower.santa.profile.model.dto.FlagsForMapResponse;

import java.util.List;
import java.util.Optional;

public interface ProfileRepository {
    //단일 유저 조회
    Optional<Profile> findByEmail(String email);
    Optional<Profile> findByIdx(int userIdx);
    //닉네임 조회
    String findNameByIdx(int userIdx);
    //게시물(정복사진,그냥사진) 조회
    List<FlagResponse> findFlagsByIdx(int userIdx);
    List<Picture> findPicturesByIdx(int userIdx);

    List<FlagsForMapResponse> findFlagsForMapByIdx(int userIdx);

    int findDiffFlagCountByIdx(int userIdx);

    //정복 횟수
    int findFlagCountByIdx(int userIdx);
    //사진 올리기
    int createPicture(int userIdx,String imageUrl);

    int findHighSumByIdx(int userIdx);

    //게시물 삭제
    boolean deleteFlagByIdx(Long flagIdx);
    boolean deletePictureByIdx(Long flagIdx);
    //정복사진 신고
    Long report(Long flagIdx, int userIdx);
    //정복사진 신고 카운트
    int reportCountByIdx(Long flagIdx);

    int updateImageUrlByEmail(String email,String filename);

    int updateImageUrlByIdx(int userIdx, String filename);

    int deleteImageUrlByEmail(String email);

    int deleteImageUrlByIdx(int userIdx);
}
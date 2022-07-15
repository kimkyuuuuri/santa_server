package com.smileflower.santa.src.profile;

import com.smileflower.santa.config.BaseException;

import com.smileflower.santa.config.BaseResponseStatus;
import com.smileflower.santa.src.profile.model.*;

import com.smileflower.santa.utils.JwtService;
import com.smileflower.santa.utils.S3Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


//Provider : Read의 비즈니스 로직 처리
@Service
public class New_ProfileProvider {


    private final New_ProfileDao newProfileDao;
    private final JwtService jwtService;
    private final S3Service s3Service;

    private JdbcTemplate jdbcTemplate;
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public New_ProfileProvider(New_ProfileDao newProfileDao, JwtService jwtService, S3Service s3Service) {

        this.newProfileDao = newProfileDao;
        this.jwtService = jwtService;
        this.s3Service = s3Service;
    }

    public GetProfileRes getProfileRes(int userIdx) throws BaseException {
        List<GetPostForProfileRes> getPostsRes = new ArrayList<>();
        List<GetFlagResForProfile> getFlagRes = newProfileDao.getFlagResForProfiles(userIdx);
        List<GetPictureRes> getPicturesRes = newProfileDao.getPicturesRes(userIdx,userIdx);
        GetUserRes getUserRes= newProfileDao.getUserRes(userIdx);
        int flagsResponseCnt = getFlagRes.size();
        int level = 0;
        if (flagsResponseCnt<=2){
            level = flagsResponseCnt;
        }
        else{
            level = (flagsResponseCnt+2)/2;
        }
        int size=getPicturesRes.size();
        for(int i=size-1;i>=0;i--){

            if(getPicturesRes.get(i).getImageUrl()!=null)
                getPicturesRes.get(i).setUserImageUrl(s3Service.getFileUrl(getPicturesRes.get(i).getImageUrl()));

            if (getPicturesRes.get(i).getUserImageUrl() != null)
                      getPostsRes.add(new GetPostForProfileRes(false,null,getPicturesRes.get(i).getPictureIdx(),s3Service.getFileUrl(getPicturesRes.get(i).getUserImageUrl()),getPicturesRes.get(i).getUserIdx(),getPicturesRes.get(i).getLevel(),getPicturesRes.get(i).getUserName(),0,null,null,getPicturesRes.get(i).getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),s3Service.getFileUrl(getPicturesRes.get(i).getImageUrl())));
    else
                getPostsRes.add(new GetPostForProfileRes(false,null,getPicturesRes.get(i).getPictureIdx(),getPicturesRes.get(i).getUserImageUrl(),getPicturesRes.get(i).getUserIdx(),getPicturesRes.get(i).getLevel(),getPicturesRes.get(i).getUserName(),0,null,null,getPicturesRes.get(i).getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),s3Service.getFileUrl(getPicturesRes.get(i).getImageUrl())));

        }
        size=getFlagRes.size();
        for(int i=size-1;i>=0;i--){
            if(getFlagRes.get(i).getPictureUrl()!=null)
                getFlagRes.get(i).setPictureUrl(s3Service.getFileUrl(getFlagRes.get(i).getPictureUrl()));

            if (getFlagRes.get(i).getUserImageUrl() != null)
                getPostsRes.add(new GetPostForProfileRes(true,getFlagRes.get(i).getFlagIdx(),null,s3Service.getFileUrl(getFlagRes.get(i).getUserImageUrl()),getFlagRes.get(i).getUserIdx(),getFlagRes.get(i).getLevel(),getFlagRes.get(i).getUserName(),getFlagRes.get(i).getFlagCount(),getFlagRes.get(i).getMountainIdx(),getFlagRes.get(i).getName(),getFlagRes.get(i).getCreatedAt(),getFlagRes.get(i).getPictureUrl()));
        else
                getPostsRes.add(new GetPostForProfileRes(true,getFlagRes.get(i).getFlagIdx(),null,getFlagRes.get(i).getUserImageUrl(),getFlagRes.get(i).getUserIdx(),getFlagRes.get(i).getLevel(),getFlagRes.get(i).getUserName(),getFlagRes.get(i).getFlagCount(),getFlagRes.get(i).getMountainIdx(),getFlagRes.get(i).getName(),getFlagRes.get(i).getCreatedAt(),getFlagRes.get(i).getPictureUrl()));

        }

        Collections.sort(getPostsRes);

        String userImg=null;
        if(getUserRes.getUserImageUrl()!=null) {
            userImg=s3Service.getFileUrl(getUserRes.getUserImageUrl());
        }

        GetProfileRes getProfileRes = new GetProfileRes(userIdx,getUserRes.getUserName(),level,flagsResponseCnt,flagsResponseCnt+getPicturesRes.size(),userImg,getPostsRes);

        return getProfileRes;

    }


    public GetMyPostRes getMyPostsRes(int userIdx, int userIdxByJwt)  throws BaseException{
        String isMyPost="T";

        if(userExist(userIdx)==0) {
            throw new BaseException(BaseResponseStatus.INVALID_USER);
        }
        if(userExist(userIdxByJwt)==0) {
            throw new BaseException(BaseResponseStatus.INVALID_USER);
        }
        if(userIdx!=userIdxByJwt)
        {
            isMyPost="F";
        }

        List<GetPostRes> getPostsRes = new ArrayList<>();
        List<GetFlagRes> getFlagRes = newProfileDao.getFlagsRes(userIdx,userIdxByJwt);
        List<GetPictureRes> getPicturesRes =newProfileDao.getPicturesRes(userIdx,userIdxByJwt);
        GetUserRes getUserRes= newProfileDao.getUserRes(userIdx);
        for(int i=0;i<getPicturesRes.size();i++){
            if (getPicturesRes.get(i).getUserImageUrl() != null)
                getPicturesRes.get(i).setUserImageUrl(s3Service.getFileUrl(getPicturesRes.get(i).getUserImageUrl()));

            List<GetCommentRes> getCommentRes=newProfileDao.getPictureCommentsRes(getPicturesRes.get(i).getPictureIdx());
           for(int j=0;j<getCommentRes.size();j++){

                   if (getCommentRes.get(j).getUserImageUrl() != null)
                       getCommentRes.get(j).setUserImageUrl(s3Service.getFileUrl(getCommentRes.get(j).getUserImageUrl()));

               }
            getPostsRes.add(new GetPostRes(false,null,getPicturesRes.get(i).getPictureIdx(),getPicturesRes.get(i).getUserImageUrl(),getPicturesRes.get(i).getUserIdx(),getPicturesRes.get(i).getLevel(),getPicturesRes.get(i).getUserName(),getPicturesRes.get(i).getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),s3Service.getFileUrl(getPicturesRes.get(i).getImageUrl()),getPicturesRes.get(i).getIsSaved(),getPicturesRes.get(i).getCommentCount(),getPicturesRes.get(i).getSaveCount(),getCommentRes));
        }

        for(int i=0;i<getFlagRes.size();i++){
            if (getFlagRes.get(i).getUserImageUrl() != null)
                getFlagRes.get(i).setUserImageUrl(s3Service.getFileUrl(getFlagRes.get(i).getUserImageUrl()));

            List<GetCommentRes> getCommentRes=newProfileDao.getFlagCommentsRes(getFlagRes.get(i).getFlagIdx());
            for(int j=0;j<getCommentRes.size();j++) {
                if (getCommentRes.get(j).getUserImageUrl() != null)
                    getCommentRes.get(j).setUserImageUrl(s3Service.getFileUrl(getCommentRes.get(j).getUserImageUrl()));
            }
            getPostsRes.add(new GetPostRes(true,getFlagRes.get(i).getFlagIdx(),null,getFlagRes.get(i).getUserImageUrl(),getFlagRes.get(i).getUserIdx(),getFlagRes.get(i).getLevel(),getFlagRes.get(i).getUserName(),getFlagRes.get(i).getCreatedAt(),s3Service.getFileUrl(getFlagRes.get(i).getPictureUrl()),getFlagRes.get(i).getIsSaved(),getFlagRes.get(i).getCommentCount(),getFlagRes.get(i).getSaveCount(),getCommentRes));

        }
        Collections.sort(getPostsRes);

        return new GetMyPostRes(isMyPost,userIdx,getUserRes.getUserName(),getPostsRes);

    }

    public List<GetMapRes> getMapsRes(int userIdx) {
        List<GetMapRes> getMapRes = newProfileDao.getMapsRes(userIdx);
        for(int i=0;i<getMapRes.size();i++){
            if(getMapRes.get(i).getImageUrl()!=null)
                getMapRes.get(i).setImageUrl(s3Service.getFileUrl(getMapRes.get(i).getImageUrl()));
        }
        return getMapRes;
    }


    public GetListRes getListRes(int userIdx, int order) {

        GetFlagCountRes getFlagCountRes =newProfileDao.getFlagCounts(userIdx);
        List<GetMountainRes> getMountainsRes=newProfileDao.getMountainsForListRes(userIdx,order);


       for(int i=0;i<getMountainsRes.size();i++){
            if(getMountainsRes.get(i).getMountainImageUrl()!=null)
                getMountainsRes.get(i).setMountainImageUrl(s3Service.getFileUrl(getMountainsRes.get(i).getMountainImageUrl()));
        }
        GetListRes getListRes = new GetListRes(getFlagCountRes,getMountainsRes);


        return getListRes;
    }

    public GetProfileImgRes getProfileImgRes(int userIdx){
        //delete file
        GetUserRes getUserRes= newProfileDao.getUserRes(userIdx);


        if(getUserRes.getUserImageUrl()!=null) {
            return new GetProfileImgRes(s3Service.getFileUrl(getUserRes.getUserImageUrl()));
        }
        else
            return new GetProfileImgRes(null);
    }

    public GetResultRes getResultRes(int userIdx) {
        int flagCount = newProfileDao.getFlagCount(userIdx);
        int diffFlagCount = newProfileDao.getDiffFlagCount(userIdx);
        int highCount = newProfileDao.getHighSum(userIdx);
        return new GetResultRes(flagCount>0,flagCount>2,flagCount>6,flagCount>9,
                highCount>4999, highCount>9999,highCount>19999,diffFlagCount>99,
                diffFlagCount>2,diffFlagCount>6,diffFlagCount>9,(double)highCount/1000);
    }

    public int userExist(int userIdx){
        int exist = newProfileDao.checkUserExist(userIdx);
        return exist;
    }

    public int getFlagCount(int userIdx){
        int flagCount = newProfileDao.getFlagCount(userIdx);
        return flagCount;
    }

    public GetUserLoginInfoRes getUserLoginInfo(int userIdx) throws BaseException {
        GetUserLoginInfoRes getUserLoginInfoRes = newProfileDao.getUserLoginInfoRes(userIdx);
        return getUserLoginInfoRes;
    }
}
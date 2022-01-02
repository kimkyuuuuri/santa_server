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
        List<GetPostsRes> getPostsRes = new ArrayList<>();
        List<GetFlagRes> getFlagRes = newProfileDao.getFlagRes(userIdx);
        List<GetPicturesRes> getPicturesRes = newProfileDao.getPicturesRes(userIdx);
        GetUserRes getUserRes= newProfileDao.getUserRes(userIdx);
        int flagsResponseCnt = getFlagRes.size();
        int level = 0;
        if (flagsResponseCnt<=2){
            level = flagsResponseCnt;
        }
        else{
            level = (flagsResponseCnt+2)/2;
        }

        for(int i=0;i<getPicturesRes.size();i++){
            getPostsRes.add(new GetPostsRes(false,null,getPicturesRes.get(i).getPictureIdx(),getPicturesRes.get(i).getUserIdx(),0,null,null,getPicturesRes.get(i).getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),s3Service.getFileUrl(getPicturesRes.get(i).getImageUrl())));
        }
        for(int i=0;i<getFlagRes.size();i++){
            getPostsRes.add(new GetPostsRes(true,getFlagRes.get(i).getFlagIdx(),null,getFlagRes.get(i).getUserIdx(),getFlagRes.get(i).getFlagCount(),getFlagRes.get(i).getMountainIdx(),getFlagRes.get(i).getName(),getFlagRes.get(i).getCreatedAt(),s3Service.getFileUrl(getFlagRes.get(i).getPictureUrl())));
        }
        Collections.sort(getPostsRes);

        GetProfileRes getProfileRes = new GetProfileRes(userIdx,getUserRes.getUserName(),level,flagsResponseCnt,flagsResponseCnt+getPicturesRes.size(),s3Service.getFileUrl(getUserRes.getUserImageUrl()),getPostsRes);

        return getProfileRes;

    }
    public GetMyPostsRes getMyPostsRes(int userIdx)  throws BaseException{
        if(userExist(userIdx)==0) {
            throw new BaseException(BaseResponseStatus.INVALID_USER);
        }
        List<GetPostsRes> getPostsRes = new ArrayList<>();
        List<GetFlagRes> getFlagRes = newProfileDao.getFlagRes(userIdx);
        List<GetPicturesRes> getPicturesRes =newProfileDao.getPicturesRes(userIdx);
        GetUserRes getUserRes= newProfileDao.getUserRes(userIdx);
        for(int i=0;i<getPicturesRes.size();i++){
            getPostsRes.add(new GetPostsRes(false,null,getPicturesRes.get(i).getPictureIdx(),getPicturesRes.get(i).getUserIdx(),0,null,null,getPicturesRes.get(i).getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),s3Service.getFileUrl(getPicturesRes.get(i).getImageUrl())));
        }
        for(int i=0;i<getFlagRes.size();i++){
            getPostsRes.add(new GetPostsRes(true,getFlagRes.get(i).getFlagIdx(),null,getFlagRes.get(i).getUserIdx(),getFlagRes.get(i).getFlagCount(),getFlagRes.get(i).getMountainIdx(),getFlagRes.get(i).getName(),getFlagRes.get(i).getCreatedAt(),s3Service.getFileUrl(getFlagRes.get(i).getPictureUrl())));
        }
        Collections.sort(getPostsRes);

        return new GetMyPostsRes(userIdx,getUserRes.getUserName(),getPostsRes);

    }

    public List<GetMapRes> getMapRes(int userIdx) {
        List<GetMapRes> getMapRes = newProfileDao.getMapRes(userIdx);
        for(int i=0;i<getMapRes.size();i++){
            if(getMapRes.get(i).getImageUrl()!=null)
                getMapRes.get(i).setImageUrl(s3Service.getFileUrl(getMapRes.get(i).getImageUrl()));
        }
        return getMapRes;
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
}
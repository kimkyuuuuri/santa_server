package com.smileflower.santa.src.profile;


import com.smileflower.santa.config.BaseException;
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



}
package com.smileflower.santa.src.picture;

import com.smileflower.santa.src.picture.model.*;
import com.smileflower.santa.utils.JwtService;
import com.smileflower.santa.utils.S3Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;


@Service
public class PictureProvider {

    private final PictureDao pictureDao;
    private final JwtService jwtService;
    private final S3Service s3Service;

    private JdbcTemplate jdbcTemplate;
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    public PictureProvider(PictureDao pictureDao, JwtService jwtService, S3Service s3Service) {

        this.pictureDao = pictureDao;
        this.jwtService = jwtService;
        this.s3Service = s3Service;
    }
    public int checkSaveExist(int userIdx,int pictureIdx){
        int exist = pictureDao.checkSaveExist(userIdx,pictureIdx);
        return exist;
    }
    public int checkPictureExist(long pictureIdx){
        int exist = pictureDao.checkPictureExist(pictureIdx);
        return exist;
    }

    public int checkPictureWhereUserExist(Long pictureIdx,int userIdx){
        return pictureDao.checkPictureWhereUserExist(pictureIdx,userIdx);
    }
    public int checkJwt(String jwt) {
        int exist = pictureDao.checkJwt(jwt);
        return exist;
    }
    public int checkPictureReportExist(Long flagIdx, int userIdx){
        return pictureDao.checkPictureReportExist(flagIdx,userIdx);
    }
    public String getPicturePushToken(int pictureIdx){
        return pictureDao.getUserPicturePushToken(pictureIdx);
    }

    public int getUserIdxByPicture(int pictureIdx){
        return pictureDao.getUserIdxByPicture(pictureIdx);
    }

    public GetUserInfoRes getUserName(int userIdx){
        return pictureDao.getUserName(userIdx);
    }

}
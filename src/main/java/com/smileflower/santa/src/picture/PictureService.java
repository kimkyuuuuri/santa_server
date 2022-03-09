package com.smileflower.santa.src.picture;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.smileflower.santa.config.BaseException;
import com.smileflower.santa.src.picture.model.*;
import com.smileflower.santa.utils.FcmPush;
import com.smileflower.santa.utils.JwtService;
import com.smileflower.santa.utils.S3Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

import static com.smileflower.santa.config.BaseResponseStatus.*;


@Service
public class PictureService{
    private final PictureProvider pictureProvider;
    private final PictureDao pictureDao;
    private final JwtService jwtService;
    private final S3Service s3Service;
    private final FcmPush fcmPush;

    private JdbcTemplate jdbcTemplate;
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    public PictureService(PictureDao pictureDao, JwtService jwtService, S3Service s3Service, PictureProvider pictureProvider, FcmPush fcmPush) {
        this.pictureProvider=pictureProvider;
        this.pictureDao = pictureDao;
        this.jwtService = jwtService;
        this.s3Service = s3Service;
        this.fcmPush = fcmPush;
    }


    public PostPictureSaveRes postPictureSaveRes(int userIdx,int pictureIdx) throws BaseException, IOException {

        if (pictureProvider.checkPictureExist(pictureIdx)!=1) {
            throw new BaseException(INVALID_POST);
        }
        if (pictureProvider.checkSaveExist(userIdx,pictureIdx)!=1) {
            GetUserInfoRes getUserInfoRes=pictureDao.getUserName(userIdx);
             int pictureSaveIdx =pictureDao.postPictureSaveRes(userIdx, pictureIdx);
            String pushToken= pictureProvider.getPicturePushToken(pictureIdx);
            int userIdxbyPictureIdx=pictureProvider.getUserIdxByPicture(pictureIdx);

            if(userIdxbyPictureIdx!=userIdx){
                GetUserInfoRes getUserInfoResForPush=pictureProvider.getUserName(userIdx);
                GetUserInfoRes getUserInfoResForPush2=pictureProvider.getUserName(userIdxbyPictureIdx);
                pictureDao.createPictureSaveNotification(userIdxbyPictureIdx,pictureIdx);
   // System.out.println(getUserInfoResForPush2.getTokenType());


                if (getUserInfoResForPush2.getTokenType().equals("I")) {

                    fcmPush.iosPush(pushToken, "SANTA", getUserInfoRes.getName() + "님이 회원님의 게시물에 좋아요를 눌렀어요!💚");

                }
                if (getUserInfoResForPush2.getTokenType().equals("A")) {
                    fcmPush.androidPush(pushToken, "SANTA", getUserInfoRes.getName() + "님이 회원님의 게시물에 좋아요를 눌렀어요!💚");
                }
            }
            return  new PostPictureSaveRes(pictureSaveIdx,"좋아요 완료");
        }
        else {

            int pictureSaveIdx =pictureDao.patchPictureSaveRes(userIdx, pictureIdx);

            return  new PostPictureSaveRes(pictureSaveIdx,"좋아요 취소");
        }
    }

    public DeletePictureRes deletePicture(int userIdx,long pictureIdx) throws BaseException{
        if (pictureProvider.checkPictureExist(pictureIdx) == 0)
            throw new BaseException(INVALID_POST);
        else if (pictureProvider.checkPictureWhereUserExist(pictureIdx,userIdx) == 0)
            throw new BaseException(INVALID_POST_USER);
        List<GetPictureCommentIdxRes> getPictureCommentIdxRes=pictureDao.getPictureCommentIdxRes(pictureIdx);
        for(int i=0;i<getPictureCommentIdxRes.size();i++){
            pictureDao.deletePictureRecomment(getPictureCommentIdxRes.get(i).getPicturecommentIdx());
        }
        pictureDao.deletePictureComment(pictureIdx);
        // flagDao.deleteFlagReComment(flagIdx);
        pictureDao.deletePictureSave(pictureIdx);

        pictureDao.deletePictureReport(pictureIdx);
        return new DeletePictureRes(pictureDao.deletePicture(pictureIdx));

    }

    @Transactional
    public PostPictureReportRes report(int userIdx, Long pictureIdx) throws BaseException{
        if (pictureProvider.checkPictureExist(pictureIdx) == 0)
            throw new BaseException(INVALID_POST);

        if (pictureProvider.checkPictureReportExist(pictureIdx,userIdx)==1)
            throw new BaseException(POST_AUTH_EXISTS_REPORT);
        else {

            pictureDao.report(pictureIdx, userIdx);
            return new PostPictureReportRes(pictureIdx, pictureDao.getReportCount(pictureIdx));
        }

    }
}
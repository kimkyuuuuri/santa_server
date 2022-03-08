package com.smileflower.santa.src.flags;


import com.amazonaws.services.s3.model.ObjectMetadata;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.smileflower.santa.config.BaseException;
import com.smileflower.santa.flag.model.*;

import com.smileflower.santa.src.flags.model.*;

import com.smileflower.santa.utils.FcmPush;
import com.smileflower.santa.utils.JwtService;
import com.smileflower.santa.utils.S3Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import javax.sql.DataSource;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

import static com.smileflower.santa.config.BaseResponseStatus.*;

// Service Create, Update, Delete 의 로직 처리
@Service
@Transactional
public class FlagService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final FlagDao flagDao;
    private final FlagProvider flagProvider;
    private final JwtService jwtService;
    private JdbcTemplate jdbcTemplate;
    private final S3Service s3Service;
    private final FcmPush fcmPush;
    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Autowired
    public FlagService(FlagDao flagDao, FlagProvider flagProvider, JwtService jwtService, S3Service s3Service, FcmPush fcmPush) {
        this.flagDao = flagDao;
        this.flagProvider = flagProvider;
        this.jwtService = jwtService;
        this.s3Service=s3Service;

        this.fcmPush = fcmPush;
    }

    public PostFlagPictureRes createFlag(PostFlagPictureReq postFlagPictureReq, int mountainIdx, int userIdx) throws BaseException {
       if (postFlagPictureReq.getPictureUrl().length() > 0) {
            int flagIdx = flagDao.createFlag(postFlagPictureReq, mountainIdx, userIdx);

            return new PostFlagPictureRes(flagIdx);
        } else {
            throw new BaseException(RESPONSE_ERROR);
        }
    }
    public PostFlagHardRes createHard(PostFlagHardReq postFlagHardReq, int mountainIdx, int userIdx) throws BaseException {
        if (postFlagHardReq.getDifficulty() >= 0 && postFlagHardReq.getDifficulty() <= 10) {
            PostFlagHardRes postFlagHardRes = new PostFlagHardRes();
            int difficultyIdx = flagDao.createHard(postFlagHardReq, mountainIdx, userIdx);

            return postFlagHardRes;
        } else {
            throw new BaseException(RESPONSE_ERROR);
        }
    }

    @Transactional
    public PatchPickRes patchPick(int userIdx, int mountainIdx) throws BaseException{
        int exist=flagDao.checkPickExist(userIdx,mountainIdx);
        if(exist==1) {
            char status = flagProvider.checkPick(userIdx, mountainIdx);
            if (status == 'T') {
                PatchPickRes patchPickRes = flagDao.patchPick("F", userIdx, mountainIdx);
                return patchPickRes;
            }
            else{
                PatchPickRes patchPickRes = flagDao.patchPick("T", userIdx,mountainIdx);
                return patchPickRes;
            }
        } else{
            PatchPickRes patchPickRes =flagDao.createPick("T",userIdx,mountainIdx);
            return patchPickRes;
        }

    }

    @Transactional
    public PostFlagReportRes report(int userIdx, Long flagIdx) throws BaseException{
        if (flagProvider.checkFlagExist(flagIdx) == 0)
            throw new BaseException(INVALID_POST);

        if (flagProvider.checkFlagReportExist(flagIdx,userIdx)==1)
            throw new BaseException(POST_AUTH_EXISTS_REPORT);
        else {

            flagDao.report(flagIdx, userIdx);
            return new PostFlagReportRes(flagIdx, flagDao.getReportCount(flagIdx));
        }

    }
    @Transactional
    public PostFlagRes uploadImage(GpsInfoRequest gpsInfoRequest, MultipartFile file, int userIdx, Long mountainIdx)throws BaseException  {

        boolean isDoubleVisited = flagDao.findTodayFlagByIdx(userIdx)!=0;
        boolean isFlag = flagDao.findIsFlagByLatAndLong(gpsInfoRequest.getLatitude(),gpsInfoRequest.getLongitude(),mountainIdx)==1;
       // if (isFlag == false)
            if (isFlag == false)
                    throw new BaseException(INVALID_FLAG_LOCATION);
        //if (isDoubleVisited == true)
         //throw new BaseException(POST_FLAGS_EXISTS_FLAG);
        //if(isFlag && !isDoubleVisited){
            if(true){

            String fileName = createFileName(file.getOriginalFilename());

            ObjectMetadata objectMetadata = new ObjectMetadata();

            objectMetadata.setContentType(file.getContentType());

            try (InputStream inputStream = file.getInputStream()) {
                s3Service.uploadFile(inputStream, objectMetadata, fileName);

                int flagIdx=updateImageUrlByIdx(userIdx, mountainIdx, fileName, gpsInfoRequest.getAltitude());
                updateUserHeight(userIdx, gpsInfoRequest.getAltitude());
                updateFlagTotalHeight(userIdx,mountainIdx, flagIdx,gpsInfoRequest.getAltitude());
                updateFlagTotalCount(userIdx,mountainIdx,flagIdx);

            } catch (IOException e) {
                throw new BaseException(FILE_ERROR);
            }
            return new PostFlagRes(isFlag,isDoubleVisited,s3Service.getFileUrl(fileName));
        }
        else{

            return new PostFlagRes(isFlag,isDoubleVisited,null);
        }

    }

    private int updateImageUrlByIdx(int userIdx, Long mountainIdx, String fileName, Double altitude){
        return flagDao.updateImageUrlByIdx(userIdx,mountainIdx,fileName,altitude);
    }
    private void updateUserHeight(int userIdx,Double altitude){

        flagDao.updateUserHeight(userIdx,altitude);
    }
    private void updateFlagTotalHeight(int userIdx,Long mountainIdx,int flagIdx,Double altitude){

        flagDao.updateFlagTotalHeight(userIdx,mountainIdx,flagIdx,altitude);
    }

    private void updateFlagTotalCount(int userIdx,Long mountainIdx,int flagIdx){

        flagDao.updateFlagTotalCount(userIdx,mountainIdx,flagIdx);
    }

    private String createFileName(String originalFileName)throws BaseException{
        return
                UUID.randomUUID().toString().concat(getFileExtension(originalFileName));
    }
    private String getFileExtension(String fileName) throws BaseException{
        try{
            return fileName.substring(fileName.lastIndexOf("."));
        }catch(StringIndexOutOfBoundsException e){
            throw new BaseException(FILE_ERROR);
        }
    }

    public DeleteFlagRes deleteFlag(Long flagIdx,int userIdx) throws BaseException {
        if (flagProvider.checkFlagExist(flagIdx) == 0)
            throw new BaseException(INVALID_POST);
        else if (flagProvider.checkFlagWhereUserExist(flagIdx,userIdx) == 0)
            throw new BaseException(INVALID_POST_USER);
        List<GetFlagCommentIdxRes> getFlagCommentIdxRes=flagDao.getFlagCommentIdxRes(flagIdx);
        for(int i=0;i<getFlagCommentIdxRes.size();i++){
            flagDao.deleteFlagRecomment(getFlagCommentIdxRes.get(i).getFlagcommentIdx());
        }

        flagDao.deleteFlagSave(flagIdx);
        flagDao.deleteFlagComment(flagIdx);
        flagDao.deleteFlagReport(flagIdx);
        return new DeleteFlagRes(flagDao.deleteFlag(flagIdx));

    }

    public PostFlagSaveRes postFlagSaveRes(int userIdx, int flagIdx) throws BaseException, IOException {
        if (flagProvider.checkFlagExist(flagIdx)!=1) {
            throw new BaseException(INVALID_POST);
        }
        if (flagProvider.checkSaveExist(userIdx,flagIdx)!=1) {

            int flagSaveIdx =flagDao.postFlagSaveRes(userIdx, flagIdx);
            String pushToken= flagProvider.getFlagPushToken(flagIdx);
            int userIdxbyFlagIdx=flagProvider.getUserIdxByFlag(flagIdx);
            GetUserInfoRes getUserInfoRes=flagProvider.getUserName(userIdx);

            if(userIdxbyFlagIdx!=userIdx){

                GetUserInfoRes getUserInfoResForPush=flagProvider.getUserName(userIdx);

                flagDao.createFlagSaveNotification(userIdxbyFlagIdx,flagIdx);

                if (getUserInfoResForPush.getTokenType().equals("I")) {
                    fcmPush.iosPush(pushToken, "SANTA", getUserInfoRes.getName() + "님이 회원님의 게시물에 좋아요를 눌렀어요!💚");

                }
                else if (getUserInfoResForPush.getTokenType().equals("A")) {
                    fcmPush.androidPush(pushToken, "SANTA", getUserInfoRes.getName() + "님이 회원님의 게시물에 좋아요를 눌렀어요!💚");

                }
            }

            return  new PostFlagSaveRes(flagSaveIdx,"좋아요 완료");
        }
        else {

            int flagSaveIdx =flagDao.patchFlagSaveRes(userIdx, flagIdx);

            return  new PostFlagSaveRes(flagSaveIdx,"좋아요 취소");
        }
    }


}
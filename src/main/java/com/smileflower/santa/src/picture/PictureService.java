package com.smileflower.santa.src.picture;


import com.smileflower.santa.config.BaseException;
import com.smileflower.santa.config.BaseResponse;
import com.smileflower.santa.src.flags.model.DeleteFlagRes;
import com.smileflower.santa.src.flags.model.PostFlagReportRes;
import com.smileflower.santa.src.picture.model.*;
import com.smileflower.santa.src.user.model.PatchUserLogoutRes;
import com.smileflower.santa.utils.JwtService;
import com.smileflower.santa.utils.S3Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.smileflower.santa.config.BaseResponseStatus.*;


@Service
public class PictureService{
    private final PictureProvider pictureProvider;
    private final PictureDao pictureDao;
    private final JwtService jwtService;
    private final S3Service s3Service;

    private JdbcTemplate jdbcTemplate;
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    public PictureService(PictureDao pictureDao, JwtService jwtService, S3Service s3Service,PictureProvider pictureProvider) {
        this.pictureProvider=pictureProvider;
        this.pictureDao = pictureDao;
        this.jwtService = jwtService;
        this.s3Service = s3Service;
    }


    public PostPictureSaveRes postPictureSaveRes(int userIdx,int pictureIdx) throws BaseException {

        if (pictureProvider.checkPictureExist(pictureIdx)!=1) {
            throw new BaseException(INVALID_POST);
        }
        if (pictureProvider.checkSaveExist(userIdx,pictureIdx)!=1) {

             int pictureSaveIdx =pictureDao.postPictureSaveRes(userIdx, pictureIdx);

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
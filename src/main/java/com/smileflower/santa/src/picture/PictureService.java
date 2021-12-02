package com.smileflower.santa.src.picture;


import com.smileflower.santa.config.BaseException;
import com.smileflower.santa.src.picture.model.*;
import com.smileflower.santa.src.user.model.PatchUserLogoutRes;
import com.smileflower.santa.utils.JwtService;
import com.smileflower.santa.utils.S3Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import static com.smileflower.santa.config.BaseResponseStatus.ALREADY_LOGOUT;


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
        if (pictureProvider.checkSaveExist(userIdx,pictureIdx)==1) {
        int pictureSaveIdx =pictureDao.postPictureSaveRes(userIdx, pictureIdx);
            return  new PostPictureSaveRes(pictureSaveIdx);
        }

        else {

            int pictureSaveIdx =pictureDao.patchPictureSaveRes(userIdx, pictureIdx);
            return  new PostPictureSaveRes(pictureSaveIdx);
        }
    }

}
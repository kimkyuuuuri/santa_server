package com.smileflower.santa.src.comment;

import com.smileflower.santa.config.BaseException;
import com.smileflower.santa.config.BaseResponseStatus;
import com.smileflower.santa.src.comment.model.*;
import com.smileflower.santa.utils.JwtService;
import com.smileflower.santa.utils.S3Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CommentProvider {

    private final CommentDao commentDao;
    private final JwtService jwtService;
    private final S3Service s3Service;

    private JdbcTemplate jdbcTemplate;
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    public CommentProvider(CommentDao commentDao, JwtService jwtService, S3Service s3Service) {

        this.commentDao = commentDao;
        this.jwtService = jwtService;
        this.s3Service = s3Service;
    }
    public int checkSaveExist(int userIdx,int pictureIdx){
        int exist = commentDao.checkSaveExist(userIdx,pictureIdx);
        return exist;
    }
    public int checkPictureExist(long pictureIdx){
        int exist = commentDao.checkPictureExist(pictureIdx);
        return exist;
    }

    public int checkPictureWhereUserExist(Long pictureIdx,int userIdx){
        return commentDao.checkPictureWhereUserExist(pictureIdx,userIdx);
    }
    public int checkJwt(String jwt) {
        int exist = commentDao.checkJwt(jwt);
        return exist;
    }

/*
    public List<GetFlagCommentRes> getFlagComment(int flagIdx) throws BaseException {
        List<GetFlagCommentRes> getFlagsMoreRes = commentDao.getFlagComment(flagIdx);


        if(getFlagsMoreRes.size()==0)
            throw new BaseException(BaseResponseStatus.EMPTY_PICTURE);

        for (int i = 0; i < getFlagsMoreRes.size(); i++) {
            if (getFlagsMoreRes.get(i).getFlagImageUrl() != null)
                getFlagsMoreRes.get(i).setFlagImageUrl(s3Service.getFileUrl(getFlagsMoreRes.get(i).getFlagImageUrl()));
            if (getFlagsMoreRes.get(i).getUserImageUrl() != null)
                getFlagsMoreRes.get(i).setUserImageUrl(s3Service.getFileUrl(getFlagsMoreRes.get(i).getUserImageUrl()));
            if (getFlagsMoreRes.get(i).getGetCommentRes().size()!= 0)
                getFlagsMoreRes.get(i).getGetCommentRes().get(0).setUserImageUrl(s3Service.getFileUrl(getFlagsMoreRes.get(i).getUserImageUrl()));

        }
        return getFlagsMoreRes;

    }*/
}
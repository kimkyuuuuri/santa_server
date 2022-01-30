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

import static com.smileflower.santa.config.BaseResponseStatus.EMPTY_COMMENT;
import static com.smileflower.santa.config.BaseResponseStatus.INVALID_POST;


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

    public int checkSaveExist(int userIdx, int pictureIdx) {
        int exist = commentDao.checkSaveExist(userIdx, pictureIdx);
        return exist;
    }


    public int checkFlagCommentExist(long flagCommentIdx) {
        int exist = commentDao.checkFlagCommentExist(flagCommentIdx);
        return exist;
    }



    public int checkJwt(String jwt) {
        int exist = commentDao.checkJwt(jwt);
        return exist;
    }

    public int checkFlagCommentWhereUserExist(Long flagIdx,int userIdx){
        return commentDao.checkFlagCommentWhereUserExist(flagIdx,userIdx);
    }
    public List<GetFlagCommentRes> getFlagComment(Long flagIdx) throws BaseException {
        if (checkFlagExist(flagIdx) == 0)
            throw new BaseException(INVALID_POST);

        List<GetFlagCommentRes> getFlagCommentRes = commentDao.getFlagComment(flagIdx);

        if (getFlagCommentRes.size() == 0)
            throw new BaseException(EMPTY_COMMENT);

        for (int i = 0; i < getFlagCommentRes.size(); i++) {
            if (getFlagCommentRes.get(i).getUserImageUrl() != null)
                getFlagCommentRes.get(i).setUserImageUrl(s3Service.getFileUrl(getFlagCommentRes.get(i).getUserImageUrl()));
            for (int j = 0; j < getFlagCommentRes.get(i).getGetFlagRecommentRes().size(); j++) {
                if (getFlagCommentRes.get(i).getGetFlagRecommentRes().get(j).getUserImageUrl() != null)
                    getFlagCommentRes.get(i).getGetFlagRecommentRes().get(j).setUserImageUrl(s3Service.getFileUrl(getFlagCommentRes.get(i).getGetFlagRecommentRes().get(j).getUserImageUrl()));

            }
        }
        return getFlagCommentRes;

    }

    public int checkFlagExist(Long flagIdx) {
        return commentDao.checkFlagExist(flagIdx);
    }
}

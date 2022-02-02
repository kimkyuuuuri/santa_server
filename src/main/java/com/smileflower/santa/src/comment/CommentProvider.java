package com.smileflower.santa.src.comment;

import com.smileflower.santa.config.BaseException;
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
    private List<GetCommentRes> getCommentRes;
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

    public int checkPictureCommentExist(long pictureCommentIdx) {
        int exist = commentDao.checkPictureCommentExist(pictureCommentIdx);
        return exist;
    }

    public int checkJwt(String jwt) {
        int exist = commentDao.checkJwt(jwt);
        return exist;
    }

    public int checkFlagCommentWhereUserExist(Long flagIdx,int userIdx){
        return commentDao.checkFlagCommentWhereUserExist(flagIdx,userIdx);
    }

    public int checkPictureCommentWhereUserExist(Long pictureIdx,int userIdx){
        return commentDao.checkPictureCommentWhereUserExist(pictureIdx,userIdx);
    }
    public List<GetCommentRes> getComment(Long idx,String type,int userIdx) throws BaseException {
        if(type.equals("flag")) {
            if (checkFlagExist(idx) == 0)
                throw new BaseException(INVALID_POST);

            getCommentRes = commentDao.getFlagComment(idx);
        }
        else if (type.equals("picture"))
        {
            if (checkPictureExist(idx) == 0)
                throw new BaseException(INVALID_POST);
            getCommentRes=commentDao.getPictureComment(idx);
        }
        if (getCommentRes.size() == 0)
            throw new BaseException(EMPTY_COMMENT);

        for (int i = 0; i < getCommentRes.size(); i++) {
            if (getCommentRes.get(i).getUserIdx()==userIdx)
            {getCommentRes.get(i).setIsUsersComment("t");}
            if (getCommentRes.get(i).getUserImageUrl() != null)
                getCommentRes.get(i).setUserImageUrl(s3Service.getFileUrl(getCommentRes.get(i).getUserImageUrl()));
            for (int j = 0; j < getCommentRes.get(i).getGetRecommentRes().size(); j++) {
                if(getCommentRes.get(i).getGetRecommentRes().get(j).getUserIdx()==userIdx)
                { getCommentRes.get(i).getGetRecommentRes().get(j).setIsUsersComment("t");  }
                if (getCommentRes.get(i).getGetRecommentRes().get(j).getUserImageUrl() != null)
                    getCommentRes.get(i).getGetRecommentRes().get(j).setUserImageUrl(s3Service.getFileUrl(getCommentRes.get(i).getGetRecommentRes().get(j).getUserImageUrl()));

            }
        }
        return getCommentRes;

    }

    public int checkFlagExist(Long flagIdx) {
        return commentDao.checkFlagExist(flagIdx);
    }
    public int checkPictureExist(Long pictureIdx) {
        return commentDao.checkPictureExist(pictureIdx);
    }
}

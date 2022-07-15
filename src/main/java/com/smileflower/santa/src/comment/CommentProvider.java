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
    private List<GetCommentRes> getCommentsRes;
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    public CommentProvider(CommentDao commentDao, JwtService jwtService, S3Service s3Service) {

        this.commentDao = commentDao;
        this.jwtService = jwtService;
        this.s3Service = s3Service;
    }



    public int checkFlagCommentExist(long flagCommentIdx) {
        int exist = commentDao.checkFlagCommentExist(flagCommentIdx);
        return exist;
    }

    public int checkPictureCommentExist(long pictureCommentIdx) {
        int exist = commentDao.checkPictureCommentExist(pictureCommentIdx);
        return exist;
    }

    public int checkFlagRecommentExist(long flagRecommentIdx) {
        int exist = commentDao.checkFlagRecommentExist(flagRecommentIdx);
        return exist;
    }

    public int checkPictureRecommentExist(long pictureRecommentIdx) {
        int exist = commentDao.checkPictureRecommentExist(pictureRecommentIdx);
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

    public String getUserImage(int userIdx){
            String image=commentDao.selectUserImage(userIdx);
        if (image != null)
            image=s3Service.getFileUrl(image);

        return image;
    }

    public List<GetCommentRes> getComment(Long idx,String type,int userIdx) throws BaseException {

        if(type.equals("flag")) {

            if (checkFlagExist(idx) == 0)
                throw new BaseException(INVALID_POST);

            getCommentsRes = commentDao.getFlagComments(idx);

        }
        else if (type.equals("picture"))
        {
            if (checkPictureExist(idx) == 0)
                throw new BaseException(INVALID_POST);
            getCommentsRes=commentDao.getPictureComments(idx);

        }
        if ( getCommentsRes==null)
            throw new BaseException(EMPTY_COMMENT);

        for (int i = 0; i < getCommentsRes.size(); i++) {
            if (getCommentsRes.get(i).getUserIdx()==userIdx)
            {getCommentsRes.get(i).setIsUsersComment("t");}
            else if (type.equals("flag") && getFlagUserIdx(idx)==userIdx)
            {getCommentsRes.get(i).setIsUsersComment("t");}
            else if (type.equals("picture") && getPictureUserIdx(idx)==userIdx)
            {getCommentsRes.get(i).setIsUsersComment("t");}

            if (getCommentsRes.get(i).getUserImageUrl() != null)
                getCommentsRes.get(i).setUserImageUrl(s3Service.getFileUrl(getCommentsRes.get(i).getUserImageUrl()));
            for (int j = 0; j < getCommentsRes.get(i).getGetRecommentRes().size(); j++) {
                if(getCommentsRes.get(i).getGetRecommentRes().get(j).getUserIdx()==userIdx)
                { getCommentsRes.get(i).getGetRecommentRes().get(j).setIsUsersComment("t");}

                if(type.equals("flag") && getFlagUserIdx(idx)==userIdx)
                { getCommentsRes.get(i).getGetRecommentRes().get(j).setIsUsersComment("t");}

                if(type.equals("picture") && getPictureUserIdx(idx)==userIdx)
                { getCommentsRes.get(i).getGetRecommentRes().get(j).setIsUsersComment("t");}

                if (getCommentsRes.get(i).getGetRecommentRes().get(j).getUserImageUrl() != null)
                    getCommentsRes.get(i).getGetRecommentRes().get(j).setUserImageUrl(s3Service.getFileUrl(getCommentsRes.get(i).getGetRecommentRes().get(j).getUserImageUrl()));

            }
        }
        return getCommentsRes;

    }

    public int checkFlagExist(Long flagIdx) {
        return commentDao.checkFlagExist(flagIdx);
    }
    public int checkPictureExist(Long pictureIdx) {
        return commentDao.checkPictureExist(pictureIdx);
    }

    public int getFlagUserIdx(Long flagIdx){
        return commentDao.selectFlagUserIdx(flagIdx);
    }

    public int getPictureUserIdx(Long pictureIdx){
        return commentDao.selectPictureUserIdx(pictureIdx);
    }

    public String getFlagPushToken(Long flagIdx){
        return commentDao.getUserFlagPushToken(flagIdx);
    }

    public int getUserIdxByFlag(Long flagIdx){
        return commentDao.getUserIdxByFlag(flagIdx);
    }

    public String getPicturePushToken(Long pictureIdx){
        return commentDao.getUserPicturePushToken(pictureIdx);
    }

    public int getUserIdxByPicture(Long pictureIdx){
        return commentDao.getUserIdxByPicture(pictureIdx);
    }

    public String getFlagCommentPushToken(Long flagIdx){
        return commentDao.getUserFlagCommentPushToken(flagIdx);
    }

    public int getUserIdxByFlagComment(Long flagCommentIdx){
        return commentDao.getUserIdxByFlagComment(flagCommentIdx);
    }

    public String getPictureCommentPushToken(Long pictureIdx){
        return commentDao.getUserPictureCommentPushToken(pictureIdx);
    }

    public int getUserIdxByPictureComment(Long pictureCommentIdx){
        return commentDao.getUserIdxByPictureComment(pictureCommentIdx);
    }
    public String getUserFlagPushTokenByRecomment(Long pictureIdx){
        return commentDao.getUserFlagPushTokenByRecomment(pictureIdx);
    }

    public int getUserIdxByFlagCommentByRecomment(Long pictureCommentIdx){
        return commentDao.getUserIdxByFlagCommentByRecomment(pictureCommentIdx);
    }

    public String getUserPicturePushTokenByRecomment(Long pictureIdx){
        return commentDao.getUserPicturePushTokenByRecomment(pictureIdx);
    }

    public int getUserIdxByPictureCommentByRecomment(Long pictureCommentIdx){
        return commentDao.getUserIdxByPictureCommentByRecomment(pictureCommentIdx);
    }
    public GetUserInfoRes getUserName(int userIdx){
        return commentDao.getUserName(userIdx);
    }
}

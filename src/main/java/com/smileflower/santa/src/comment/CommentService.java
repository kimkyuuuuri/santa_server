package com.smileflower.santa.src.comment;


import com.smileflower.santa.config.BaseException;
import com.smileflower.santa.src.comment.model.*;
import com.smileflower.santa.utils.FcmPush;
import com.smileflower.santa.utils.JwtService;
import com.smileflower.santa.utils.S3Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;

import static com.smileflower.santa.config.BaseResponseStatus.*;


@Service

public class CommentService {
    private final CommentProvider commentProvider;
    private final CommentDao commentDao;
    private final JwtService jwtService;
    private final S3Service s3Service;
    private final FcmPush fcmPush;

    private JdbcTemplate jdbcTemplate;
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    public CommentService(CommentDao commentDao, JwtService jwtService, S3Service s3Service, CommentProvider commentProvider, FcmPush fcmPush) throws BaseException {
        this.commentProvider = commentProvider;
        this.commentDao = commentDao;
        this.jwtService = jwtService;
        this.s3Service = s3Service;
        this.fcmPush = fcmPush;
    }

    public PostCommentRes createComment(PostCommentReq postCommentReq, Long idx, int userIdx, String type) throws BaseException, IOException {

        int commentIdx=0;

        if(type.equals("flag")) {
            if (commentProvider.checkFlagExist(idx) == 0)
                throw new BaseException(INVALID_POST);
            commentIdx = commentDao.createFlagComment(postCommentReq, idx, userIdx);
            String pushToken= commentProvider.getFlagPushToken(idx);
            GetUserInfoRes getUserInfoRes=commentProvider.getUserName(userIdx);
            int userIdxbyFlagIdx=commentProvider.getUserIdxByFlag(idx);

            if(userIdxbyFlagIdx!=userIdx){
                GetUserInfoRes getUserInfoResForPush=commentProvider.getUserName(userIdx);
                commentDao.createFlagCommentNotification(userIdxbyFlagIdx,idx,getUserInfoRes.getName()+"님이 회원님의 게시물에 댓글을 남겼어요! 지금 확인해보세요👀");
                if(getUserInfoResForPush.getTokenType().equalsIgnoreCase("I"))
                fcmPush.iosPush(pushToken,"SANTA",getUserInfoRes.getName()+"님이 회원님의 게시물에 댓글을 남겼어요! 지금 확인해보세요👀");
                else if(getUserInfoResForPush.getTokenType().equals("A"))
                    fcmPush.androidPush(pushToken,"SANTA",getUserInfoRes.getName()+"님이 회원님의 게시물에 댓글을 남겼어요! 지금 확인해보세요👀");
            }
        }
        else if(type.equals("picture")) {

            if (commentProvider.checkPictureExist(idx) == 0)
                throw new BaseException(INVALID_POST);
            commentIdx = commentDao.createPictureComment(postCommentReq, idx, userIdx);
            String pushToken= commentProvider.getPicturePushToken(idx);
            int userIdxbyPictureIdx=commentProvider.getUserIdxByPicture(idx);

            if(userIdxbyPictureIdx!=userIdx){
                GetUserInfoRes getUserInfoRes =commentProvider.getUserName(userIdx);
                GetUserInfoRes getUserInfoResForPush=commentProvider.getUserName(userIdx);
                commentDao.createPictureCommentNotification(userIdxbyPictureIdx,idx,getUserInfoRes.getName() + "님이 회원님의 게시물에 댓글을 남겼어요! 지금 확인해보세요👀");


                if(getUserInfoResForPush.getTokenType().equalsIgnoreCase("I")) {

                    fcmPush.iosPush(pushToken, "SANTA", getUserInfoRes.getName() + "님이 회원님의 게시물에 댓글을 남겼어요! 지금 확인해보세요👀");
                }
                else if(getUserInfoResForPush.getTokenType().equals("A")) {
                    fcmPush.androidPush(pushToken, "SANTA", getUserInfoRes.getName() + "님이 회원님의 게시물에 댓글을 남겼어요! 지금 확인해보세요👀");

                }
            }
        }


            return new PostCommentRes(commentIdx);

    }

    public PostRecommentRes createRecomment(PostRecommentReq postRecommentReq, long commentIdx, int userIdx, String type) throws BaseException, IOException {
       int recommentIdx=0;


        if(type.equals("flag")) {
            String flagPushToken= commentProvider.getUserFlagPushTokenByRecomment(commentIdx);
            int userIdxbyFlagIdx=commentProvider.getUserIdxByFlagCommentByRecomment(commentIdx);
            if (commentProvider.checkFlagCommentExist(commentIdx) == 0)
                throw new BaseException(INVALID_COMMENT);

            String flagCommentPushToken= commentProvider.getFlagCommentPushToken(commentIdx);
            int userIdxbyFlagCommentIdx=commentProvider.getUserIdxByFlagComment(commentIdx);

            //GetUserInfoRes getUserInfoRes2=commentProvider.getUserName(userIdxbyFlagIdx);

             if(userIdxbyFlagCommentIdx!=userIdx){
                 GetUserInfoRes getUserInfoResForPush=commentProvider.getUserName(userIdxbyFlagCommentIdx);
                 GetUserInfoRes getUserInfoRes=commentProvider.getUserName(userIdx);
                 Long flagIdx=commentDao.getFlagIdx(commentIdx);
                commentDao.createFlagRecommentNotification(userIdxbyFlagCommentIdx,flagIdx,getUserInfoRes.getName() + "님이 회원님의 댓글에 답글을 남겼어요! 지금 확인해보세요👀");
                if(getUserInfoResForPush.getTokenType().equalsIgnoreCase("I")) {
                    fcmPush.iosPush(flagCommentPushToken, "SANTA", getUserInfoRes.getName() + "님이 회원님의 댓글에 답글을 남겼어요! 지금 확인해보세요👀");
                }
                else if(getUserInfoResForPush.getTokenType().equals("A")) {
                    fcmPush.androidPush(flagCommentPushToken, "SANTA", getUserInfoRes.getName() + "님이 회원님의 댓글에 답글을 남겼어요! 지금 확인해보세요👀");
                }
             }

            else if(userIdxbyFlagIdx!=userIdx){
                 GetUserInfoRes getUserInfoRes=commentProvider.getUserName(userIdx);
                 GetUserInfoRes getUserInfoResForPush=commentProvider.getUserName(userIdxbyFlagIdx);

                 Long flagIdx=commentDao.getFlagIdx(commentIdx);

                    commentDao.createFlagCommentNotification(userIdxbyFlagIdx,flagIdx,getUserInfoRes.getName() + "님이 회원님의 게시물에 댓글을 남겼어요! 지금 확인해보세요👀");
                if(getUserInfoResForPush.getTokenType().equalsIgnoreCase("I")) {
                    fcmPush.iosPush(flagPushToken, "SANTA", getUserInfoRes.getName() + "님이 회원님의 게시물에 댓글을 남겼어요! 지금 확인해보세요👀");
                }else if(getUserInfoResForPush.getTokenType().equals("A")) {
                    fcmPush.androidPush(flagCommentPushToken, "SANTA", getUserInfoRes.getName() + "님이 회원님의 게시물에 댓글을 남겼어요! 지금 확인해보세요👀");
                }

             }

            recommentIdx = commentDao.createFlagRecomment(postRecommentReq, commentIdx, userIdx);
        }


        else if(type.equals("picture")) {
            System.out.println("dd");
            if (commentProvider.checkPictureCommentExist(commentIdx) == 0)
                throw new BaseException(INVALID_COMMENT);
            String picturePushToken= commentProvider.getUserPicturePushTokenByRecomment(commentIdx);
            int userIdxbyPictureIdx=commentProvider.getUserIdxByPictureCommentByRecomment(commentIdx);


            int userIdxbyPictureCommentIdx=commentProvider.getUserIdxByPictureComment(commentIdx);
            if(userIdxbyPictureCommentIdx!=userIdx){
                GetUserInfoRes getUserInfoRes=commentProvider.getUserName(userIdx);
                String pictureCommentPushToken= commentProvider.getPictureCommentPushToken(commentIdx);
                GetUserInfoRes getUserInfoResForPush=commentProvider.getUserName(userIdxbyPictureCommentIdx);

                Long pictureIdx=commentDao.getPictureIdx(commentIdx);
                commentDao.createPictureRecommentNotification(userIdxbyPictureCommentIdx,pictureIdx,getUserInfoRes.getName() + "님이 회원님의 댓글에 답글을 남겼어요! 지금 확인해보세요👀");
                if(getUserInfoResForPush.getTokenType().equalsIgnoreCase("I")) {

                    fcmPush.iosPush(pictureCommentPushToken, "SANTA", getUserInfoRes.getName() + "님이 회원님의 댓글에 답글을 남겼어요! 지금 확인해보세요👀");
                }
                else if(getUserInfoResForPush.getTokenType().equals("A")) {
                    fcmPush.androidPush(pictureCommentPushToken, "SANTA", getUserInfoRes.getName() + "님이 회원님의 댓글에 답글을 남겼어요! 지금 확인해보세요👀");
                }

            }

                else if(userIdxbyPictureIdx!=userIdx){
                GetUserInfoRes getUserInfoRes=commentProvider.getUserName(userIdx);
                String pictureCommentPushToken= commentProvider.getPictureCommentPushToken(commentIdx);
                GetUserInfoRes getUserInfoResForPush=commentProvider.getUserName(userIdxbyPictureIdx);

                Long pictureIdx=commentDao.getPictureIdx(commentIdx);
                commentDao.createPictureCommentNotification(userIdxbyPictureIdx,pictureIdx,getUserInfoRes.getName() + "님이 회원님의 게시물에 댓글을 남겼어요! 지금 확인해보세요👀");
                if(getUserInfoResForPush.getTokenType().equalsIgnoreCase("I")) {
                    fcmPush.iosPush(picturePushToken, "SANTA", getUserInfoRes.getName() + "님이 회원님의 게시물에 댓글을 남겼어요! 지금 확인해보세요👀");
                }
                else if(getUserInfoResForPush.getTokenType().equals("A")) {
                    fcmPush.androidPush(pictureCommentPushToken, "SANTA", getUserInfoRes.getName() + "님이 회원님의 게시물에 댓글을 남겼어요! 지금 확인해보세요👀");
                }
            }
            recommentIdx = commentDao.createPictureRecomment(postRecommentReq, commentIdx, userIdx);
        }

        return new PostRecommentRes(recommentIdx);

    }

    public PatchCommentStatusRes deleteComment(int userIdx, long commentIdx, String type) throws BaseException {
       if(type.equals("flag")) {
           if (commentProvider.checkFlagCommentExist(commentIdx) == 0)
               throw new BaseException(INVALID_COMMENT);
           else if (commentProvider.checkFlagCommentWhereUserExist(commentIdx, userIdx) == 0)
               throw new BaseException(INVALID_COMMENT_USER);
           commentDao.deleteFlagComment(commentIdx);
       }
       else if (type.equals("picture")) {
           if (commentProvider.checkPictureCommentExist(commentIdx) == 0)
               throw new BaseException(INVALID_COMMENT);
           else if (commentProvider.checkPictureCommentWhereUserExist(commentIdx, userIdx) == 0)
               throw new BaseException(INVALID_COMMENT_USER);
           commentDao.deletePictureComment(commentIdx);
       }
           return new PatchCommentStatusRes(commentIdx);
       }

    public PatchRecommentStatusRes deleteRecomment(int userIdx, long recommentIdx, String type) throws BaseException {
        if(type.equals("flag")) {
            if (commentProvider.checkFlagRecommentExist(recommentIdx) == 0)
                throw new BaseException(INVALID_COMMENT);

            commentDao.deleteFlagRecomment(recommentIdx);
        }
        else if (type.equals("picture")) {
            if (commentProvider.checkPictureRecommentExist(recommentIdx) == 0)
                throw new BaseException(INVALID_COMMENT);

            commentDao.deletePictureRecomment(recommentIdx);
        }
        return new PatchRecommentStatusRes(recommentIdx);
    }



}
package com.smileflower.santa.src.comment;


import com.smileflower.santa.config.BaseException;
import com.smileflower.santa.src.comment.model.*;
import com.smileflower.santa.utils.JwtService;
import com.smileflower.santa.utils.S3Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import static com.smileflower.santa.config.BaseResponseStatus.*;


@Service
public class CommentService {
    private final CommentProvider commentProvider;
    private final CommentDao commentDao;
    private final JwtService jwtService;
    private final S3Service s3Service;

    private JdbcTemplate jdbcTemplate;
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    public CommentService(CommentDao commentDao, JwtService jwtService, S3Service s3Service, CommentProvider commentProvider) throws BaseException {
        this.commentProvider = commentProvider;
        this.commentDao = commentDao;
        this.jwtService = jwtService;
        this.s3Service = s3Service;
    }
    public PostCommentRes createComment(PostCommentReq postCommentReq, Long idx, int userIdx, String type) throws BaseException {
        int commentIdx=0;
        if(type.equals("flag")) {
            if (commentProvider.checkFlagExist(idx) == 0)
                throw new BaseException(INVALID_POST);
            commentIdx = commentDao.createFlagComment(postCommentReq, idx, userIdx);
        }
        if(type.equals("picture")) {
            if (commentProvider.checkPictureExist(idx) == 0)
                throw new BaseException(INVALID_POST);
            commentIdx = commentDao.createPictureComment(postCommentReq, idx, userIdx);
        }

            return new PostCommentRes(commentIdx);

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



}
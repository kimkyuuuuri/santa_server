package com.smileflower.santa.src.comment;


import com.smileflower.santa.config.BaseException;
import com.smileflower.santa.src.comment.model.*;
import com.smileflower.santa.src.flags.model.DeleteFlagRes;
import com.smileflower.santa.utils.JwtService;
import com.smileflower.santa.utils.S3Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public PostFlagCommentRes createFlagComment(PostFlagCommentReq postFlagCommentReq, Long flagIdx, int userIdx) throws BaseException {
        if (commentProvider.checkFlagExist(flagIdx) == 0)
            throw new BaseException(INVALID_POST);
            int flagCommentIdx = commentDao.createFlagComment(postFlagCommentReq, flagIdx, userIdx);

            return new PostFlagCommentRes(flagCommentIdx);

    }

    public PatchFlagCommentStatusRes deleteFlagComment(int userIdx, long flagCommentIdx) throws BaseException {
        if (commentProvider.checkFlagCommentExist(flagCommentIdx) == 0)
            throw new BaseException(INVALID_COMMENT);
        else if (commentProvider.checkFlagCommentWhereUserExist(flagCommentIdx,userIdx) == 0)
            throw new BaseException(INVALID_COMMENT_USER);
        commentDao.deleteFlagComment(flagCommentIdx);
        return new PatchFlagCommentStatusRes(flagCommentIdx);

    }

}
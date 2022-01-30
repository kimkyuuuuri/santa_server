package com.smileflower.santa.src.comment;

import com.smileflower.santa.config.BaseException;
import com.smileflower.santa.config.BaseResponse;

import com.smileflower.santa.src.comment.model.*;
import com.smileflower.santa.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.smileflower.santa.config.BaseResponseStatus.EMPTY_JWT;
import static com.smileflower.santa.config.BaseResponseStatus.INVALID_JWT;


@RestController
@RequestMapping("/app/comments")
public class CommentController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final CommentProvider commentProvider;
    @Autowired
    private final CommentService commentService;
    @Autowired
    private final JwtService jwtService;


    public CommentController(CommentProvider commentProvider, CommentService commentService, JwtService jwtService) {
        this.commentProvider = commentProvider;
        this.commentService = commentService;
        this.jwtService = jwtService;
    }

    @ResponseBody
    @GetMapping("/flags/{flagIdx}")
    public BaseResponse<List<GetFlagCommentRes>> getFlagComment(@PathVariable("flagIdx") Long flagIdx) throws BaseException {
        try {
            if (jwtService.getJwt() == null) {
                return new BaseResponse<>(EMPTY_JWT);
            }

            else if (commentProvider.checkJwt(jwtService.getJwt()) == 1) {
                return new BaseResponse<>(INVALID_JWT);

            }

                List<GetFlagCommentRes> getFlagCommentRes = commentProvider.getFlagComment( flagIdx);
                return new BaseResponse<>(getFlagCommentRes);
            } catch (BaseException exception) {
                return new BaseResponse<>((exception.getStatus()));
            }
        }


}
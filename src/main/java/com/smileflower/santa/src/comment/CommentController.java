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

import static com.smileflower.santa.config.BaseResponseStatus.*;


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
    @GetMapping("/{idx}")
    public BaseResponse<List<GetCommentRes>> getFlagComment(@PathVariable("idx") Long Idx, @RequestParam("type") String type) throws BaseException {
        try {
            if (jwtService.getJwt() == null) {
                return new BaseResponse<>(EMPTY_JWT);
            }

            else if (commentProvider.checkJwt(jwtService.getJwt()) == 1) {
                return new BaseResponse<>(INVALID_JWT);

            }

                    List<GetCommentRes> getCommentRes = commentProvider.getComment(Idx,type);

                return new BaseResponse<>(getCommentRes);
            } catch (BaseException exception) {
                return new BaseResponse<>((exception.getStatus()));
            }
        }

    @ResponseBody
    @PostMapping("/{idx}")
    public BaseResponse<PostCommentRes> createFlagComment(@RequestBody PostCommentReq postCommentReq, @PathVariable("idx") long idx,@RequestParam("type") String type ) throws BaseException {
        try{
            if(jwtService.getJwt()==null){
                return new BaseResponse<>(EMPTY_JWT);
            }
            if(postCommentReq.getContents()==null)
                return new BaseResponse<>(POST_COMMENT_EMPTY_CONTENT);

            if(postCommentReq.getContents().length() > 450)
                return new BaseResponse<>(POST_COMMENT_INVALID_CONTENT);

            else{
                int userIdx=jwtService.getUserIdx();
                PostCommentRes postCommentRes = commentService.createComment(postCommentReq,idx,userIdx,type);
                return new BaseResponse<>(postCommentRes);
            }

        }catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }

    }

    @ResponseBody
    @PatchMapping ("/{idx}")
    public BaseResponse<PatchCommentStatusRes> deleteFlagComment(@PathVariable("idx") long idx ,@RequestParam("type") String type ) throws BaseException {
        try{
            if(jwtService.getJwt()==null){
                return new BaseResponse<>(EMPTY_JWT);
            }

            else{
                int userIdx=jwtService.getUserIdx();
                PatchCommentStatusRes patchFlagCommentStatus = commentService.deleteComment(userIdx,idx,type);
                return new BaseResponse<>(patchFlagCommentStatus);
            }

        }catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }

    }

}
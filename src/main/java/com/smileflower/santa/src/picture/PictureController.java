package com.smileflower.santa.src.picture;

import com.smileflower.santa.config.BaseException;
import com.smileflower.santa.config.BaseResponse;
import com.smileflower.santa.src.picture.model.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.smileflower.santa.src.picture.model.*;
import com.smileflower.santa.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.smileflower.santa.config.BaseResponseStatus.EMPTY_JWT;
import static com.smileflower.santa.config.BaseResponseStatus.INVALID_JWT;


@RestController
@RequestMapping("/app/pictures")
public class PictureController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final PictureProvider pictureProvider;
    @Autowired
    private final PictureService pictureService;
    @Autowired
    private final JwtService jwtService;


    public PictureController(PictureProvider pictureProvider, PictureService pictureService, JwtService jwtService) {
        this.pictureProvider = pictureProvider;
        this.pictureService = pictureService;
        this.jwtService = jwtService;
    }

    @ResponseBody
    @PostMapping("/save")
    public BaseResponse<PostPictureSaveRes> save(@RequestBody PostPictureSaveReq postPictureSaveReq) throws BaseException {
        try {
            if (jwtService.getJwt() == null) {
                return new BaseResponse<>(EMPTY_JWT);
            }
            else if (pictureProvider.checkJwt(jwtService.getJwt()) == 1) {
                return new BaseResponse<>(INVALID_JWT);

            }


                PostPictureSaveRes postPictureSaveRes = pictureService.postPictureSaveRes(jwtService.getUserIdx(), postPictureSaveReq.getPictureIdx());
                return new BaseResponse<>(postPictureSaveRes);
            } catch (BaseException exception) {
                return new BaseResponse<>((exception.getStatus()));
            }
        }

}
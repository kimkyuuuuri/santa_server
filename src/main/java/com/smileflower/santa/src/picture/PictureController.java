package com.smileflower.santa.src.picture;

import com.smileflower.santa.config.BaseException;
import com.smileflower.santa.config.BaseResponse;
import com.smileflower.santa.src.flags.model.DeleteFlagRes;
import com.smileflower.santa.src.picture.model.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.smileflower.santa.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.smileflower.santa.config.BaseResponseStatus.*;


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
    @PostMapping("/{pictureIdx}/save")
    public BaseResponse<PostPictureSaveRes> save(@PathVariable("pictureIdx") int pictureIdx) throws BaseException {
        try {
            if (jwtService.getJwt() == null) {
                return new BaseResponse<>(EMPTY_JWT);
            }

            else if (pictureProvider.checkJwt(jwtService.getJwt()) == 1) {
                return new BaseResponse<>(INVALID_JWT);

            }
            else if (pictureProvider.checkPictureExist(pictureIdx)!=1) {
                return new BaseResponse<>( POST_SAVE_EMPTY_PICTURE);
            }

                PostPictureSaveRes postPictureSaveRes = pictureService.postPictureSaveRes(jwtService.getUserIdx(), pictureIdx);
                return new BaseResponse<>(postPictureSaveRes);
            } catch (BaseException exception) {
                return new BaseResponse<>((exception.getStatus()));
            }
        }
    @DeleteMapping("/{pictureIdx}")
    public BaseResponse<DeletePictureRes> deleteFlag(@PathVariable("pictureIdx") Long pictureIdx) {
        try{
            if(jwtService.getJwt()==null){
                return new BaseResponse<>(EMPTY_JWT);
            }

            else{
                int userIdx=jwtService.getUserIdx();
                DeletePictureRes deletePictureRes =  pictureService.deletePicture(pictureIdx);
                return new BaseResponse<>(deletePictureRes);
            }
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}
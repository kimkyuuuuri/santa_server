package com.smileflower.santa.src.flag;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.smileflower.santa.flag.model.GpsInfoRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.smileflower.santa.config.BaseException;
import com.smileflower.santa.config.BaseResponse;
import com.smileflower.santa.src.flag.model.*;
import com.smileflower.santa.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


import static com.smileflower.santa.config.BaseResponseStatus.*;

@RestController
@RequestMapping("/app")
public class FlagController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final FlagProvider flagProvider;
    @Autowired
    private final FlagService flagService;
    @Autowired
    private final JwtService jwtService;


    public FlagController(FlagProvider flagProvider, FlagService flagService, JwtService jwtService) {
        this.flagProvider = flagProvider;
        this.flagService = flagService;
        this.jwtService = jwtService;
    }


    @ResponseBody
    @PostMapping("/flags/{flagIdx}/save")
    public BaseResponse<PostFlagSaveRes> save(@PathVariable("flagIdx") int flagIdx) throws BaseException {
        try {
            if (jwtService.getJwt() == null) {
                return new BaseResponse<>(EMPTY_JWT);
            } else if (flagProvider.checkJwt(jwtService.getJwt()) == 1) {
                return new BaseResponse<>(INVALID_JWT);

            }
            PostFlagSaveRes postFlagSaveRes = flagService.postFlagSaveRes(jwtService.getUserIdx(), flagIdx);
            return new BaseResponse<>(postFlagSaveRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new BaseResponse<>();
        } catch (IOException e) {
            e.printStackTrace();
            return new BaseResponse<>();
        }
    }




    @ResponseBody
    @GetMapping("/flags")
    public BaseResponse<GetFlagRes> getFlag(@RequestParam(required = true) String mountain) {
        try{
            if(jwtService.getJwt()==null){
                return new BaseResponse<>(EMPTY_JWT);
            }

            else{
                int userIdx=jwtService.getUserIdx();
                GetFlagRes getFlagRes = flagProvider.getFlag(userIdx,mountain);
                return new BaseResponse<>(getFlagRes);
            }
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }


    @ResponseBody
    @PostMapping("flags/{mountainIdx}/")
    public BaseResponse<PostFlagPictureRes> createFlag(@RequestBody PostFlagPictureReq postFlagPictureReq,@PathVariable("mountainIdx")int mountainIdx ) throws BaseException {
        try{
            if(jwtService.getJwt()==null){
                return new BaseResponse<>(EMPTY_JWT);
            }

            else{
                int userIdx=jwtService.getUserIdx();
                PostFlagPictureRes postFlagPictureRes = flagService.createFlag(postFlagPictureReq,mountainIdx,userIdx);
                return new BaseResponse<>(postFlagPictureRes);
            }

        }catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }

    }

    @ResponseBody
    @GetMapping("/flags/{mountainIdx}/rank")
    public BaseResponse<GetFlagRankRes> getFlagRank(@PathVariable("mountainIdx")int mountainIdx) {
        try{
            if(jwtService.getJwt()==null){
                return new BaseResponse<>(EMPTY_JWT);
            }

            else{
                int userIdx=jwtService.getUserIdx();
                GetFlagRankRes getFlagRankRes = flagProvider.getFlagRank(userIdx,mountainIdx);
                return new BaseResponse<>(getFlagRankRes);
            }
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
    @ResponseBody
    @GetMapping("/flags/{mountainIdx}/{altitude}")
    public BaseResponse<GetAltitudeRes> getAltitude(@PathVariable("mountainIdx")int mountainIdx,
                                                    @PathVariable("altitude")double altitude) {
        try{
            if(jwtService.getJwt()==null){
                return new BaseResponse<>(EMPTY_JWT);
            }

            else{
                int userIdx=jwtService.getUserIdx();
                GetAltitudeRes getAltitudeRes = flagProvider.getAltitude(userIdx,mountainIdx,altitude);
                return new BaseResponse<>(getAltitudeRes);
            }
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @PostMapping("/flags/{mountainIdx}/hard")
    public BaseResponse<PostFlagHardRes> createHard(@RequestBody PostFlagHardReq postFlagHardReq,@PathVariable("mountainIdx")int mountainIdx ) throws BaseException {
        try{
            if(jwtService.getJwt()==null){
                return new BaseResponse<>(EMPTY_JWT);
            }

            else{
                int userIdx=jwtService.getUserIdx();
                PostFlagHardRes postFlagHardRes = flagService.createHard(postFlagHardReq,mountainIdx,userIdx);
                return new BaseResponse<>(postFlagHardRes);
            }

        }catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }

    }

    @ResponseBody
    @PatchMapping ("/picks/{mountainIdx}")
    public BaseResponse<PatchPickRes> patchPickRes(@PathVariable("mountainIdx") int mountainIdx) throws BaseException {
        try{
            if(jwtService.getJwt()==null){
                return new BaseResponse<>(EMPTY_JWT);
            }

            else{
                int userIdx=jwtService.getUserIdx();
                PatchPickRes patchPickRes = flagService.patchPick(userIdx,mountainIdx);
                return new BaseResponse<>(patchPickRes);
            }

        }catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }

    }

    @ResponseBody
    @GetMapping("/picks")
    public BaseResponse<List<GetPickRes>> getPicks() {
        try{
            if(jwtService.getJwt()==null){
                return new BaseResponse<>(EMPTY_JWT);
            }

            else{
                int userIdx=jwtService.getUserIdx();
                List<GetPickRes> getPickRes = flagProvider.getPicks(userIdx);
                return new BaseResponse<>(getPickRes);
            }
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @PostMapping("/flags/{flagIdx}/report")
    public BaseResponse<PostFlagReportRes> report(@PathVariable("flagIdx")Long flagIdx ) throws BaseException {
        try{
            if(jwtService.getJwt()==null){
                return new BaseResponse<>(EMPTY_JWT);
            }

            else{
                int userIdx=jwtService.getUserIdx();
                PostFlagReportRes postFlagReportRes = flagService.report(userIdx,flagIdx);
                return new BaseResponse<>(postFlagReportRes);
            }

        }catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }

    }

    @PostMapping(path = "/flags/{mountainIdx}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public BaseResponse<PostFlagRes> uploadFlag(@RequestParam("latitude") double latitude, @RequestParam("longitude") double longitude,
                                                @RequestParam("altitude") double altitude, @RequestPart(required = false) MultipartFile file,
                                                @PathVariable("mountainIdx") Long mountainIdx) {
        try{
            if(jwtService.getJwt()==null){
                return new BaseResponse<>(EMPTY_JWT);
            }

            else{
                int userIdx=jwtService.getUserIdx();
                PostFlagRes postFlagRes =  flagService.uploadImage(new GpsInfoRequest(latitude,longitude,altitude), file, userIdx, mountainIdx);
                return new BaseResponse<>(postFlagRes);
            }
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }

    }


    @DeleteMapping("/flags/{flagIdx}")
    public BaseResponse<DeleteFlagRes> deleteFlag(@PathVariable("flagIdx") Long flagIdx) {
        try{
            if(jwtService.getJwt()==null){
                return new BaseResponse<>(EMPTY_JWT);
            }

            else{
                int userIdx=jwtService.getUserIdx();
                 DeleteFlagRes deleteFlagRes =  flagService.deleteFlag(flagIdx,userIdx);
                return new BaseResponse<>(deleteFlagRes);
            }
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }


    @GetMapping("/flags/{mountainIdx}/check-double")
    public BaseResponse<GetCheckDoubleVisitedRes> checkDoubleVisited(@PathVariable("mountainIdx") int mountainIdx) {
        try{
            if(jwtService.getJwt()==null){
                return new BaseResponse<>(EMPTY_JWT);
            }

            else{
                int userIdx=jwtService.getUserIdx();
                GetCheckDoubleVisitedRes getCheckDoubleVisitedRes =  flagProvider.checkDoubleVisited(mountainIdx,userIdx);
                return new BaseResponse<>(getCheckDoubleVisitedRes);
            }
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}
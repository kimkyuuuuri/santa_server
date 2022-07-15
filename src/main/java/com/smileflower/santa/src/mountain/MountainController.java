package com.smileflower.santa.src.mountain;

import com.fasterxml.jackson.databind.ser.Serializers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.smileflower.santa.config.BaseException;
import com.smileflower.santa.config.BaseResponse;
import com.smileflower.santa.src.mountain.model.*;
import com.smileflower.santa.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


import static com.smileflower.santa.config.BaseResponseStatus.*;
import static com.smileflower.santa.utils.ValidationRegex.isRegexEmail;

@RestController
@RequestMapping("/app/mountains")
public class MountainController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final MountainProvider mountainProvider;
    @Autowired
    private final MountainService mountainService;
    @Autowired
    private final JwtService jwtService;


    public MountainController(MountainProvider mountainProvider, MountainService mountainService, JwtService jwtService) {
        this.mountainProvider = mountainProvider;
        this.mountainService = mountainService;
        this.jwtService = jwtService;
    }
    @ResponseBody
    @GetMapping("")
    public BaseResponse<List<GetMountainRes>> getMountains() throws BaseException {
        try{
            if(jwtService.getJwt()==null){
                return new BaseResponse<>(EMPTY_JWT);
            }

            else{
                int userIdx=jwtService.getUserIdx();
                List<GetMountainRes> getMountainRes = mountainProvider.getMountains(userIdx);
                return new BaseResponse<>(getMountainRes);
            }

        }catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }


    @ResponseBody
    @GetMapping("/paging")
    public BaseResponse<List<GetMountainRes>> getMountainsPaging(@RequestParam(required = true) int index) throws BaseException {
        try{
            if(jwtService.getJwt()==null){
                return new BaseResponse<>(EMPTY_JWT);
            }

            else{
                int userIdx=jwtService.getUserIdx();
                List<GetMountainRes> getMountainRes = mountainProvider.getMountainsPaging(userIdx,index);
                return new BaseResponse<>(getMountainRes);
            }

        }catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @GetMapping("/{mountainIdx}/rank")
    public BaseResponse<GetMountainRankRes> getMountainRank(@PathVariable("mountainIdx")int mountainIdx) throws BaseException {
        try{
            if(jwtService.getJwt()==null){
                return new BaseResponse<>(EMPTY_JWT);
            }

            else{
                int userIdx=jwtService.getUserIdx();

                GetMountainRankRes getMountainRankRes = mountainProvider.getMountainRank(userIdx,mountainIdx);
                return new BaseResponse<>(getMountainRankRes);
            }

        }catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @GetMapping("/search")
    public BaseResponse<GetMountainIdxRes> getFlag(@RequestParam(required = true) String mountain) throws BaseException {
        try{
            if(jwtService.getJwt()==null){
                return new BaseResponse<>(EMPTY_JWT);
            }
            else if(mountain.equals("")){
                return new BaseResponse<>(EMPTY_MOUNTAIN_INPUT);
            }

            else{
                int userIdx=jwtService.getUserIdx();
                GetMountainIdxRes getMountainIdxRes = mountainProvider.getMountainIdx(userIdx,mountain);
                return new BaseResponse<>(getMountainIdxRes);
            }
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @GetMapping("/{mountainIdx}/map")
    public BaseResponse<GetMapRoadRes> getMap(@PathVariable("mountainIdx") int mountainIdx) throws BaseException{
        try{
            if(jwtService.getJwt()==null){
                return new BaseResponse<>(EMPTY_JWT);
            }
            else{
                int userIdx=jwtService.getUserIdx();
                GetMapRoadRes getMapRoadRes = mountainProvider.getMap(userIdx,mountainIdx);
                return new BaseResponse<>(getMapRoadRes);
            }
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @GetMapping("/{mountainIdx}/info")
    public BaseResponse<GetInfoPage> getMountainInfo(@PathVariable("mountainIdx")int mountainIdx) throws BaseException {
        try{
            if(jwtService.getJwt()==null){
                return new BaseResponse<>(EMPTY_JWT);
            }

            else{
                int userIdx=jwtService.getUserIdx();
                GetInfoPage getInfoPage = mountainProvider.getMountainInfo(userIdx,mountainIdx);
                return new BaseResponse<>(getInfoPage);
            }

        }catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}
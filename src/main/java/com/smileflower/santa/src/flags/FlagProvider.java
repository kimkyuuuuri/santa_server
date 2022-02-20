package com.smileflower.santa.src.flags;


import com.smileflower.santa.config.BaseException;
import com.smileflower.santa.src.flags.model.*;
import com.smileflower.santa.utils.JwtService;
import com.smileflower.santa.utils.S3Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.smileflower.santa.config.BaseResponseStatus.*;

import javax.transaction.Transactional;
//Provider : Read의 비즈니스 로직 처리
@Service
public class FlagProvider {

    private final FlagDao flagDao;
    private final JwtService jwtService;
    private final S3Service s3Service;

    private JdbcTemplate jdbcTemplate;
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public FlagProvider(FlagDao flagDao, JwtService jwtService, S3Service s3Service) {

        this.flagDao = flagDao;
        this.jwtService = jwtService;
        this.s3Service = s3Service;
    }
    public int checkMountain(String mountain){
        int exist = flagDao.checkMountain(mountain);
        return exist;
    }

    @Transactional
    public GetFlagRes getFlag(int userIdx, String mountain) throws BaseException {
        if (flagDao.checkMountain(mountain)==1){
            int mountainIdx = flagDao.checkMountainIdx(mountain);
            GetFlagRes getFlagRes = new GetFlagRes();
            getFlagRes.setMountainIdx(mountainIdx);
            getFlagRes.setMountain(mountain);
            getFlagRes.setHigh(flagDao.checkhigh(mountainIdx));
            return getFlagRes;
        }
        else{
            throw new BaseException(NON_EXIST_MOUNTAIN);
        }

    }


    public GetFlagRankRes getFlagRank(int userIdx, int mountainIdx) throws BaseException {
        GetFlagRankRes getFlagRankRes =new GetFlagRankRes();
        for(int i=0;i<2;i++){
            if(i==0){
                GetRankRes getRankRes= flagDao.getfirstRank(mountainIdx);
                if(getRankRes.getUserImage()!=null) {
                    getRankRes.setUserImage(s3Service.getFileUrl(getRankRes.getUserImage()));
                }
                getFlagRankRes.setFirstRank(getRankRes);
            }
            else {
                GetRankRes getRankRes= flagDao.getmyRank(userIdx,mountainIdx);
                if(getRankRes.getUserImage()!=null){
                    getRankRes.setUserImage(s3Service.getFileUrl(getRankRes.getUserImage()));
                }
                getFlagRankRes.setMyRank(getRankRes);
            }
        }
        return getFlagRankRes;
    }

    public GetAltitudeRes getAltitude(int userIdx, int mountainIdx,double altitude) throws BaseException {
        GetAltitudeRes getAltitudeRes =new GetAltitudeRes();
        int nowAlti = flagDao.checkhigh(mountainIdx)-(int)altitude;
        getAltitudeRes.setAltitude(nowAlti);
        int admithigh = (flagDao.checkhigh(mountainIdx)*4)/5;
        if(altitude>=admithigh){
            getAltitudeRes.setStatus("T");
        }
        else{
            getAltitudeRes.setStatus("F");
        }
        return getAltitudeRes;
    }

    public List<GetPickRes> getPick(int userIdx) throws BaseException {
        List<GetPickRes> getPickRes= flagDao.getPick(userIdx);
        for(int i=0;i<getPickRes.size();i++){
            if(getPickRes.get(i).getMountainImg()!=null){
                getPickRes.get(i).setMountainImg(s3Service.getFileUrl(getPickRes.get(i).getMountainImg()));
            }
        }
        return getPickRes;
    }

    public char checkPick(int userIdx, int mountainIdx){
        return flagDao.checkPick(userIdx,mountainIdx);
    }

    public int checkFlagExist(Long flagIdx){
        return flagDao.checkFlagExist(flagIdx);
    }

    public int checkFlagWhereUserExist(Long flagIdx,int userIdx){
        return flagDao.checkFlagWhereUserExist(flagIdx,userIdx);
    }
    public int checkFlagReportExist(Long flagIdx, int userIdx){
        return flagDao.checkFlagReportExist(flagIdx,userIdx);
        }

    public int checkJwt(String jwt) {
        int exist = flagDao.checkJwt(jwt);
        return exist;
    }
    public int checkSaveExist(int userIdx,int flagIdx){
        int exist =flagDao.checkSaveExist(userIdx,flagIdx);
        return exist;
    }
    public int checkFlagExist(long flagIdx){
        int exist = flagDao.checkFlagExist(flagIdx);
        return exist;
    }

    public int checkMountainExist(long mountainIdx){
        int exist = flagDao.checkMountainExist(mountainIdx);
        return exist;
    }

    public GetCheckDoubleVisitedRes checkDoubleVisited(int mountainIdx, int userIdx) throws BaseException {
        if (flagDao.checkMountainExist(mountainIdx)==0) {
            throw new BaseException(POST_FLAGS_INVALID_MOUNTAIN);

        }
        if (flagDao.checkDoubleVisited(mountainIdx,userIdx)==1) {
            throw new BaseException(POST_FLAGS_EXISTS_FLAG);

        }
        else {

            return  new GetCheckDoubleVisitedRes(true);
        }
    }

}
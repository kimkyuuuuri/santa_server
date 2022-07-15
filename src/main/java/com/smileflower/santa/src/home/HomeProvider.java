package com.smileflower.santa.src.home;


import com.smileflower.santa.config.BaseException;
import com.smileflower.santa.src.home.model.*;
import com.smileflower.santa.utils.JwtService;
import com.smileflower.santa.utils.S3Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;


//Provider : Read의 비즈니스 로직 처리
@Service
public class HomeProvider {

    private final HomeDao homeDao;
    private final JwtService jwtService;
    private final S3Service s3Service;

    private JdbcTemplate jdbcTemplate;
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public HomeProvider(HomeDao homeDao, JwtService jwtService, S3Service s3Service) {

        this.homeDao = homeDao;
        this.jwtService = jwtService;
        this.s3Service = s3Service;
    }

    public GetHomeRes getHome(int userIdx) throws BaseException {
        GetmyflagMountainRes getmyflagMountainRes = new GetmyflagMountainRes();
        GetHomeRes getHomeRes = new GetHomeRes();
        getHomeRes.setUserIdx(userIdx);
        String userImage=getUserImage(userIdx);
        getHomeRes.setUserImage(userImage);
        String homeStatus= "추천 산";
//        for(int i=1; i<=110; i++) {
//            if (homeDao.checkFlagMountain(i)==1 && homeDao.checkFlagUser(userIdx,i)==1) {
//                homeStatus= "내가 정복한 산";
//                getHomeRes.setHomeStatus(homeStatus);
//                int mountainIdx = i;
//                List<GetHomeMountainRes> getHomeMountainRes = homeDao.getHomeMountain(userIdx, mountainIdx);
//
//                getmyflagMountainRes.getMountain().add(getHomeMountainRes.get(0));
//            }
//        }
        for (int i =1; i<=110;i++){
            if (homeDao.checkFlagMountain(i)==1 && homeStatus.equals("추천 산")){
                homeStatus= "추천 산";
                getHomeRes.setHomeStatus(homeStatus);
                int mountainIdx = i;
                List<GetHomeMountainRes> getHomeMountainRes = homeDao.getHomeMountains(userIdx, mountainIdx);
                getmyflagMountainRes.getMountain().add(getHomeMountainRes.get(0));
            }
        }
        for(int i = 0;i < getmyflagMountainRes.getMountain().size();i++){
            if(getmyflagMountainRes.getMountain().get(i).getMountainImage()!=null){
                getmyflagMountainRes.getMountain().get(i).setMountainImage(s3Service.getFileUrl(getmyflagMountainRes.getMountain().get(i).getMountainImage()));
            }
            if(getmyflagMountainRes.getMountain().get(i).getUserImage()!=null){
                getmyflagMountainRes.getMountain().get(i).setUserImage(s3Service.getFileUrl(getmyflagMountainRes.getMountain().get(i).getUserImage()));
            }
        }
        if (getmyflagMountainRes.getMountain().size()==0){
            getHomeRes.setHomeStatus(homeStatus);
            GetHomeMountainRes getDummy = new GetHomeMountainRes(1,"관악산","https://smileflower-bucket.s3.ap-northeast-2.amazonaws.com/46a34a4a-9ebc-469e-ab7f-2a0d5c98a187.jpg",2,0,"산타",null,0);
            getmyflagMountainRes.getMountain().add(getDummy);
        }


        getHomeRes.setMyflag(getmyflagMountainRes);

        return getHomeRes;

    }

    public int checkFlagMountain(int mountainIdx){
        int exist = homeDao.checkFlagMountain(mountainIdx);
        return exist;
    }

    public int checkFlagUser(int userIdx,int mountainIdx){
        int exist = homeDao.checkFlagUser(userIdx,mountainIdx);
        return exist;
    }

    public String getUserImage(int userIdx){
        String userImage = homeDao.getUserImage(userIdx);
        if(userImage!=null){
            userImage = s3Service.getFileUrl(userImage);
        }
        return userImage;
    }
}
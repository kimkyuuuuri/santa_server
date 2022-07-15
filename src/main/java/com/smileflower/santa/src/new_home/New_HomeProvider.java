package com.smileflower.santa.src.new_home;


import com.smileflower.santa.config.BaseException;
import com.smileflower.santa.config.BaseResponseStatus;
import com.smileflower.santa.src.new_home.model.*;
import com.smileflower.santa.utils.JwtService;
import com.smileflower.santa.utils.S3Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.smileflower.santa.config.BaseResponseStatus.DATABASE_ERROR;
import static com.smileflower.santa.config.BaseResponseStatus.INVALID_USER;


//Provider : Read의 비즈니스 로직 처리
@Service
public class New_HomeProvider {

    private final New_HomeDao newHomeDao;
    private final JwtService jwtService;
    private final S3Service s3Service;

    private JdbcTemplate jdbcTemplate;
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public New_HomeProvider(New_HomeDao newHomeDao, JwtService jwtService, S3Service s3Service) {

        this.newHomeDao = newHomeDao;
        this.jwtService = jwtService;
        this.s3Service = s3Service;
    }

    public GetHomeRes getHome(int userIdx) throws BaseException {

        if (newHomeDao.checkUserIdx(userIdx)!=1) {
            throw new BaseException(INVALID_USER);
        }
        GetHomeRes getHomeRes = newHomeDao.getHomeRes(userIdx);


        for (int i = 0; i < getHomeRes.getGetFlagResList().size(); i++) {
            if (getHomeRes.getGetFlagResList().get(i).getFlagImageUrl() != null)
                getHomeRes.getGetFlagResList().get(i).setFlagImageUrl(s3Service.getFileUrl(getHomeRes.getGetFlagResList().get(i).getFlagImageUrl()));

            if (getHomeRes.getGetFlagResList().get(i).getUserImageUrl() != null)
                getHomeRes.getGetFlagResList().get(i).setUserImageUrl(s3Service.getFileUrl(getHomeRes.getGetFlagResList().get(i).getUserImageUrl()));
        }
        for (int i = 0; i < getHomeRes.getGetUsersResList().size(); i++) {
            if (getHomeRes.getGetUsersResList().get(i).getUserImageUrl() != null)
                getHomeRes.getGetUsersResList().get(i).setUserImageUrl(s3Service.getFileUrl(getHomeRes.getGetUsersResList().get(i).getUserImageUrl()));
        }

        for (int i = 0; i < getHomeRes.getGetMountainsResList().size(); i++) {
            if (getHomeRes.getGetMountainsResList().get(i).getMountainImageUrl() != null)
                getHomeRes.getGetMountainsResList().get(i).setMountainImageUrl(s3Service.getFileUrl(getHomeRes.getGetMountainsResList().get(i).getMountainImageUrl()));
            if (getHomeRes.getGetMountainsResList().get(i).getUserImageUrl() != null)
                getHomeRes.getGetMountainsResList().get(i).setUserImageUrl(s3Service.getFileUrl(getHomeRes.getGetMountainsResList().get(i).getUserImageUrl()));
        }
        if(getUserStatus(userIdx).equals("F")){

            setUserStatus(userIdx);
        }
        return getHomeRes;

    }

    public List<GetFlagMoreRes> getFlagsMoreRes(int userIdx) throws BaseException {
        List<GetFlagMoreRes> getFlagMoreRes = newHomeDao.getFlagsMoreRes(userIdx);


            if(getFlagMoreRes.size()==0)
                throw new BaseException(BaseResponseStatus.EMPTY_PICTURE);

            for (int i = 0; i < getFlagMoreRes.size(); i++) {
                if (getFlagMoreRes.get(i).getFlagImageUrl() != null)
                    getFlagMoreRes.get(i).setFlagImageUrl(s3Service.getFileUrl(getFlagMoreRes.get(i).getFlagImageUrl()));
                if (getFlagMoreRes.get(i).getUserImageUrl() != null)

                    getFlagMoreRes.get(i).setUserImageUrl(s3Service.getFileUrl(getFlagMoreRes.get(i).getUserImageUrl()));
                if (getFlagMoreRes.get(i).getGetCommentRes().size() != 0) {
                    if (getFlagMoreRes.get(i).getGetCommentRes().get(0).getUserImageUrl() != null)
                        getFlagMoreRes.get(i).getGetCommentRes().get(0).setUserImageUrl(s3Service.getFileUrl(getFlagMoreRes.get(i).getGetCommentRes().get(0).getUserImageUrl()));
                }

            }
            return getFlagMoreRes;

    }

    public List<GetUserRes> getUsersRes() throws BaseException {
        List<GetUserRes> getUsersRes = newHomeDao.getUsersRes();


        if(getUsersRes.size()==0)
            throw new BaseException(BaseResponseStatus.EMPTY_USER);

        for (int i = 0; i < getUsersRes.size(); i++) {
            if (getUsersRes.get(i).getUserImageUrl() != null)
                getUsersRes.get(i).setUserImageUrl(s3Service.getFileUrl(getUsersRes.get(i).getUserImageUrl()));

        }
        return getUsersRes;

    }

    public List<GetMountainRes> getMountainsRes(int order) throws BaseException {
        List<GetMountainRes> getMountainsRes = newHomeDao.getMountainsRes(order);


        if(getMountainsRes.size()==0)
            throw new BaseException(BaseResponseStatus.EMPTY_MOUNTAIN);

        for (int i = 0; i < getMountainsRes.size(); i++) {
            if (getMountainsRes.get(i).getUserImageUrl() != null)
                getMountainsRes.get(i).setUserImageUrl(s3Service.getFileUrl(getMountainsRes.get(i).getUserImageUrl()));
            if (getMountainsRes.get(i).getMountainImageUrl() != null)
                getMountainsRes.get(i).setMountainImageUrl(s3Service.getFileUrl(getMountainsRes.get(i).getMountainImageUrl()));

        }
        return getMountainsRes;



    }

    public String getUserStatus(int userIdx){
        String userStatus = newHomeDao.getUserStatus(userIdx);
        return userStatus;
    }
    public void setUserStatus(int userIdx){
         newHomeDao.setUserStatus(userIdx);

    }

    public void modifyNotificationStatus(int notificationIdx){
        newHomeDao.updateNotificationStatus(notificationIdx);

    }

    public List<GetNotificationRes> getNotification(int userIdx) throws BaseException {
        List<GetNotificationRes> getNotificationRes = newHomeDao.getNotificationRes(userIdx);


        return getNotificationRes;

    }
    public int checkUserIdx(int userIdx) throws BaseException{
        try{
            return newHomeDao.checkUserIdx(userIdx);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
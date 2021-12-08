package com.smileflower.santa.src.new_home;


import com.smileflower.santa.config.BaseException;
import com.smileflower.santa.config.BaseResponse;
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

import static com.smileflower.santa.config.BaseResponseStatus.EMPTY_JWT;


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

        GetHomeRes getHomeRes = newHomeDao.getHomeRes(userIdx);

        for (int i = 0; i < getHomeRes.getGetPicturesResList().size(); i++) {
            if (getHomeRes.getGetPicturesResList().get(i).getPictureImageUrl() != null)
                getHomeRes.getGetPicturesResList().get(i).setPictureImageUrl(s3Service.getFileUrl(getHomeRes.getGetPicturesResList().get(i).getPictureImageUrl()));

            if (getHomeRes.getGetPicturesResList().get(i).getUserImageUrl() != null)
                getHomeRes.getGetPicturesResList().get(i).setUserImageUrl(s3Service.getFileUrl(getHomeRes.getGetUsersResList().get(i).getUserImageUrl()));
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
        return getHomeRes;

    }

    public List<GetPicturesRes> getPicturesRes() throws BaseException {
        List<GetPicturesRes> getPicturesRes = newHomeDao.getPicturesRes();


            if(getPicturesRes.size()==0)
                throw new BaseException(BaseResponseStatus.EMPTY_PICTURE);

            for (int i = 0; i < getPicturesRes.size(); i++) {
                if (getPicturesRes.get(i).getPictureImageUrl() != null)
                    getPicturesRes.get(i).setPictureImageUrl(s3Service.getFileUrl(getPicturesRes.get(i).getPictureImageUrl()));
                if (getPicturesRes.get(i).getUserImageUrl() != null)
                    getPicturesRes.get(i).setUserImageUrl(s3Service.getFileUrl(getPicturesRes.get(i).getUserImageUrl()));

            }
            return getPicturesRes;

    }

    public List<GetUsersRes> getUsersRes() throws BaseException {
        List<GetUsersRes> getUsersRes = newHomeDao.getUsersRes();


        if(getUsersRes.size()==0)
            throw new BaseException(BaseResponseStatus.EMPTY_USER);

        for (int i = 0; i < getUsersRes.size(); i++) {
            if (getUsersRes.get(i).getUserImageUrl() != null)
                getUsersRes.get(i).setUserImageUrl(s3Service.getFileUrl(getUsersRes.get(i).getUserImageUrl()));

        }
        return getUsersRes;

    }

    public List<GetMountainsRes> getMountainsRes() throws BaseException {
        List<GetMountainsRes> getMountainsRes = newHomeDao.getMountainsRes();


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
}
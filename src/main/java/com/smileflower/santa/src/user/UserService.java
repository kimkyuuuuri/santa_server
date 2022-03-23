package com.smileflower.santa.src.user;


import com.smileflower.santa.config.BaseException;
import com.smileflower.santa.config.secret.Secret;
import com.smileflower.santa.src.user.model.*;
import com.smileflower.santa.utils.AES128;
import com.smileflower.santa.utils.JwtService;
import com.smileflower.santa.utils.S3Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import javax.transaction.Transactional;
import java.util.List;

import static com.smileflower.santa.config.BaseResponseStatus.*;

// Service Create, Update, Delete 의 로직 처리
@Service
@Transactional
public class UserService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final UserDao userDao;
    private final UserProvider userProvider;
    private final JwtService jwtService;
    private JdbcTemplate jdbcTemplate;
    private final S3Service s3Service;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Autowired
    public UserService(UserDao userDao, UserProvider userProvider, JwtService jwtService,S3Service s3Service) {
        this.userDao = userDao;
        this.userProvider = userProvider;
        this.jwtService = jwtService;
        this.s3Service=s3Service;

    }

    //POST
    public PostUserRes createUser(PostUserReq postUserReq) throws BaseException {
        //중복
        if (postUserReq.getPassword().equals(postUserReq.getPasswordCheck())) {
            if (userProvider.checkName(postUserReq.getName()) == 1) {
                throw new BaseException(POST_USER_EXISTS_NAME);
            }
            if (userProvider.checkEmailId(postUserReq.getEmailId()) == 1) {
                throw new BaseException(POST_USERS_EXISTS_EMAIL);
            }

            String pwd;
            try {
                //암호화
                pwd = new AES128(Secret.USER_INFO_PASSWORD_KEY).encrypt(postUserReq.getPassword());
                postUserReq.setPassword(pwd);
            } catch (Exception ignored) {
                throw new BaseException(PASSWORD_ENCRYPTION_ERROR);
            }

            int userIdx = userDao.createUser(postUserReq);

            //jwt발급
            String jwt = jwtService.createJwt(userIdx);
            return new PostUserRes(jwt, userIdx);
        } else {
            throw new BaseException(PASSWORD_CONFIRM_ERROR);
        }

    }



    @Transactional
    public PostUserLoginRes loginUser(PostUserLoginReq postUserLoginReq) throws BaseException {
        //System.out.println("테스트1:"+postUserLoginReq.getEmailId());

        int userIdx = userProvider.checkAccount(postUserLoginReq.getEmailId(), postUserLoginReq.getPassword());
        String name;
        if (userProvider.checkLogExist(userIdx) != 1) {  // 신규로 처음 로그인하는 사람을 위한
            name = userDao.recordLog(userIdx, "I");

        } else {
            name = userDao.recordLog(userIdx, "I");

        }

        //jwt발급
        String jwt = jwtService.createJwt(userIdx);
        int jwtIdx = userDao.postJwt(jwt);
        //userDao.updateUserToken(userIdx,postUserLoginReq.getPushToken(),postUserLoginReq.getTokenType());
        userDao.updateUserToken(userIdx,postUserLoginReq.getPushToken(),"I");

        userDao.patchUserIsFirst(userIdx);
        return new PostUserLoginRes(jwt, userIdx, name);
    }


    public PatchUserLogoutRes patchLogout(int userIdx) throws BaseException {
        if (userProvider.checkLog(userIdx).equals("I")) {
            PatchUserLogoutRes patchUserLogoutRes = userDao.patchLogout(userIdx);
            int jwtIdx = userDao.patchJwtStatus(jwtService.getJwt());

            return patchUserLogoutRes;
        } else {
            throw new BaseException(ALREADY_LOGOUT);
        }
    }

    public PostNameRes checkName(PostNameReq postNameReq) throws BaseException {
        PostNameRes postNameRes = new PostNameRes();
        if (userProvider.checkName(postNameReq.getName()) == 1) {
            postNameRes.setStatus("중복된 닉네임 입니다.");
            postNameRes.setBool(false);
            return postNameRes;
        }
         else {
            postNameRes.setStatus("사용할 수 있는 닉네임 입니다.");
            postNameRes.setBool(true);
            return postNameRes;
        }
    }

    public DeleteUserRes deleteUser(int userIdx) throws BaseException {

        if (userProvider.checkUserIdx(userIdx)!=1) {
            throw new BaseException(INVALID_USER);
        }
        GetUserRes getUserRes=userDao.getUserRes(userIdx);
        List<GetFlagRes> getFlagRes=userDao.getFlagRes(userIdx);
        List<GetPictureRes> getPictureRes=userDao.getPictueRes(userIdx);
        for(int i=0;i<getFlagRes.size();i++){
            s3Service.deleteFile(getFlagRes.get(i).getPictureImgUrl());
        }
        for(int i=0;i<getPictureRes.size();i++){
            s3Service.deleteFile(getPictureRes.get(i).getPictureImgUrl());
        }


        if(getUserRes.getPictureImgUrl()!=null)
            s3Service.deleteFile(getUserRes.getPictureImgUrl());
        int delete = userDao.deleteUser(userIdx);

        return new DeleteUserRes(userIdx);

    }

    public void modifyNickname(int userIdx,String name) throws BaseException{
        if (userProvider.checkUserIdx(userIdx)!=1) {
            throw new BaseException(INVALID_USER);
        }
        userDao.modifyNickname(userIdx,name);
    }

    public void patchUserStatus(int userIdx) throws BaseException{
        if (userProvider.checkUserIdx(userIdx)!=1) {
            throw new BaseException(INVALID_USER);
        }
        userDao.patchUserStatus(userIdx);

    }

    public void patchUserPushToken(int userIdx,String pushToken,String tokenType) throws BaseException{
        if (userProvider.checkUserIdx(userIdx)!=1) {
            throw new BaseException(INVALID_USER);
        }
        userDao.updateUserToken(userIdx,pushToken,tokenType);

    }

    public void patchUserIsFirst(int userIdx) throws BaseException{
        if (userProvider.checkUserIdx(userIdx)!=1) {
            throw new BaseException(INVALID_USER);
        }
        userDao.patchUserIsFirst(userIdx);

    }
    @Scheduled(cron = "0 0 0 * * *")	// 두달 후 테이블에 userIdx 0으로 바꾸기
    public void updateUserIdx() throws Exception {

        List<GetUserIdxRes> getUserIdxRes=userDao.getTwoMonthAgoDeletedUser();
        for (int i=0;i<getUserIdxRes.size();i++){
            userDao.updateUserIdx(getUserIdxRes.get(i).getUserIdx());
        }

    }

    @Scheduled(cron = "0 0 0 * * *")	// 6개월 후 삭제하기
    public void deleteUserIdx() throws Exception {
        List<GetUserIdxRes> getUserIdxRes=userDao.getSixMonthAgoDeletedUser();
        for (int i=0;i<getUserIdxRes.size();i++){
            userDao.deleteUser(getUserIdxRes.get(i).getUserIdx());
        }

    }
    public void agree(int userIdx,PostUserAgreeRes postUserAgreeRes){
        userDao.patchUserIsFirst(userIdx);
        if(postUserAgreeRes.getIsAgree().equals("N")) {
            userDao.patchUserTokenType(userIdx);
        }

    }
    public void restoreUser(PatchUserStatusReq patchUserStatusRes) throws BaseException {
        int userIdx = userProvider.checkAccount(patchUserStatusRes.getEmailId(), patchUserStatusRes.getPassword());


            userDao.patchUserStatusForRestore(userIdx,patchUserStatusRes.getName());


    }

}



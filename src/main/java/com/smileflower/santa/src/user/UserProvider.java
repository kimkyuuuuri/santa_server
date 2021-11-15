package com.smileflower.santa.src.user;


import com.smileflower.santa.config.BaseException;
import com.smileflower.santa.config.secret.Secret;
import com.smileflower.santa.src.user.model.*;
import com.smileflower.santa.utils.AES128;
import com.smileflower.santa.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.List;

import static com.smileflower.santa.config.BaseResponseStatus.*;

//Provider : Read의 비즈니스 로직 처리
@Service
public class UserProvider {

    private final UserDao userDao;
    private final JwtService jwtService;

    private JdbcTemplate jdbcTemplate;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Autowired
    public UserProvider(UserDao userDao, JwtService jwtService) {
        this.userDao = userDao;
        this.jwtService = jwtService;
    }
    public int checkUserIdx(int userIdx) throws BaseException{
        try{
            return userDao.checkUserIdx(userIdx);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }



    public int checkName(String name){
        return userDao.checkName(name);
    }



    public int checkEmailId(String email){
        return userDao.checkEmail(email);
    }



    public int checkAccount(String email,String password) throws BaseException {
        if(userDao.checkEmail(email)!=1){
            throw new BaseException(POST_USERS_NONEXIST_ACCOUNT);
        }

        PostUserLoginPWRes postUserLoginPWRes = userDao.checkAccount(email); // 여기서 잘 못받아오면 에러나옴 그러므로 미리 위에서 체크하는 것임

        String realpw;
        try{
            realpw = new AES128(Secret.USER_INFO_PASSWORD_KEY).decrypt(postUserLoginPWRes.getPassword());
        } catch (Exception ignored) {
            throw new BaseException(PASSWORD_ENCRYPTION_ERROR);
        }
        //System.out.println("테스트2:"+postUserLoginPWRes.getEmailId());
        //System.out.println("테스트3:"+password);
        //System.out.println("테스트4:"+realpw);

        if(postUserLoginPWRes.getEmailId().equals(email)){
            if(realpw.equals(password)){
                return postUserLoginPWRes.getUserIdx();  // 둘 다 맞음
            }
            else{
                throw new BaseException(POST_USERS_NONEXIST_ACCOUNT);
            }
        }

        else{
            throw new BaseException(POST_USERS_NONEXIST_ACCOUNT);
        }
    }


    public String checkLog(int userIdx){
        String logStatus = userDao.checkLog(userIdx);
        return logStatus;
    }



    public int checkLogExist(int userIdx){
        int exist = userDao.checkLogExist(userIdx);
        return exist;
    }


    public int checkJwt(String jwt) {
        int exist = userDao.checkJwt(jwt);
        return exist;
    }

    public GetAutoRes getAuto(String jwt, int userIdx) throws BaseException {
        GetAutoRes getAutoRes = new GetAutoRes();
        if (userDao.getAuto(userIdx)=='I') {
            getAutoRes.setStauts("T");
        }
        else{
            getAutoRes.setStauts("F");
        }
        getAutoRes.setUserIdx(userIdx);
        getAutoRes.setEmailId(userDao.getEmailId(userIdx));
        return getAutoRes;
    }



}

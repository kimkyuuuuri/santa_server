
package com.smileflower.santa.src.social_login;

import com.smileflower.santa.config.BaseException;
import com.smileflower.santa.config.secret.Secret;
import com.smileflower.santa.src.user.model.PostUserLoginPWRes;
import com.smileflower.santa.utils.AES128;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.smileflower.santa.config.BaseResponseStatus.PASSWORD_ENCRYPTION_ERROR;
import static com.smileflower.santa.config.BaseResponseStatus.POST_USERS_NONEXIST_ACCOUNT;

@RequiredArgsConstructor
@Service
public class Social_loginProvider {
    @Autowired
    private final Social_loginDao socialloginDao;

    final Logger logger = LoggerFactory.getLogger(this.getClass());
    public int checkAccount(String email) throws BaseException {
        if(socialloginDao.checkEmail(email)!=1){
            throw new BaseException(POST_USERS_NONEXIST_ACCOUNT);
        }

        int userIdx = socialloginDao.checkAccount(email); // 여기서 잘 못받아오면 에러나옴 그러므로 미리 위에서 체크하는 것임

        return userIdx;


    }


    public String checkLog(int userIdx){
        String logStatus = socialloginDao.checkLog(userIdx);
        return logStatus;
    }



    public int checkLogExist(int userIdx){
        int exist = socialloginDao.checkLogExist(userIdx);
        return exist;
    }


    public int checkName(String name){
        return socialloginDao.checkName(name);
    }



    public int checkEmailId(String email){
        return socialloginDao.checkEmail(email);
    }
    public int checkJwt(String jwt) {
        int exist = socialloginDao.checkJwt(jwt);
        return exist;
    }

}
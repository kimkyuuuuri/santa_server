
package com.smileflower.santa.src.email;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.smileflower.santa.config.BaseException;
import com.smileflower.santa.config.BaseResponseStatus;
import com.smileflower.santa.src.email.model.PostAuthReq;
import com.smileflower.santa.src.user.UserDao;

@RequiredArgsConstructor
@Service
public class EmailProvider {
    @Autowired
    private final EmailDao emailDao;
    @Autowired
    private final UserDao userDao;
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    public int checkEmail(String email) throws BaseException {
        try{
            return emailDao.checkAuthEmail(email);
        } catch (Exception e){
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    public void checkAuth(PostAuthReq postAuthReq) throws BaseException {
        if(emailDao.checkAuthEmail(postAuthReq.getEmail()) == 0) {
            throw new BaseException(BaseResponseStatus.POST_AUTH_EMPTY_EMAIL);
        }
        if(emailDao.checkAuthCode(postAuthReq) == 0) {
            throw new BaseException(BaseResponseStatus.INVALID_AUTH_EMAIL_CODE);
        }
    }

    public int checkDuplicateEmail(String email) throws BaseException{
        try{
            return userDao.checkEmail(email);
        } catch (Exception exception){
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    public String checkpw(String email) throws BaseException{
       try{
            return emailDao.checkpw(email);
        } catch (Exception exception){
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }
}
package com.smileflower.santa.src.flags;


import com.smileflower.santa.config.BaseException;
import com.smileflower.santa.config.secret.Secret;
import com.smileflower.santa.profile.model.dto.ReportFlagResponse;
import com.smileflower.santa.src.flags.model.*;
import com.smileflower.santa.src.flags.FlagDao;
import com.smileflower.santa.src.flags.FlagProvider;
import com.smileflower.santa.utils.AES128;
import com.smileflower.santa.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.fasterxml.jackson.databind.ser.Serializers;
import org.springframework.stereotype.Service;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;


import javax.sql.DataSource;

import static com.smileflower.santa.config.BaseResponseStatus.*;

// Service Create, Update, Delete 의 로직 처리
@Service
public class FlagService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final FlagDao flagDao;
    private final FlagProvider flagProvider;
    private final JwtService jwtService;
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Autowired
    public FlagService(FlagDao flagDao, FlagProvider flagProvider, JwtService jwtService) {
        this.flagDao = flagDao;
        this.flagProvider = flagProvider;
        this.jwtService = jwtService;

    }

    public PostFlagPictureRes createFlag(PostFlagPictureReq postFlagPictureReq, int mountainIdx, int userIdx) throws BaseException {
       if (postFlagPictureReq.getPictureUrl().length() > 0) {
            int flagIdx = flagDao.createFlag(postFlagPictureReq, mountainIdx, userIdx);

            return new PostFlagPictureRes(flagIdx);
        } else {
            throw new BaseException(RESPONSE_ERROR);
        }
    }
    public PostFlagHardRes createHard(PostFlagHardReq postFlagHardReq, int mountainIdx, int userIdx) throws BaseException {
        if (postFlagHardReq.getDifficulty() >= 0 && postFlagHardReq.getDifficulty() <= 10) {
            PostFlagHardRes postFlagHardRes = new PostFlagHardRes();
            int difficultyIdx = flagDao.createHard(postFlagHardReq, mountainIdx, userIdx);

            return postFlagHardRes;
        } else {
            throw new BaseException(RESPONSE_ERROR);
        }
    }

    @Transactional
    public PatchPickRes patchPick(int userIdx, int mountainIdx) throws BaseException{
        int exist=flagDao.checkPickExist(userIdx,mountainIdx);
        if(exist==1) {
            char status = flagProvider.checkPick(userIdx, mountainIdx);
            if (status == 'T') {
                PatchPickRes patchPickRes = flagDao.patchPick("F", userIdx, mountainIdx);
                return patchPickRes;
            }
            else{
                PatchPickRes patchPickRes = flagDao.patchPick("T", userIdx,mountainIdx);
                return patchPickRes;
            }
        } else{
            PatchPickRes patchPickRes =flagDao.createPick("T",userIdx,mountainIdx);
            return patchPickRes;
        }

    }

    @Transactional
    public PostFlagReportRes report(int userIdx, Long flagIdx) {
        flagDao.report(flagIdx,userIdx);
        return new PostFlagReportRes(flagIdx,flagDao.getReportCount(flagIdx));
    }

}
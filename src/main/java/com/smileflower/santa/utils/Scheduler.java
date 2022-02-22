package com.smileflower.santa.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Scheduler {
    @Scheduled(cron = "0 0 0 * * *")	// 두달 후 테이블에 userIdx 0으로 바꾸기
            public void updateUserIdx() throws Exception {

        //status가 f이고 updateAt 현재 시간과 2달 차이달 때 (select 문)
        // update Status
    }
}
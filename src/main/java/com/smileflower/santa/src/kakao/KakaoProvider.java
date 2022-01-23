
package com.smileflower.santa.src.kakao;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class KakaoProvider {
    @Autowired
    private final KakaoDao kakaoDao;

    final Logger logger = LoggerFactory.getLogger(this.getClass());


}
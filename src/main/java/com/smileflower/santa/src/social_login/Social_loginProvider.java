
package com.smileflower.santa.src.social_login;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class Social_loginProvider {
    @Autowired
    private final Social_loginDao socialloginDao;

    final Logger logger = LoggerFactory.getLogger(this.getClass());


}
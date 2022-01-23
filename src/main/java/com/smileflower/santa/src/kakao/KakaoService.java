package com.smileflower.santa.src.kakao;

import com.smileflower.santa.src.email.EmailDao;
import com.smileflower.santa.src.email.EmailProvider;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.smileflower.santa.config.secret.Secret;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import com.smileflower.santa.utils.AES128;
import org.thymeleaf.spring5.SpringTemplateEngine;
import com.smileflower.santa.config.BaseException;
import com.smileflower.santa.config.BaseResponseStatus;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;

import static com.smileflower.santa.utils.Email.*;

@RequiredArgsConstructor
@Service
public class KakaoService {

    @Autowired
    private final EmailDao emailDao;
    @Autowired
    private final EmailProvider emailProvider;
    @Autowired
    private final JavaMailSender emailSender;
    private final SpringTemplateEngine templateEngine;

    final Logger logger = LoggerFactory.getLogger(this.getClass());




}
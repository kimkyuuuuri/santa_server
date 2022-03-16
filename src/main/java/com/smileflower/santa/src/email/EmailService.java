package com.smileflower.santa.src.email;

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
public class EmailService {

    @Autowired
    private final EmailDao emailDao;
    @Autowired
    private final EmailProvider emailProvider;
    @Autowired
    private final JavaMailSender emailSender;
    private final SpringTemplateEngine templateEngine;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Transactional
    public void sendEmailMessage(String email) throws BaseException {
        if(emailProvider.checkDeletedUser(email) == 1)
            throw new BaseException(BaseResponseStatus.POST_USER_DELETED_USER);
        // 회원가입 된 이메일인지 확인
        if(emailProvider.checkDuplicateEmail(email) == 1)
            throw new BaseException(BaseResponseStatus.POST_AUTH_EXISTS_EMAIL);


        String pw = createKey();
        MimeMessage message = emailSender.createMimeMessage();

        try{
            message.addRecipients(MimeMessage.RecipientType.TO, email); // 보낼 이메일 설정
            message.setSubject("[산타] " + "인증번호를 안내해드립니다."); // 이메일 제목
            message.setText(setContext(pw, templateEngine), "utf-8", "html"); // 내용 설정(Template Process)


            emailSender.send(message); // 이메일 전송

            // 이메일 이미 있으면,
            if(emailProvider.checkEmail(email) == 1) emailDao.updateAuthCode(email, pw);
            else emailDao.createAuth(email, pw);
        }
        catch (MessagingException e){
            throw new BaseException(BaseResponseStatus.SEND_MAIL_ERROR);
        }
        catch (Exception exception){
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    @Transactional
    public void emailpassword(String email) throws BaseException {

        // 회원가입 된 이메일인지 확인
        if(emailProvider.checkDuplicateEmail(email) == 0)
            throw new BaseException(BaseResponseStatus.POST_USERS_EMPTY_USER);

        MimeMessage message = emailSender.createMimeMessage();

        String realpw;
        String nonreal;
        nonreal = emailProvider.checkpw(email);
        System.out.print(nonreal);
        try {

            realpw = new AES128(Secret.USER_INFO_PASSWORD_KEY).decrypt(emailProvider.checkpw(email));
        }catch (Exception ignored) {
            throw new BaseException(BaseResponseStatus.PASSWORD_ENCRYPTION_ERROR);
        }
        try{
            message.addRecipients(MimeMessage.RecipientType.TO, email);
            message.setSubject("[산타] " + "비밀번호를 안내해드립니다."); // 이메일 제목
            message.setText(findPassword(realpw, templateEngine), "utf-8", "html");

            emailSender.send(message); // 이메일 전송

        }
        catch (MessagingException e){
            throw new BaseException(BaseResponseStatus.SEND_MAIL_ERROR);
        }
        catch (Exception exception){
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

}
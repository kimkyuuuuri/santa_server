
package com.smileflower.santa.src.social_login;

import com.smileflower.santa.src.social_login.model.AppleUser;
import com.smileflower.santa.src.social_login.model.Email;
import com.smileflower.santa.src.user.model.PostUserLoginPWRes;
import com.smileflower.santa.src.user.model.PostUserReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;


@Repository
public class Social_loginDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public AppleUser insertUser(AppleUser user) {
        String query = "insert into user (emailId, pw, kakao, apple, userImageUrl, name) VALUES (?,?,?,?,?,?)";
        Object[] params = new Object[]{user.getEmailId().getEmail(),user.getPasswd(),user.getIsKakao(),
                user.getIsApple(),user.getUserImageUrl(),user.getName()};
        this.jdbcTemplate.update(query, params);

        String lastInserIdQuery = "select last_insert_id()";
        return user;
    }


    public boolean findByEmail(Email email) {
        String query = "select Count(*) from user where emailid = ?";
        String param = email.getEmail();
        int count = this.jdbcTemplate.queryForObject(query, new Object[]{param}, Integer.class);

        if (count == 0)
            return false;
        else
            return true;
    }


    public boolean findByIdx(Long userIdx) {
        String query = "select Count(*) from user where userIdx = ?";
        Long param = userIdx;
        int count = this.jdbcTemplate.queryForObject(query, new Object[]{param}, Integer.class);

        if (count == 0)
            return false;
        else
            return true;
    }

    public int createKakaoUser(String name,String Email){
        this.jdbcTemplate.update("insert into user (createdAt,  emailId, name ,pw, kakao ) VALUES (NOW(),?,?,?,?)",
                new Object[]{Email, name ,"kakao",'T'}
        );
        return this.jdbcTemplate.queryForObject("select last_insert_id()",int.class);
    }

    public int checkEmail(String email){
        return this.jdbcTemplate.queryForObject("select exists(select emailId from user where emailId = ?)",
                int.class,
                email);

    }
    public int checkAccount(String email){
        return this.jdbcTemplate.queryForObject("select  userIdx from user where emailId=?",
                (rs, rowNum) -> rs.getInt("userIdx"),
                email);
    }



    public int checkUserIdx(int userIdx){
        String checkUserQuery = "select exists(select userIdx from user where userIdx = ?)";
        int checkUserParams = userIdx;
        return this.jdbcTemplate.queryForObject(checkUserQuery,
                int.class,
                checkUserParams);
    }


    public String recordLog(int userIdx,String status){
        this.jdbcTemplate.update("insert into loghistory (createdAt, userIdx,status ) VALUES (NOW(),?,?)",
                userIdx,status
        );
        return this.jdbcTemplate.queryForObject("select name from user\n" +
                        "where userIdx=?",
                String.class,
                userIdx);
    }

    public int postJwt(String jwt){
        this.jdbcTemplate.update("insert into jwtmanagement (createdAt,status,jwt) VALUES (NOW(),'T',?)",
                jwt
        );
        return this.jdbcTemplate.queryForObject("select last_insert_id()",int.class);
    }

    public int checkName(String name){
        return this.jdbcTemplate.queryForObject("select exists(select name from user where name = ?)",
                int.class,
                name);
    }


    public String checkLog(int userIdx){
        return this.jdbcTemplate.queryForObject("select status from loghistory\n" +
                        "where loghistoryIdx=(select max(loghistoryIdx) from (select loghistoryIdx from loghistory where userIdx=?) a)",
                String.class,
                userIdx);
    }
    public int checkLogExist(int userIdx){
        return this.jdbcTemplate.queryForObject("select EXISTS(select loghistoryIdx from loghistory where userIdx=?) as exist",
                int.class,
                userIdx);
    }
    public String setStatus(String status) {
        return null;
    }

    public int checkJwt(String jwt){
        return this.jdbcTemplate.queryForObject("select EXISTS(select status from jwtmanagement\n" +
                        "where jwt=? and status='F') as exist",
                int.class,
                jwt);
    }


    public String getStatusByToken(String refreshToken) {
        return null;
    }
}
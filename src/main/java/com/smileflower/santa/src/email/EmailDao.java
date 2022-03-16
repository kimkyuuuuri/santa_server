
package com.smileflower.santa.src.email;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import com.smileflower.santa.src.email.model.*;

import javax.sql.DataSource;


@Repository
public class EmailDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public String checkpw(String email){
        return this.jdbcTemplate.queryForObject("select pw from user where emailId = ?",String.class,email);
    }

    public int updateAuthCode(String email, String pw){
        Object[] createAuthparamas = new Object[]{pw, email};
        return this.jdbcTemplate.update("update auth set code = ? where email = ? and status = 'T'", createAuthparamas);
    }


    public int createAuth(String email, String pw){
        String createAuthQuery = "insert into auth (email, code) values (?,?)";
        Object[] createAuthparamas = new Object[]{email, pw};
        this.jdbcTemplate.update(createAuthQuery, createAuthparamas);

        String lastInserIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInserIdQuery,int.class);
    }

    public int checkAuthEmail(String email){
        String checkEmailQuery = "select exists(select email from auth where email = ? and status = 'T')";
        String checkEmailParams = email;
        return this.jdbcTemplate.queryForObject(checkEmailQuery,
                int.class,
                checkEmailParams);
    }

    public int checkAuthCode(PostAuthReq postAuthReq){
        String checkEmailQuery = "select exists(select email,code from auth where email = ? and code = ? and status = 'T')";
        String checkEmailParams = postAuthReq.getEmail();
        int checkPwParams = postAuthReq.getCode();
        return this.jdbcTemplate.queryForObject(checkEmailQuery,
                int.class,
                checkEmailParams, checkPwParams);
    }

    public int checkDeletedUserExist(String email){
        String checkEmailQuery = "select exists(select userIdx from user where emailId = ? and status='F')";
        String checkEmailParams = email;

        return this.jdbcTemplate.queryForObject(checkEmailQuery,
                int.class,
                checkEmailParams);
    }
}
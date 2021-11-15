package com.smileflower.santa.apple.repository;

import com.smileflower.santa.apple.model.domain.AppleUser;
import com.smileflower.santa.apple.model.domain.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class AppleJdbcRepository implements AppleRepository{

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public AppleUser save(AppleUser user) {
        String query = "insert into user (emailId, pw, kakao, apple, userImageUrl, name) VALUES (?,?,?,?,?,?)";
        Object[] params = new Object[]{user.getEmailId().getEmail(),user.getPasswd(),user.getIsKakao(),
        user.getIsApple(),user.getUserImageUrl(),user.getName()};
        this.jdbcTemplate.update(query, params);

        String lastInserIdQuery = "select last_insert_id()";
        return user;
    }

    @Override
    public boolean findByEmail(Email email) {
        String query = "select Count(*) from user where emailid = ?";
        String param = email.getEmail();
        int count = this.jdbcTemplate.queryForObject(query, new Object[]{param}, Integer.class);

        if (count == 0)
            return false;
        else
            return true;
    }

    @Override
    public boolean findByIdx(Long userIdx) {
        String query = "select Count(*) from user where userIdx = ?";
        Long param = userIdx;
        int count = this.jdbcTemplate.queryForObject(query, new Object[]{param}, Integer.class);

        if (count == 0)
            return false;
        else
            return true;
    }

    @Override
    public String setStatus(String status) {
        return null;
    }

    @Override
    public String getStatusByToken(String refreshToken) {
        return null;
    }
}

package com.smileflower.santa.src.user;


import com.smileflower.santa.src.user.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class UserDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    public int createUser(PostUserReq postUserReq){
        this.jdbcTemplate.update("insert into user (createdAt,  emailId, name ,pw ) VALUES (NOW(),?,?,?)",
                new Object[]{postUserReq.getEmailId(), postUserReq.getName() ,postUserReq.getPassword()}
        );
        return this.jdbcTemplate.queryForObject("select last_insert_id()",int.class);
    }



    public int checkName(String name){
        return this.jdbcTemplate.queryForObject("select exists(select name from user where name = ?)",
                int.class,
                name);
    }



    public int checkEmail(String email){
        return this.jdbcTemplate.queryForObject("select exists(select emailId from user where emailId = ?)",
                int.class,
                email);

    }



    public PostUserLoginPWRes checkAccount(String email){
        return this.jdbcTemplate.queryForObject("select emailId, pw, userIdx from user where emailId=?",
                (rs, rowNum) -> new PostUserLoginPWRes(
                        rs.getString("emailId"),
                        rs.getString("pw"),
                        rs.getInt("userIdx")),
                email);
    }

    public int deleteUser(int userIdx) {
        String userQuery = "delete from user where userIdx = ?";
        String pictureQuery="delete from picture where userIdx=?";
        String flagQuery="delete from flag where userIdx=?";

        Object[] params = new Object[]{userIdx};
        this.jdbcTemplate.update(pictureQuery,params);
        this.jdbcTemplate.update(flagQuery,params);
        int changedCnt = this.jdbcTemplate.update(userQuery,params);
        return changedCnt;
    }


    public char getAuto(int userIdx){
        return this.jdbcTemplate.queryForObject("select status from loghistory where userIdx=? ORDER BY createdAt DESC LIMIT 1",char.class,
                userIdx);
    }
    public String getEmailId(int userIdx){
        return this.jdbcTemplate.queryForObject("select emailId from user where userIdx=?",String.class,
                userIdx);
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



    public PatchUserLogoutRes patchLogout(int userIdx){
        this.jdbcTemplate.update("update loghistory set status='O', updatedAt =NOW()\n" +
                        "where loghistoryIdx=(select max(loghistoryIdx) from (select loghistoryIdx from loghistory where loghistory.userIdx=?) a);",
                userIdx
        );
        return this.jdbcTemplate.queryForObject("select loghistory.loghistoryIdx,loghistory.userIdx,loghistory.status,user.name from loghistory\n" +
                        "inner join user\n" +
                        "on user.userIdx=loghistory.userIdx\n" +
                        "where loghistory.loghistoryIdx=(select max(loghistoryIdx) from (select loghistoryIdx from loghistory where loghistory.userIdx=?) a)",
                (rs, rowNum) -> new PatchUserLogoutRes(
                        rs.getInt("loghistoryIdx"),
                        rs.getInt("userIdx"),
                        rs.getString("status"),
                        rs.getString("name")),
                userIdx);
    }
    


    public int getUserIdxByEmail(String emailId){
        return this.jdbcTemplate.queryForObject("select userIdx from User\n" +
                        "where emailId=?",
                int.class,
                emailId);
    }

    public List<GetFlagRes> getFlagRes(int userIdx){
        return this.jdbcTemplate.query("select pictureUrl as pictureImgUrl from flag where userIdx=?",
                (rs, rowNum) -> new GetFlagRes(
                        rs.getString("pictureImgUrl")),

                userIdx);
    }

    public List<GetPictureRes> getPictueRes(int userIdx){
        return this.jdbcTemplate.query("select imgUrl as pictureImgUrl from picture where userIdx=?",
                (rs, rowNum) -> new GetPictureRes(
                        rs.getString("pictureImgUrl")),

                userIdx);
    }

    public GetUserRes getUserRes(int userIdx){
        return this.jdbcTemplate.queryForObject("select userImageUrl as pictureImgUrl from user where userIdx=?",
                (rs, rowNum) -> new GetUserRes(
                        rs.getString("pictureImgUrl")),

                userIdx);
    }
    public String getUserNameByEmail(String emailId){
        return this.jdbcTemplate.queryForObject("select name from User\n" +
                        "where emailId=?",
                String.class,
                emailId);
    }



    public int postJwt(String jwt){
        this.jdbcTemplate.update("insert into jwtmanagement (createdAt,status,jwt) VALUES (NOW(),'T',?)",
                jwt
        );
        return this.jdbcTemplate.queryForObject("select last_insert_id()",int.class);
    }


    public int patchJwtStatus(String jwt){
        this.jdbcTemplate.update("update jwtmanagement set status='F',updatedAt=now() where jwt=?",
                jwt
        );
        return 1;
    }


    public int checkJwt(String jwt){
        return this.jdbcTemplate.queryForObject("select EXISTS(select status from jwtmanagement\n" +
                        "where jwt=? and status='F') as exist",
                int.class,
                jwt);
    }

    public void modifyNickname(int userIdx,String name){
        this.jdbcTemplate.update("UPDATE user SET name = ? WHERE userIdx = ?",
                name,userIdx);
    }

    public void patchUserStatus(int userIdx){
        this.jdbcTemplate.update("UPDATE user SET status = 'F' WHERE userIdx = ?",
                userIdx);
    }
}

package com.smileflower.santa.src.picture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;


@Repository
public class PictureDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {

        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
    public int checkSaveExist(int userIdx,int pictureIdx){
        return this.jdbcTemplate.queryForObject("select EXISTS(select saveIdx from save where userIdx=? and pictureIdx=? and status='t') as exist",
                int.class,
                userIdx,pictureIdx);
    }
    public int postPictureSaveRes(int userIdx,int pictureIdx){
        return this.jdbcTemplate.update("insert into save (userIdx,pictureIdx) VALUES (? , ?)",

                userIdx,pictureIdx);
    }
    public int patchPictureSaveRes(int userIdx,int pictureIdx){
        return this.jdbcTemplate.update("update save set status='f' where userIdx=? and pictureIdx=? order by createdAt desc limit 1",

                userIdx,pictureIdx);
    }

    public int checkJwt(String jwt){
        return this.jdbcTemplate.queryForObject("select EXISTS(select status from jwtmanagement\n" +
                        "where jwt=? and status='F') as exist",
                int.class,
                jwt);
    }

    public int checkPictureExist(int pictureIdx){
        return this.jdbcTemplate.queryForObject("select EXISTS(select pictureIdx from picture where pictureIdx=? and status='t') as exist",
                int.class,
                pictureIdx);
    }
}
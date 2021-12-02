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
        return this.jdbcTemplate.queryForObject("insert into save (userIdx,pictureIdx ) VALUES (?,?)",
                int.class,
                userIdx,pictureIdx);
    }
    public int patchPictureSaveRes(int userIdx,int pictureIdx){
        return this.jdbcTemplate.queryForObject("update save set status='f'",
                int.class,
                userIdx,pictureIdx);
    }

    public int checkJwt(String jwt){
        return this.jdbcTemplate.queryForObject("select EXISTS(select status from jwtmanagement\n" +
                        "where jwt=? and status='F') as exist",
                int.class,
                jwt);
    }


}
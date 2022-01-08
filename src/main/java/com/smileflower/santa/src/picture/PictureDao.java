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
        return this.jdbcTemplate.queryForObject("select EXISTS(select picturesaveIdx from picturesave where userIdx=? and pictureIdx=? and status='t') as exist",
                int.class,
                userIdx,pictureIdx);
    }
    public int postPictureSaveRes(int userIdx,int pictureIdx){
        return this.jdbcTemplate.update("insert into picturesave (userIdx,pictureIdx) VALUES (? , ?)",

                userIdx,pictureIdx);
    }
    public int patchPictureSaveRes(int userIdx,int pictureIdx){
        return this.jdbcTemplate.update("update picturesave set status='f' where userIdx=? and pictureIdx=? order by createdAt desc limit 1",

                userIdx,pictureIdx);
    }

    public int checkJwt(String jwt){
        return this.jdbcTemplate.queryForObject("select EXISTS(select status from jwtmanagement\n" +
                        "where jwt=? and status='F') as exist",
                int.class,
                jwt);
    }

    public int checkPictureExist(long pictureIdx){
        return this.jdbcTemplate.queryForObject("select EXISTS(select pictureIdx from picture where pictureIdx=? and status='t') as exist",
                int.class,
                pictureIdx);
    }
    public int checkPictureWhereUserExist(Long pictureIdx,int userIdx){
        return this.jdbcTemplate.queryForObject("select Exists(select pictureIdx from picture\n" +
                "                where status='T' and pictureIdx=? and userIdx=?) as FlagExist", int.class,pictureIdx,userIdx);
    }
    public boolean deletePicture(Long pictureIdx) {
        String query = "delete from picture where pictureIdx = ?";
        Object[] params = new Object[]{pictureIdx};
        int changedCnt = this.jdbcTemplate.update(query,params);
        return changedCnt==1 ? true : false;
    }

    public Long report(Long  pictureIdx, int userIdx) {
        String query = "insert into picturereport (userIdx, pictureIdx) VALUES (?,?)";
        Object[] params = new Object[]{userIdx,pictureIdx};
        this.jdbcTemplate.update(query, params);
        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,Long.class);
    }
    public int checkPictureReportExist(Long pictureIdx,int userIdx){
        return this.jdbcTemplate.queryForObject("select Exists(select picturereportIdx from picturereport" +
                "                where status='T' and pictureIdx=? and userIdx=? ) as reportExist", int.class,pictureIdx,userIdx);
    }
    public int getReportCount(Long pictureIdx) {
        return this.jdbcTemplate.queryForObject("SELECT COUNT(*) FROM picturereport WHERE pictureIdx = ?",new Object[]{pictureIdx}, int.class);
    }
}
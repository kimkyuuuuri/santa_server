package com.smileflower.santa.src.comment;

import com.smileflower.santa.src.comment.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;


@Repository
public class CommentDao {

    private JdbcTemplate jdbcTemplate;
    private List<GetRecommentRes> getRecommentRes;

    @Autowired
    public void setDataSource(DataSource dataSource) {

        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
    public int checkSaveExist(int userIdx,int pictureIdx){
        return this.jdbcTemplate.queryForObject("select EXISTS(select picturesaveIdx from picturesave where userIdx=? and pictureIdx=? and status='t') as exist",
                int.class,
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

    public List<GetCommentRes> getFlagComment(Long flagIdx) {
        String getFlagCommentQuery = "select u.userIdx, u.userImageUrl, u.name as userName, flagcommentIdx as commentIdx, contents," +
                "  flagcomment.status as status,\n" +
                "       case\n" +
                "                                                            when timestampdiff(minute , flagcomment.createdAt, current_timestamp()) < 60\n" +
                "                                                          then '방금 전'\n" +
                "                                                            when timestampdiff(hour , flagcomment.createdAt, current_timestamp()) < 24\n" +
                "                                                                then concat(timestampdiff(hour, flagcomment.createdAt, current_timestamp()), '시간 전')\n" +
                "                                                           when  timestampdiff(hour, flagcomment.createdAt, current_timestamp()) < 168\n" +
                "                                                                then concat(timestampdiff(day , flagcomment.createdAt, current_timestamp()), '일 전')\n" +
                "\n" +
                "                                                            ELSE\n" +
                "                                                               concat(timestampdiff(week, flagcomment.createdAt, current_timestamp()), '주 전') end  as  createdAt from flagcomment\n" +
                "left join user u on flagcomment.userIdx = u.userIdx\n" +
                "where flagcomment.flagIdx=? ";
            return this.jdbcTemplate.query(getFlagCommentQuery,
                (rs, rowNum) -> new GetCommentRes(
                        rs.getInt("userIdx"),
                        rs.getString("userImageUrl"),
                        rs.getString("userName"),
                        rs.getInt("commentIdx"),
                        rs.getString("contents"),
                        rs.getString("status"),
                        rs.getString("createdAt"),
                        getRecommentRes =this.jdbcTemplate.query(" select u.userIdx, u.userImageUrl, u.name as userName, flagrecommentIdx as recommentIdx, contents," +
                                        " flagrecomment.status as status,\n" +
                                        "       case\n" +
                                        "           when timestampdiff(minute , flagrecomment.createdAt, current_timestamp()) < 60\n" +
                                        "               then '방금 전'\n" +
                                        "           when timestampdiff(hour , flagrecomment.createdAt, current_timestamp()) < 24\n" +
                                        "               then concat(timestampdiff(hour, flagrecomment.createdAt, current_timestamp()), '시간 전')\n" +
                                        "           when  timestampdiff(hour, flagrecomment.createdAt, current_timestamp()) < 168\n" +
                                        "               then concat(timestampdiff(day , flagrecomment.createdAt, current_timestamp()), '일 전')\n" +
                                        "\n" +
                                        "           ELSE\n" +
                                        "               concat(timestampdiff(week, flagrecomment.createdAt, current_timestamp()), '주 전') end  as  createdAt\n" +
                                        "    from flagrecomment\n" +
                                        "inner join user u on flagrecomment.userIdx = u.userIdx\n" +
                                        "where flagrecomment.flagcommentIdx=? and flagrecomment.status='t' "  ,
                                (rk, rowNum2) -> new GetRecommentRes(
                                        rk.getInt("userIdx"),
                                        rk.getString("userImageUrl"),
                                        rk.getString("userName"),
                                        rk.getInt("recommentIdx"),
                                        rk.getString("contents"),
                                        rk.getString("status"),
                                        rk.getString("createdAt"))
                                ,rs.getInt("commentIdx"))),flagIdx);

    }

    public List<GetCommentRes> getPictureComment(Long pictureIdx) {
        String getPictureCommentQuery = "select u.userIdx, u.userImageUrl, u.name as userName, picturecommentIdx as commentIdx, contents," +
                "  picturecomment.status as status,\n" +
                "       case\n" +
                "                                                            when timestampdiff(minute , picturecomment.createdAt, current_timestamp()) < 60\n" +
                "                                                          then '방금 전'\n" +
                "                                                            when timestampdiff(hour , picturecomment.createdAt, current_timestamp()) < 24\n" +
                "                                                                then concat(timestampdiff(hour, picturecomment.createdAt, current_timestamp()), '시간 전')\n" +
                "                                                           when  timestampdiff(hour, picturecomment.createdAt, current_timestamp()) < 168\n" +
                "                                                                then concat(timestampdiff(day , picturecomment.createdAt, current_timestamp()), '일 전')\n" +
                "\n" +
                "                                                            ELSE\n" +
                "                                                               concat(timestampdiff(week, picturecomment.createdAt, current_timestamp()), '주 전') end  as  createdAt from picturecomment\n" +
                "left join user u on picturecomment.userIdx = u.userIdx\n" +
                "where picturecomment.pictureIdx=? ";
        return this.jdbcTemplate.query(getPictureCommentQuery,
                (rs, rowNum) -> new GetCommentRes(
                        rs.getInt("userIdx"),
                        rs.getString("userImageUrl"),
                        rs.getString("userName"),
                        rs.getInt("commentIdx"),
                        rs.getString("contents"),
                        rs.getString("status"),
                        rs.getString("createdAt"),
                        getRecommentRes =this.jdbcTemplate.query(" select u.userIdx, u.userImageUrl, u.name as userName, picturerecommentIdx as recommentIdx, contents," +
                                        " picturerecomment.status as status,\n" +
                                        "       case\n" +
                                        "           when timestampdiff(minute , picturerecomment.createdAt, current_timestamp()) < 60\n" +
                                        "               then '방금 전'\n" +
                                        "           when timestampdiff(hour , picturerecomment.createdAt, current_timestamp()) < 24\n" +
                                        "               then concat(timestampdiff(hour, picturerecomment.createdAt, current_timestamp()), '시간 전')\n" +
                                        "           when  timestampdiff(hour, picturerecomment.createdAt, current_timestamp()) < 168\n" +
                                        "               then concat(timestampdiff(day , picturerecomment.createdAt, current_timestamp()), '일 전')\n" +
                                        "\n" +
                                        "           ELSE\n" +
                                        "               concat(timestampdiff(week, picturerecomment.createdAt, current_timestamp()), '주 전') end  as  createdAt\n" +
                                        "    from picturerecomment\n" +
                                        "inner join user u on picturerecomment.userIdx = u.userIdx\n" +
                                        "where picturerecomment.picturecommentIdx=? and picturerecomment.status='t' "  ,
                                (rk, rowNum2) -> new GetRecommentRes(
                                        rk.getInt("userIdx"),
                                        rk.getString("userImageUrl"),
                                        rk.getString("userName"),
                                        rk.getInt("recommentIdx"),
                                        rk.getString("contents"),
                                        rk.getString("status"),
                                        rk.getString("createdAt"))
                                ,rs.getInt("commentIdx"))),pictureIdx);

    }


    public int createFlagComment(PostCommentReq postCommentReq, Long flagIdx, int userIdx){
        Object[] createFlagCommentParams = new Object[]{userIdx, flagIdx, postCommentReq.getContents()};

        this.jdbcTemplate.update("insert into flagcomment (userIdx,flagIdx,contents) VALUES (? , ?,?)",

                createFlagCommentParams);
        return this.jdbcTemplate.queryForObject("select last_insert_id()",int.class);
    }

    public int createPictureComment(PostCommentReq postCommentReq, Long pictureIdx, int userIdx){
        Object[] createPictureCommentParams = new Object[]{userIdx, pictureIdx, postCommentReq.getContents()};

        this.jdbcTemplate.update("insert into picturecomment (userIdx,pictureIdx,contents) VALUES (? , ?,?)",

                createPictureCommentParams);
        return this.jdbcTemplate.queryForObject("select last_insert_id()",int.class);
    }

    public int createFlagRecomment(PostRecommentReq postReCommentReq, Long flagCommentIdx, int userIdx){
        Object[] createFlagRecommentParams = new Object[]{userIdx, flagCommentIdx, postReCommentReq.getContents()};

        this.jdbcTemplate.update("insert into flagrecomment (userIdx,flagcommentIdx,contents) VALUES (? , ?,?)",

                createFlagRecommentParams);
        return this.jdbcTemplate.queryForObject("select last_insert_id()",int.class);
    }

    public int createPictureRecomment(PostRecommentReq postReCommentReq, Long pictureCommentIdx, int userIdx){
        Object[] createPictureRecommentParams = new Object[]{userIdx, pictureCommentIdx, postReCommentReq.getContents()};

        this.jdbcTemplate.update("insert into picturerecomment (userIdx,picturecommentIdx,contents) VALUES (? , ?,?)",

                createPictureRecommentParams);
        return this.jdbcTemplate.queryForObject("select last_insert_id()",int.class);
    }


    public int checkFlagExist(Long flagIdx){
        return this.jdbcTemplate.queryForObject("select Exists(select flagIdx from flag\n" +
                "                where status='T' and flagIdx=? ) as FlagExist", int.class,flagIdx);
    }
    public int checkPictureExist(Long pictureIdx){
        return this.jdbcTemplate.queryForObject("select Exists(select pictureIdx from picture\n" +
                "                where status='T' and pictureIdx=? ) as pictureExist", int.class,pictureIdx);
    }

    public void deleteFlagComment(Long flagCommentIdx) {
        this.jdbcTemplate.update("update flagcomment status set status='f'  where flagcommentIdx=?",
                flagCommentIdx
        );

    }

    public void deletePictureComment(Long pictureCommentIdx) {
        this.jdbcTemplate.update("update picturecomment status set status='f'  where picturecommentIdx=?",
                pictureCommentIdx
        );

    }

    public void deleteFlagRecomment(Long flagRecommentIdx) {
        this.jdbcTemplate.update("update flagrecomment status set status='f'  where flagrecommentIdx=?",
                flagRecommentIdx
        );

    }

    public void deletePictureRecomment(Long pictureRecommentIdx) {
        this.jdbcTemplate.update("update picturerecomment status set status='f'  where picturerecommentIdx=?",
                pictureRecommentIdx
        );

    }

    public int checkFlagCommentExist(Long flagCommentIdx){
        return this.jdbcTemplate.queryForObject("select Exists(select flagCommentIdx from flagcomment\n" +
                "                where status='t' and flagCommentIdx=? ) as FlagCommentExist", int.class,flagCommentIdx);
    }

    public int checkPictureCommentExist(Long pictureCommentIdx){
        return this.jdbcTemplate.queryForObject("select Exists(select pictureCommentIdx from picturecomment\n" +
                "                where status='t' and pictureCommentIdx=? ) as pictureCommentExist", int.class,pictureCommentIdx);
    }

    public int checkFlagRecommentExist(Long flagRecommentIdx){
        return this.jdbcTemplate.queryForObject("select Exists(select flagRecommentIdx from flagrecomment\n" +
                "                where status='t' and flagRecommentIdx=? ) as FlagRecommentExist", int.class,flagRecommentIdx);
    }

    public int checkPictureRecommentExist(Long pictureRecommentIdx){
        return this.jdbcTemplate.queryForObject("select Exists(select pictureRecommentIdx from picturerecomment\n" +
                "                where status='t' and pictureRecommentIdx=? ) as pictureRecommentExist", int.class,pictureRecommentIdx);
    }
    public int checkFlagWhereUserExist(Long flagIdx,int userIdx){
        return this.jdbcTemplate.queryForObject("select Exists(select flagIdx from flag\n" +
                "                where status='t' and flagIdx=? and userIdx=?) as FlagExist", int.class,flagIdx,userIdx);
    }

    public int checkFlagCommentWhereUserExist(Long flagCommentIdx,int userIdx){
        return this.jdbcTemplate.queryForObject("select Exists(select flagCommentIdx from flagcomment\n" +
                "                where status='t' and flagCommentIdx=? and userIdx=?) as FlagExist", int.class,flagCommentIdx,userIdx);
    }
    public int checkPictureCommentWhereUserExist(Long pictureCommentIdx,int userIdx){
        return this.jdbcTemplate.queryForObject("select Exists(select pictureCommentIdx from picturecomment\n" +
                "                where status='t' and pictureCommentIdx=? and userIdx=?) as pictureExist", int.class,pictureCommentIdx,userIdx);
    }
}
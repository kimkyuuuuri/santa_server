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
    private List<GetFlagRecommentRes> getFlagRecommentRes;

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

    public List<GetFlagCommentRes> getFlagComment(Long flagIdx) {
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
                (rs, rowNum) -> new GetFlagCommentRes(
                        rs.getInt("userIdx"),
                        rs.getString("userImageUrl"),
                        rs.getString("userName"),
                        rs.getInt("commentIdx"),
                        rs.getString("contents"),
                        rs.getString("status"),
                        rs.getString("createdAt"),
                        getFlagRecommentRes=this.jdbcTemplate.query(" select u.userIdx, u.userImageUrl, u.name as userName, flagrecommentIdx as recommentIdx, contents," +
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
                                (rk, rowNum2) -> new GetFlagRecommentRes(
                                        rk.getInt("userIdx"),
                                        rk.getString("userImageUrl"),
                                        rk.getString("userName"),
                                        rk.getInt("recommentIdx"),
                                        rk.getString("contents"),
                                        rk.getString("status"),
                                        rk.getString("createdAt"))
                                ,rs.getInt("commentIdx"))),flagIdx);

    }


    public int createFlagComment(PostFlagCommentReq postFlagCommentReq,Long flagIdx,int userIdx){
        Object[] createFlagCommentParams = new Object[]{userIdx, flagIdx, postFlagCommentReq.getContents()};

        this.jdbcTemplate.update("insert into flagcomment (userIdx,flagIdx,contents) VALUES (? , ?,?)",

                createFlagCommentParams);
        return this.jdbcTemplate.queryForObject("select last_insert_id()",int.class);
    }

    public int checkFlagExist(Long flagIdx){
        return this.jdbcTemplate.queryForObject("select Exists(select flagIdx from flag\n" +
                "                where status='T' and flagIdx=? ) as FlagExist", int.class,flagIdx);
    }

    public void deleteFlagComment(Long flagCommentIdx) {
        this.jdbcTemplate.update("update flagcomment status set status='f'  where flagCommentIdx=?",
                flagCommentIdx
        );

    }

    public int checkFlagCommentExist(Long flagCommentIdx){
        return this.jdbcTemplate.queryForObject("select Exists(select flagCommentIdx from flagcomment\n" +
                "                where status='t' and flagCommentIdx=? ) as FlagCommentExist", int.class,flagCommentIdx);
    }

    public int checkFlagWhereUserExist(Long flagIdx,int userIdx){
        return this.jdbcTemplate.queryForObject("select Exists(select flagIdx from flag\n" +
                "                where status='t' and flagIdx=? and userIdx=?) as FlagExist", int.class,flagIdx,userIdx);
    }

    public int checkFlagCommentWhereUserExist(Long flagCommentIdx,int userIdx){
        return this.jdbcTemplate.queryForObject("select Exists(select flagCommentIdx from flagcomment\n" +
                "                where status='t' and flagCommentIdx=? and userIdx=?) as FlagExist", int.class,flagCommentIdx,userIdx);
    }
}
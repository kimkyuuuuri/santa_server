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
    private List<GetRecommentRes> getRecommentsRes;

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

    public List<GetCommentRes> getFlagComments(Long flagIdx) {
        String getFlagCommentQuery = "select u.userIdx, u.userImageUrl, u.name as userName, flagcommentIdx as commentIdx, contents," +
                "  flagcomment.status as status,\n" +
                "       case\n" +
                "                                                            when timestampdiff(minute , flagcomment.createdAt, current_timestamp()) < 60\n" +
                "                                                          then '?????? ???'\n" +
                "                                                            when timestampdiff(hour , flagcomment.createdAt, current_timestamp()) < 24\n" +
                "                                                                then concat(timestampdiff(hour, flagcomment.createdAt, current_timestamp()), '?????? ???')\n" +
                "                                                           when  timestampdiff(hour, flagcomment.createdAt, current_timestamp()) < 168\n" +
                "                                                                then concat(timestampdiff(day , flagcomment.createdAt, current_timestamp()), '??? ???')\n" +
                "\n" +
                "                                                            ELSE\n" +
                "                                                               concat(timestampdiff(week, flagcomment.createdAt, current_timestamp()), '??? ???') end  as  createdAt from flagcomment\n" +
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
                        getRecommentsRes =this.jdbcTemplate.query(" select u.userIdx, u.userImageUrl, u.name as userName, flagrecommentIdx as recommentIdx, contents," +
                                        " flagrecomment.status as status,\n" +
                                        "       case\n" +
                                        "           when timestampdiff(minute , flagrecomment.createdAt, current_timestamp()) < 60\n" +
                                        "               then '?????? ???'\n" +
                                        "           when timestampdiff(hour , flagrecomment.createdAt, current_timestamp()) < 24\n" +
                                        "               then concat(timestampdiff(hour, flagrecomment.createdAt, current_timestamp()), '?????? ???')\n" +
                                        "           when  timestampdiff(hour, flagrecomment.createdAt, current_timestamp()) < 168\n" +
                                        "               then concat(timestampdiff(day , flagrecomment.createdAt, current_timestamp()), '??? ???')\n" +
                                        "\n" +
                                        "           ELSE\n" +
                                        "               concat(timestampdiff(week, flagrecomment.createdAt, current_timestamp()), '??? ???') end  as  createdAt\n" +
                                        "    from flagrecomment\n" +
                                        "inner join user u on flagrecomment.userIdx = u.userIdx\n" +
                                        "where flagrecomment.flagcommentIdx=?  "  ,
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

    public List<GetCommentRes> getPictureComments(Long pictureIdx) {
        String getPictureCommentQuery = "select u.userIdx, u.userImageUrl, u.name as userName, picturecommentIdx as commentIdx, contents," +
                "  picturecomment.status as status,\n" +
                "       case\n" +
                "                                                            when timestampdiff(minute , picturecomment.createdAt, current_timestamp()) < 60\n" +
                "                                                          then '?????? ???'\n" +
                "                                                            when timestampdiff(hour , picturecomment.createdAt, current_timestamp()) < 24\n" +
                "                                                                then concat(timestampdiff(hour, picturecomment.createdAt, current_timestamp()), '?????? ???')\n" +
                "                                                           when  timestampdiff(hour, picturecomment.createdAt, current_timestamp()) < 168\n" +
                "                                                                then concat(timestampdiff(day , picturecomment.createdAt, current_timestamp()), '??? ???')\n" +
                "\n" +
                "                                                            ELSE\n" +
                "                                                               concat(timestampdiff(week, picturecomment.createdAt, current_timestamp()), '??? ???') end  as  createdAt from picturecomment\n" +
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
                        getRecommentsRes =this.jdbcTemplate.query(" select u.userIdx, u.userImageUrl, u.name as userName, picturerecommentIdx as recommentIdx, contents," +
                                        " picturerecomment.status as status,\n" +
                                        "       case\n" +
                                        "           when timestampdiff(minute , picturerecomment.createdAt, current_timestamp()) < 60\n" +
                                        "               then '?????? ???'\n" +
                                        "           when timestampdiff(hour , picturerecomment.createdAt, current_timestamp()) < 24\n" +
                                        "               then concat(timestampdiff(hour, picturerecomment.createdAt, current_timestamp()), '?????? ???')\n" +
                                        "           when  timestampdiff(hour, picturerecomment.createdAt, current_timestamp()) < 168\n" +
                                        "               then concat(timestampdiff(day , picturerecomment.createdAt, current_timestamp()), '??? ???')\n" +
                                        "\n" +
                                        "           ELSE\n" +
                                        "               concat(timestampdiff(week, picturerecomment.createdAt, current_timestamp()), '??? ???') end  as  createdAt\n" +
                                        "    from picturerecomment\n" +
                                        "inner join user u on picturerecomment.userIdx = u.userIdx\n" +
                                        "where picturerecomment.picturecommentIdx=? "  ,
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

    public String selectUserImage(int userIdx){
        return this.jdbcTemplate.queryForObject("select userImageUrl from user" +
                "                where status='T' and userIdx=? ", String.class,userIdx);
    }



    public int selectFlagUserIdx(Long flagIdx){
        return this.jdbcTemplate.queryForObject("select userIdx from flag" +
                "                where  flagIdx=? ", Integer.class,flagIdx);
    }

    public int selectPictureUserIdx(Long pictureIdx){
        return this.jdbcTemplate.queryForObject("select userIdx from picture" +
                "                where  pictureIdx=? ", Integer.class,pictureIdx);
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


    public int createFlagCommentNotification(int userIdx, Long flagIdx,String contents){
        Object[] createFlagCommentNotificationParams = new Object[]{userIdx, flagIdx,"T",contents};

        this.jdbcTemplate.update("insert into notification (userIdx,flagIdx,isComment,contents) VALUES (? ,?,?,?)",

                createFlagCommentNotificationParams);
        return this.jdbcTemplate.queryForObject("select last_insert_id()",int.class);
    }

    public int createPictureCommentNotification(int userIdx, Long pictureIdx,String contents){
        Object[] createPictureCommentNotificationParams = new Object[]{userIdx, pictureIdx,"T",contents};

        this.jdbcTemplate.update("insert into notification (userIdx,pictureIdx,isComment,contents) VALUES (? ,?,?,?)",

                createPictureCommentNotificationParams);
        return this.jdbcTemplate.queryForObject("select last_insert_id()",int.class);
    }

    public int createFlagRecommentNotification(int userIdx, Long flagIdx,String contents){
        Object[] createFlagCommentNotificationParams = new Object[]{userIdx, flagIdx,"T",contents};

        this.jdbcTemplate.update("insert into notification (userIdx,flagIdx,isRecomment,contents) VALUES (? ,?,?,?)",

                createFlagCommentNotificationParams);
        return this.jdbcTemplate.queryForObject("select last_insert_id()",int.class);
    }

    public int createPictureRecommentNotification(int userIdx, Long pictureIdx,String contents){
        Object[] createPictureCommentNotificationParams = new Object[]{userIdx, pictureIdx,"T",contents};

        this.jdbcTemplate.update("insert into notification (userIdx,pictureIdx,isRecomment,contents) VALUES (? ,?,?,?)",

                createPictureCommentNotificationParams);
        return this.jdbcTemplate.queryForObject("select last_insert_id()",int.class);
    }
    public int createPictureRecomment(PostRecommentReq postReCommentReq, Long pictureCommentIdx, int userIdx){
        Object[] createPictureRecommentParams = new Object[]{userIdx, pictureCommentIdx, postReCommentReq.getContents()};

        this.jdbcTemplate.update("insert into picturerecomment (userIdx,picturecommentIdx,contents) VALUES (? ,?,?)",

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
        return this.jdbcTemplate.queryForObject("select Exists(select flagcommentIdx from flagcomment\n" +
                "                where status='t' and flagcommentIdx=? ) as FlagCommentExist", int.class,flagCommentIdx);
    }

    public int checkPictureCommentExist(Long pictureCommentIdx){
        return this.jdbcTemplate.queryForObject("select Exists(select picturecommentIdx from picturecomment\n" +
                "                where status='t' and picturecommentIdx=? ) as pictureCommentExist", int.class,pictureCommentIdx);
    }

    public int checkFlagRecommentExist(Long flagRecommentIdx){
        return this.jdbcTemplate.queryForObject("select Exists(select flagcecommentIdx from flagrecomment\n" +
                "                where status='t' and flagrecommentIdx=? ) as FlagRecommentExist", int.class,flagRecommentIdx);
    }

    public int checkPictureRecommentExist(Long pictureRecommentIdx){
        return this.jdbcTemplate.queryForObject("select Exists(select picturececommentIdx from picturerecomment\n" +
                "                where status='t' and picturerecommentIdx=? ) as pictureRecommentExist", int.class,pictureRecommentIdx);
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

    public String getUserFlagPushToken(Long flagIdx){
        return this.jdbcTemplate.queryForObject("select pushToken from user\n" +
                "join flag f on user.userIdx = f.userIdx\n" +
                "where f.flagIdx=?", String.class,flagIdx);
    }
    public int getUserIdxByFlag(Long flagIdx){
        return this.jdbcTemplate.queryForObject("select user.userIdx from user\n" +
                "join flag f on user.userIdx = f.userIdx\n" +
                "where f.flagIdx=?", int.class,flagIdx);
    }

    public String getUserPicturePushToken(Long pictureIdx){
        return this.jdbcTemplate.queryForObject("select pushToken from user\n" +
                "join picture p on user.userIdx = p.userIdx\n" +
                "where p.pictureIdx=?", String.class,pictureIdx);
    }

    public int getUserIdxByPicture(Long pictureIdx){
        return this.jdbcTemplate.queryForObject("select user.userIdx from user\n" +
                "join picture p on user.userIdx = p.userIdx\n" +
                "where p.pictureIdx=?", int.class,pictureIdx);
    }

    public String getUserFlagCommentPushToken(Long flagCommentIdx){
        return this.jdbcTemplate.queryForObject("select pushToken from user\n" +
                "join flagcomment f on user.userIdx = f.userIdx\n" +
                "where f.flagcommentIdx=?", String.class,flagCommentIdx);
    }
    public int getUserIdxByFlagComment(Long flagCommentIdx){
        return this.jdbcTemplate.queryForObject("select user.userIdx from user\n" +
                "join flagcomment f on user.userIdx = f.userIdx\n" +
                "where f.flagcommentIdx=?", Integer.class,flagCommentIdx);
    }
    public Long getFlagIdx(Long flagCommentIdx){
        return this.jdbcTemplate.queryForObject("select flag.flagIdx from flag\n" +
                "                join flagcomment f on f.flagIdx = flag.flagIdx\n" +
                "                where f.flagcommentIdx=?", Long.class,flagCommentIdx);
    }
    public Long getPictureIdx(Long pictureCommentIdx){
        return this.jdbcTemplate.queryForObject("select picture.pictureIdx from picture\n" +
                "                join picturecomment p on p.pictureIdx = picture.pictureIdx\n" +
                "                where p.picturecommentIdx=?", Long.class,pictureCommentIdx);
    }

    public String getUserPictureCommentPushToken(Long pictureCommentIdx){
        return this.jdbcTemplate.queryForObject("select pushToken from user\n" +
                "join picturecomment p on user.userIdx = p.userIdx\n" +
                "where p.picturecommentIdx=?", String.class,pictureCommentIdx);
    }
    public int getUserIdxByPictureComment(Long pictureCommentIdx){
        return this.jdbcTemplate.queryForObject("select user.userIdx from user\n" +
                "join picturecomment p on user.userIdx = p.userIdx\n" +
                "where p.picturecommentIdx=?", int.class,pictureCommentIdx);
    }

    public String getUserFlagPushTokenByRecomment(Long flagCommentIdx){
        return this.jdbcTemplate.queryForObject("select pushToken from user\n" +
                "join flagcomment f on user.userIdx = f.userIdx\n" +
                "where f.flagcommentIdx=?", String.class,flagCommentIdx);
    }

    public int getUserIdxByFlagCommentByRecomment(Long flagCommentIdx){
        return this.jdbcTemplate.queryForObject("select user.userIdx from user\n" +
                " join flag f on user.userIdx = f.userIdx\n" +
                "join flagcomment f2 on f.flagIdx = f2.flagIdx\n" +
                "where f2.flagcommentIdx=?", int.class,flagCommentIdx);
    }
    public String getUserPicturePushTokenByRecomment(Long pictureCommentIdx){
        return this.jdbcTemplate.queryForObject("select user.userIdx from user" +
                "       join picture p on user.userIdx = p.userIdx" +
                "       join picturecomment p2 on p.pictureIdx = p2.pictureIdx" +
                "       where p2.picturecommentIdx=?", String.class,pictureCommentIdx);
    }
    public int getUserIdxByPictureCommentByRecomment(Long pictureCommentIdx){
        return this.jdbcTemplate.queryForObject("select user.userIdx from user\n" +
                " join picture p on user.userIdx = p.userIdx\n" +
                "join picturecomment p2 on p.pictureIdx = p2.pictureIdx\n" +
                "where p2.picturecommentIdx=?", int.class,pictureCommentIdx);
    }


    public GetUserInfoRes getUserName(int userIdx){
        return this.jdbcTemplate.queryForObject("select name,tokenType from user where userIdx=?",
                (rs, rowNum) -> new GetUserInfoRes(
                        rs.getString("name"),
                        rs.getString("tokenType")

                ),
                userIdx);   }

}
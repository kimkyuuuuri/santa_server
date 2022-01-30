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
    private GetFlagRecommentRes getFlagRecommentRes;

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

   /* public List<GetFlagCommentRes> getFlagComment(int flagIdx) {
        String getNoticeQuery = "select  case when exists(select noticeIdx from notice where userIdx=? and status='t') =1 then 't' else 'f' end as notice ";
        return this.jdbcTemplate.query(getNoticeQuery,
                (rs, rowNum) -> new GetFlagCommentRes(
                        rs.getString("notice"),
                        getFlagRecommentRes=this.jdbcTemplate.query("select user.userIdx,user.userImageUrl, (select\n" +
                                        "                                                                          case\n" +
                                        "                                                                              when  count(*) > 0 and  count(*) < 2 then 'Lv.1'\n" +
                                        "                                                                              when count(*)>= 2 and  count(*) < 4 then 'Lv.2'\n" +
                                        "                                                                              when  count(*) >= 4 and  count(*) < 6 then 'Lv.3'\n" +
                                        "                                                                              when  count(*)>= 6 and  count(*) < 8 then 'Lv.4'\n" +
                                        "                                                                              when  count(*) >= 8 and  count(*) < 10 then 'Lv.5'\n" +
                                        "                                                                              when  count(*) >= 10 then 'Lv.6' end as level\n" +
                                        "                     from flag where  flag.userIdx=user.userIdx)\n" +
                                        "    as level,user.name as userName," +
                                        "case when EXISTS(select flagsaveIdx from flagsave where flagsave.userIdx=? and flagsave.flagIdx=flag.flagIdx and flagsave.status='t') =1 then 'Y' else 'N' end as isSaved," +
                                        "       (select count(*) from flagrecomment where flagrecomment.flagcommentIdx=flagcomment.flagcommentIdx ) +(select count(*) from flagcomment where flagcomment.flagIdx=flag.flagIdx)  as commentCount\n" +
                                        "     ,count( distinct  flagsave.flagsaveIdx ) as saveCount,flag.flagIdx,flag.pictureUrl as flagImageUrl from flag\n" +
                                        "    left join flagsave on flag.flagIdx = flagsave.flagIdx\n" +
                                        "    inner join user on flag.userIdx = user.userIdx\n" +
                                        "    left join flagcomment on flag.flagIdx = flagcomment.flagIdx\n" +
                                        "\n" +
                                        "where flagsave.status='t' group by flagsave.flagIdx order by  saveCount desc,commentCount desc limit 6",
                                (rk,rownum) -> new GetFlagRecommentRes(
                                        rk.getInt("userIdx"),
                                        rk.getString("userImageUrl"),
                                        rk.getString("level"),
                                        rk.getString("userName"),
                                        rk.getString("isSaved"),
                                        rk.getInt("commentCount"),
                                        rk.getInt("saveCount"),
                                        rk.getInt("flagIdx"),
                                        rk.getString("flagImageUrl")


                                ),rs.getInt("Idx")));

    }
*/
}
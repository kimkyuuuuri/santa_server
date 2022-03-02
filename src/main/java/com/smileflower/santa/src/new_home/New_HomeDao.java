package com.smileflower.santa.src.new_home;


import com.smileflower.santa.src.new_home.model.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

import static org.thymeleaf.util.StringUtils.concat;

@Repository
public class New_HomeDao {

    private JdbcTemplate jdbcTemplate;
    private GetHomeRes getHomeRes;
    private List<GetFlagsRes> getFlagsResList;
    private List<GetUsersRes> getUsersResList;
    private List<GetMountainsRes> getMountainsResList;
    private List<GetCommentRes >getCommentRes;
    private int count=0;

    private List<GetMountainsIdxRes> getMountainsIdxResList;
    @Autowired
    public void setDataSource(DataSource dataSource) {

        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public GetHomeRes getHomeRes(int userIdx) {
        String getNoticeQuery = "select  case when exists(select noticeIdx from notice where userIdx=? and status='t') =1 then 't' else 'f' end as notice," +
                "case when user.status='f' then 't' else 'f' end as isFirst from user where userIdx=?" ;
        return this.jdbcTemplate.queryForObject(getNoticeQuery,
                (rs, rowNum) -> new GetHomeRes(
                rs.getString("notice"),
                rs.getString("isFirst"),
                getFlagsResList=this.jdbcTemplate.query("select user.userIdx, userImageUrl, (select\n" +
                        "                                                                          case\n" +
                        "                                                                              when  count(*) > 0 and  count(*) < 2 then 'Lv.1'\n" +
                        "                                                                              when count(*)>= 2 and  count(*) < 4 then 'Lv.2'\n" +
                        "                                                                              when  count(*) >= 4 and  count(*) < 6 then 'Lv.3'\n" +
                        "                                                                              when  count(*)>= 6 and  count(*) < 8 then 'Lv.4'\n" +
                        "                                                                              when  count(*) >= 8 and  count(*) < 10 then 'Lv.5'\n" +
                        "                                                                              when  count(*) >= 10 then 'Lv.6' end as level\n" +
                        "                     from flag where  flag.userIdx=user.userIdx)\n" +
                        "    as level,user.name as userName," +
                                "case when EXISTS(select flagsaveIdx from flagsave where flagsave.userIdx=? and flagsave.flagIdx=flag.flagIdx and flagsave.status='t') =1 then 'T' else 'F' end as isSaved," +
                        "       (select count(*) from flagrecomment where flagrecomment.flagcommentIdx=flagcomment.flagcommentIdx ) +(select count(*) from flagcomment where flagcomment.flagIdx=flag.flagIdx)  as commentCount\n" +
                        "     ,count( distinct  flagsave.flagsaveIdx ) as saveCount,flag.flagIdx,flag.pictureUrl as flagImageUrl from flag\n" +
                        "    left join flagsave on flag.flagIdx = flagsave.flagIdx\n" +
                        "    inner join user on flag.userIdx = user.userIdx \n" +
                        "    left join flagcomment on flag.flagIdx = flagcomment.flagIdx\n" +
                        "\n" +
                        "where flagsave.status='t' and user.status='t'  group by flagsave.flagIdx order by  saveCount desc,commentCount desc limit 6",
                        (rk,rownum) -> new GetFlagsRes(
                                rk.getInt("userIdx"),
                                rk.getString("userImageUrl"),
                                rk.getString("level"),
                                rk.getString("userName"),
                                rk.getString("isSaved"),
                                rk.getInt("commentCount"),
                                rk.getInt("saveCount"),
                                rk.getInt("flagIdx"),
                                rk.getString("flagImageUrl")


                        ),userIdx),
                getUsersResList=this.jdbcTemplate.query("select user.userIdx, userImageUrl ,\n" +
                                "        case\n" +
                                "\n" +
                                "                     when  f.flagCount> 0 and  f.flagCount < 2 then 'Lv.1'\n" +
                                "                     when f.flagCount>= 2 and  f.flagCount < 4 then 'Lv.2'\n" +
                                "                     when  f.flagCount >= 4 and  f.flagCount < 6 then 'Lv.3'\n" +
                                "                     when  f.flagCount>= 6 and  f.flagCount< 8 then 'Lv.4'\n" +
                                "                     when  f.flagCount >= 8 and  f.flagCount < 10 then 'Lv.5'\n" +
                                "                     when  f.flagCount >= 10 then 'Lv.6' end\n" +
                                "\n" +
                                "                                                                          as level,name as userName,\n" +
                                "\n" +
                                "        (select case\n" +
                                "                               when timestampdiff(minute , max(flag.createdAt), current_timestamp()) < 60\n" +
                                "                                   then concat(timestampdiff(minute, max(flag.createdAt), current_timestamp()), '분전')\n" +
                                "                               when timestampdiff(hour, max(flag.createdAt), current_timestamp()) < 24\n" +
                                "                                   then concat(timestampdiff(hour , max(flag.createdAt), current_timestamp()), '시간전')\n" +
                                "                               ELSE\n" +
                                "                                   concat(timestampdiff(day, max(flag.createdAt), current_timestamp()), '일전') end from flag\n" +
                                "                    where  user.userIdx=flag.userIdx and flag.status='t') as  agoTime,\n" +
                                "       case when user.height<1000 then concat(Round(user.height,2), 'm')\n" +
                                "            else concat( Round(user.height/1000,2), 'km') end as height\n" +
                                "\n" +
                                "from user\n" +
                                "         left join (select userIdx,count(flagIdx) as flagCount,createdAt from flag group by userIdx) f\n" +
                                "                   on f.userIdx = user.userIdx\n" +
                                "where user.status='t'" +
                                "order by user.height desc limit 10;",
                        (rk,rownum) -> new GetUsersRes(
                                rk.getInt("userIdx"),
                                rk.getString("userImageUrl"),
                                rk.getString("level"),
                                rk.getString("userName"),
                                rk.getString("agotime"),
                                rk.getString("height")

                        )),
                getMountainsResList=this.jdbcTemplate.query("select mountain.mountainIdx as mountainIdx,mountain.imageUrl as mountainImageUrl, case when p.hot > 10 then 't' else 'f' end as isHot,\n" +
                                "       case when mountain.high<500 then 1\n" +
                                "                                   when mountain.high<800  then 2\n" +
                                "                                   when mountain.high<1000 then 3\n" +
                                "                                   when mountain.high<1300 then 4\n" +
                                "                                   else 5 end as difficulty\n" +
                                "       ,mountain.name as mountainName,  concat('(', mountain.high, 'm)') as high,\n" +
                                "      u.userIdx,u.userImageUrl\n" +
                                "                         from mountain\n" +
                                "                                  left join (select mountainIdx, userIdx,createdAt ,totalHeight ,count(*) as flagCount from flag  where status='t'  group by mountainIdx\n" +
                                "                                             ) f  on f.mountainIdx = mountain.mountainIdx\n" +
                                "                                 left join (select mountainIdx, count(picklistIdx) as hot from picklist  where status='t' group by mountainIdx) p    on p.mountainIdx = mountain.mountainIdx\n" +
                                "                                  left join (select flagIdx,userIdx,mountainIdx, max(totalHeight) as maxHeight from flag   where status='t' group by mountainIdx\n" +
                                "                         ) maxflag   on maxflag.mountainIdx = f.mountainIdx\n" +
                                "                                left join (select userIdx,totalHeight from flag ) f2  on maxflag.maxHeight=f2.totalHeight\n" +
                                "                                  left join (select userIdx,userImageUrl from user  where status='t'\n" +
                                "                         ) u on f2.userIdx=u.userIdx\n" +
                                "    order by flagCount desc limit 3;\n",
                        (rk,rownum) -> new GetMountainsRes(
                                rk.getInt("mountainIdx"),
                                rk.getString("mountainImageUrl"),
                                rk.getString("isHot"),
                                rk.getInt("difficulty"),
                                rk.getString("mountainName"),
                                rk.getString("high"),
                                rk.getInt("userIdx"),
                                rk.getString("userImageUrl")
                        ))),userIdx,userIdx);

    }

    public List<GetFlagsRes> getFlagRes() {
        return this.jdbcTemplate.query("select user.userIdx,user.userImageUrl, (select\n" +
                "                                                                          case\n" +
                        "                                                                              when  count(*) > 0 and  count(*) < 2 then 'Lv.1'\n" +
                        "                                                                              when count(*)>= 2 and  count(*) < 4 then 'Lv.2'\n" +
                        "                                                                              when  count(*) >= 4 and  count(*) < 6 then 'Lv.3'\n" +
                        "                                                                              when  count(*)>= 6 and  count(*) < 8 then 'Lv.4'\n" +
                        "                                                                              when  count(*) >= 8 and  count(*) < 10 then 'Lv.5'\n" +
                        "                                                                              when  count(*) >= 10 then 'Lv.6' end as level\n" +
                        "                     from flag where  flag.userIdx=user.userIdx and user.status='t')\n" +
                        "    as level,user.name as userName,\n" +
                        "       (select count(*) from recomment where recomment.commentIdx=comment.commentIdx  ) +(select count(*) from comment where comment.pictureIdx=picture.pictureIdx)  as commentCount\n" +
                        "     ,count( distinct  picturesave.saveIdx) as saveCount,picture.pictureIdx,picture.imgUrl as pictureImageUrl from picture\n" +
                        "    left join picturesave on picture.pictureIdx = picturesave.pictureIdx\n" +
                        "    inner join user on picture.userIdx = user.userIdx\n" +
                        "    left join comment on picture.pictureIdx = comment.pictureIdx\n" +
                        "\n" +
                        "where picturesave.status='t' and user.status='t' group by picturesave.pictureIdx order by  saveCount desc,commentCount desc limit 10",
                (rk,rownum) -> new GetFlagsRes(
                        rk.getInt("userIdx"),
                        rk.getString("userImageUrl"),
                        rk.getString("level"),
                        rk.getString("userName"),
                        rk.getString("isFlag"),
                        rk.getInt("commentCount"),
                        rk.getInt("saveCount"),
                        rk.getInt("pictureIdx"),
                        rk.getString("pictureImageUrl")


                ));

}
    public List<GetUsersRes> getUsersRes() {
        return this.jdbcTemplate.query("select user.userIdx, userImageUrl ,\n" +
                "        case\n" +
                        "\n" +
                        "                     when  f.flagCount> 0 and  f.flagCount < 2 then 'Lv.1'\n" +
                        "                     when f.flagCount>= 2 and  f.flagCount < 4 then 'Lv.2'\n" +
                        "                     when  f.flagCount >= 4 and  f.flagCount < 6 then 'Lv.3'\n" +
                        "                     when  f.flagCount>= 6 and  f.flagCount< 8 then 'Lv.4'\n" +
                        "                     when  f.flagCount >= 8 and  f.flagCount < 10 then 'Lv.5'\n" +
                        "                     when  f.flagCount >= 10 then 'Lv.6' end\n" +
                        "\n" +
                        "                                                                          as level,name as userName,\n" +
                        "\n" +
                        "        (select case\n" +
                        "                               when timestampdiff(minute , max(flag.createdAt), current_timestamp()) < 60\n" +
                        "                                   then concat(timestampdiff(minute, max(flag.createdAt), current_timestamp()), '분전')\n" +
                        "                               when timestampdiff(hour, max(flag.createdAt), current_timestamp()) < 24\n" +
                        "                                   then concat(timestampdiff(hour , max(flag.createdAt), current_timestamp()), '시간전')\n" +
                        "                               ELSE\n" +
                        "                                   concat(timestampdiff(day, max(flag.createdAt), current_timestamp()), '일전') end from flag\n" +
                        "                    where  user.userIdx=flag.userIdx and flag.status='t') as  agoTime,\n" +
                        "       case when user.height<1000 then concat(Round(user.height,2), 'm')\n" +
                        "            else concat( Round(user.height/1000,2), 'km') end as height\n" +
                        "\n" +
                        "from user\n" +
                        "         left join (select userIdx,count(flagIdx) as flagCount,createdAt from flag group by userIdx) f\n" +
                        "                   on f.userIdx = user.userIdx\n" +
                        "order by user.height desc limit 10;",
                (rk,rownum) -> new GetUsersRes(
                        rk.getInt("userIdx"),
                        rk.getString("userImageUrl"),
                        rk.getString("level"),
                        rk.getString("userName"),
                        rk.getString("agotime"),
                        rk.getString("height")

                ));

    }

    public List<GetFlagsMoreRes> getFlagsMoreRes(int userIdx) {

        return this.jdbcTemplate.query("select user.userIdx,user.userImageUrl, (select\n" +
                        "                                                                                          case\n" +
                        "                                                                                       when  count(*) > 0 and  count(*) < 2 then 'Lv.1'\n" +
                        "                                                                                                  when count(*)>= 2 and  count(*) < 4 then 'Lv.2'\n" +
                        "                                                                                                   when  count(*) >= 4 and  count(*) < 6 then 'Lv.3'\n" +
                        "                                                                                                 when  count(*)>= 6 and  count(*) < 8 then 'Lv.4'\n" +
                        "                                                                                                  when  count(*) >= 8 and  count(*) < 10 then 'Lv.5'\n" +
                        "                                                                                                   when  count(*) >= 10 then 'Lv.6' end as level\n" +
                        "                                         from flag where  flag.userIdx=user.userIdx and user.status='t')\n" +
                        "                           as level,user.name as userName," +
                        "case when EXISTS(select flagsaveIdx from flagsave where flagsave.userIdx=? and flagsave.flagIdx=flag.flagIdx  and flagsave.status='t') =1 then 'T' else 'F' end as isSaved," +
                        "                             (select count(*) from flagrecomment where flagrecomment.flagcommentIdx=flagcomment.flagcommentIdx  ) +(select count(*) from flagcomment where flagcomment.flagIdx=flag.flagIdx)  as commentCount\n" +
                        "                            ,count( distinct  flagsave.flagsaveIdx) as saveCount,flag.flagIdx,flag.pictureUrl as flagImageUrl\n" +
                        "                        from flag\n" +
                        "                            left join flagsave on flag.flagIdx = flagsave.flagIdx\n" +
                        "                            inner join user on flag.userIdx = user.userIdx\n" +
                        "                            left join flagcomment on flag.flagIdx = flagcomment.flagIdx\n" +
                        "                        where flagsave.status='t' and user.status='t' group by flagsave.flagIdx order by  saveCount desc,commentCount desc limit 10\n",
                (rk,rownum) -> new GetFlagsMoreRes(
                        rk.getInt("userIdx"),
                        rk.getString("userImageUrl"),
                        rk.getString("level"),
                        rk.getString("userName"),
                        rk.getString("isSaved"),
                        rk.getInt("commentCount"),
                        rk.getInt("saveCount"),
                        rk.getInt("flagIdx"),
                        rk.getString("flagImageUrl"),
                        getCommentRes=this.jdbcTemplate.query("select user.userIdx, user.userImageUrl,user.name as userName, flagcomment.contents  from flagcomment inner join user on flagcomment.userIdx = user.userIdx\n" +
                                "  where flagcomment.flagIdx=? and user.status='t' order by flagcomment.createdAt limit 1",(rs,rownum2) -> new GetCommentRes(
                        rs.getInt("userIdx"),
                        rs.getString("userImageUrl"),
                        rs.getString("userName"),
                        rs.getString("contents"),
                                this.jdbcTemplate.queryForObject("select ((select count(*) from flagrecomment where flagrecomment.flagcommentIdx=flagcomment.flagcommentIdx   ) +count(*) ) as count\n" +
                                        "from flagcomment  where flagIdx=?  order by createdAt limit 1;",(rl,rownum3) -> rl.getInt("count"),rk.getInt("flagIdx")

                                )) ,rk.getInt("flagIdx"))

                        ),userIdx);

    }




    public List<GetMountainsRes> getMountainsRes(int order) {
        return this.jdbcTemplate.query("select mountain.mountainIdx as mountainIdx,mountain.imageUrl as mountainImageUrl, case when p.hot > 10 then 't' else 'f' end as isHot,\n" +
                        "       case when mountain.high<500 then 1\n" +
                        "                                   when mountain.high<800  then 2\n" +
                        "                                   when mountain.high<1000 then 3\n" +
                        "                                   when mountain.high<1300 then 4\n" +
                        "                                   else 5 end as difficulty\n" +
                        "       ,mountain.name as mountainName,  concat('(', mountain.high, 'm)') as high,\n" +
                        "      u.userIdx,u.userImageUrl\n" +
                        "                         from mountain\n" +
                        "                                  left join (select mountainIdx, userIdx,createdAt ,totalHeight ,count(*) as flagCount from flag  where status='t'  group by mountainIdx\n" +
                        "                                             ) f  on f.mountainIdx = mountain.mountainIdx\n" +
                        "                                 left join (select mountainIdx, count(picklistIdx) as hot from picklist  where status='t' group by mountainIdx) p    on p.mountainIdx = mountain.mountainIdx\n" +
                        "                                  left join (select flagIdx,userIdx,mountainIdx, max(totalHeight) as maxHeight from flag   where status='t' group by mountainIdx\n" +
                        "                         ) maxflag   on maxflag.mountainIdx = f.mountainIdx\n" +
                        "                                left join (select userIdx,totalHeight from flag ) f2  on maxflag.maxHeight=f2.totalHeight\n" +
                        "                                  left join (select userIdx,userImageUrl from user  where status='t'\n" +
                        "                         ) u on f2.userIdx=u.userIdx\n" +
                        "    order by flagCount desc limit 10;",
                (rk,rownum) -> new GetMountainsRes(
                        rk.getInt("mountainIdx"),
                        rk.getString("mountainImageUrl"),
                        rk.getString("isHot"),
                        rk.getInt("difficulty"),
                        rk.getString("mountainName"),
                        rk.getString("high"),
                        rk.getInt("userIdx"),
                        rk.getString("userImageUrl")
                ));

    }
    public String getUserStatus(int userIdx){
        return this.jdbcTemplate.queryForObject("select status from user where userIdx=?",
                String.class,
                userIdx);
    }

    public void setUserStatus(int userIdx){
        this.jdbcTemplate.update("UPDATE user SET status = 'T' WHERE userIdx = ?",
                userIdx);
    }
    public void updateNotificationStatus(int notificationIdx){
        this.jdbcTemplate.update("UPDATE notification SET status = 'T' WHERE notificationIdx = ?",
                notificationIdx);
    }

    public List<GetNotificationRes> getNotificationRes(int userIdx) {
        return this.jdbcTemplate.query("select notificationIdx,flagIdx,pictureIdx, case when isComment='T' then 'comment' when isRecomment='T' then 'recomment'\n" +
                        "when isSave='t' then 'save' end as type ,\n" +
                        "       status,\n" +
                        "       case when timestampdiff(minute , createdAt, current_timestamp()) < 60\n" +
                        "    then '방금 전'\n" +
                        "    when timestampdiff(hour , createdAt, current_timestamp()) < 24\n" +
                        "    then concat(timestampdiff(hour, createdAt, current_timestamp()), '시간 전')\n" +
                        "    when  timestampdiff(hour, createdAt, current_timestamp()) < 168\n" +
                        "    then concat(timestampdiff(day , createdAt, current_timestamp()), '일 전')\n" +
                        "\n" +
                        "    ELSE\n" +
                        "    concat(timestampdiff(week, createdAt, current_timestamp()), '주 전') end  as  createdAt from notification\n" +
                        " where userIdx=?\n",
                (rk,rownum) -> new GetNotificationRes(
                rk.getInt("notificationIdx"),
                 rk.getInt("flagIdx"),
                rk.getInt("pictureIdx"),
                rk.getString("type"),
                rk.getString("status"),
                rk.getString("createdAt")


                ),userIdx);

    }
}
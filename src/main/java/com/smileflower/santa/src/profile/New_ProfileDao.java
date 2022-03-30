package com.smileflower.santa.src.profile;



import com.smileflower.santa.src.flags.model.GetFlagCommentIdxRes;
import com.smileflower.santa.src.profile.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Repository
public class New_ProfileDao {

    private JdbcTemplate jdbcTemplate;
    private GetProfileRes getProfileRes;
    private List<GetPostsRes> getPostsResList;



    private List<GetFlagRes> getFlagResList;
    @Autowired
    public void setDataSource(DataSource dataSource) {

        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }




    public int checkUserExist(int userIdx){
        return this.jdbcTemplate.queryForObject("select EXISTS(select userIdx from user where userIdx=? and status='t') as userExist",
                int.class,
                userIdx);
    }
    public List<GetFlagResForProfile> getFlagResForProfile(int userIdx) {
        String query = "SELECT a.flagIdx, (select userImageUrl as userImgUrl from user where userIdx=?) as userImgUrl,a.userIdx,(select case" +
                "                                                                                                                                   when  count(*) > 0 and  count(*) < 2 then 'Lv.1'" +
                "                                                                                                                                                      when count(*)>= 2 and  count(*) < 4 then 'Lv.2'" +
                "                                                                                                                                                          when  count(*) >= 4 and  count(*) < 6 then 'Lv.3'" +
                "                                                                                                                                                         when  count(*)>= 6 and  count(*) < 8 then 'Lv.4'" +
                "                                                                                                                                                          when  count(*) >= 8 and  count(*) < 10 then 'Lv.5'" +
                "                                                                                                                                                           when  count(*) >= 10 then 'Lv.6' end as level" +
                "                                                                                                 from flag where  flag.userIdx=a.userIdx and a.status='t')" +
                "                                                                                   as level, (select name as userName from user where userIdx=?)as userName,a.mountainIdx, a.createdAt, a.pictureUrl, b.cnt, b.name from flag a left join (Select ANY_VALUE(f.userIdx) as userIdx," +
                " ANY_VALUE(f.mountainIdx) as mountainIdx, COUNT(f.mountainIdx) as cnt, m.name  " +
                "from flag f LEFT JOIN mountain m ON f.mountainIdx = m.mountainIdx group by f.mountainIdx) b on a.mountainIdx = b.mountainIdx where a.userIdx = ?  ";

        Object[] param = new Object[]{userIdx,userIdx,userIdx};
        List<GetFlagResForProfile> getFlagRes = this.jdbcTemplate.query(query,param,(rs,rowNum) -> new GetFlagResForProfile(
                rs.getLong("flagIdx"),
                rs.getString("userImgUrl"),
                rs.getInt("userIdx"),
                rs.getString("level"),
                rs.getString("userName"),
                rs.getLong("mountainIdx"),
                rs.getTimestamp("createdAt").toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                rs.getString("pictureUrl"),
                rs.getInt("cnt"),
                rs.getString("name")
        ));
        return getFlagRes;
    }
    public List<GetPicturesRes> getPicturesRes(int userIdx, int userIdxByJwt) {
        String query = "select picture.pictureIdx ,user.userImageUrl,user.userIdx, (select" +
                "                                                                                                 case" +
                "                                                                                               when  count(*) > 0 and  count(*) < 2 then 'Lv.1'" +
                "                                                                                                               when count(*)>= 2 and  count(*) < 4 then 'Lv.2'" +
                "                                                                                                              when  count(*) >= 4 and  count(*) < 6 then 'Lv.3'" +
                "                                                                                                            when  count(*)>= 6 and  count(*) < 8 then 'Lv.4'" +
                "                                                                                                               when  count(*) >= 8 and  count(*) < 10 then 'Lv.5'" +
                "                                                                                                              when  count(*) >= 10 then 'Lv.6' end as level" +
                "                                                     from flag where  flag.userIdx=user.userIdx and user.status='t')" +
                "                                     as level,user.name as userName,picture.createdAt," +
                "                   picture.imgUrl as imgUrl,   case when EXISTS(select picturesaveIdx from picturesave where picturesave.userIdx=? and picturesave.pictureIdx=picture.pictureIdx  and picturesave.status='T') =1 then 'T' else 'F' end as isSaved," +
                "                                                       (select count(*) from picturerecomment where picturerecomment.picturecommentIdx=picturecomment.picturecommentIdx  ) +(select count(*) from picturecomment where picturecomment.pictureIdx=picture.pictureIdx)  as commentCount" +
                "                                                      ,(select count(*) from picturesave where picturesave.pictureIdx=picture.pictureIdx and picturesave.status='t') as saveCount" +
                "                                      from picture" +
                "                                          left join picturesave on picture.pictureIdx = picturesave.pictureIdx" +
                "                                         inner join user on picture.userIdx = user.userIdx" +
                "" +
                " left join picturecomment on picture.pictureIdx = picturecomment.pictureIdx" +
                "                                       where user.status='t' and picture.userIdx=? and picture.status='t' group by picture.pictureIdx order by picture.createdAt " ;

        Object[] param = new Object[]{userIdxByJwt,userIdx};
        List<GetPicturesRes> getPicturesRes = this.jdbcTemplate.query(query,param,(rs,rowNum) -> new GetPicturesRes(
                rs.getLong("pictureIdx"),
                rs.getString("userImageUrl"),
                rs.getInt("userIdx"),
                rs.getString("level"),
                rs.getString("userName"),
                rs.getString("imgUrl"),
                rs.getTimestamp("createdAt").toLocalDateTime(),

                rs.getString("isSaved"),
                rs.getInt("commentCount"),
                rs.getInt("saveCount")
        ));
        return getPicturesRes;
    }

    public List<GetFlagRes> getFlagRes(int userIdx,int userIdxByJwt) {
        String query = "select flag.flagIdx ,user.userImageUrl as userImgUrl,user.userIdx, (select" +
                "                                                                                                 case" +
                "                                                                                               when  count(*) > 0 and  count(*) < 2 then 'Lv.1'" +
                "                                                                                                               when count(*)>= 2 and  count(*) < 4 then 'Lv.2'" +
                "                                                                                                              when  count(*) >= 4 and  count(*) < 6 then 'Lv.3'" +
                "                                                                                                            when  count(*)>= 6 and  count(*) < 8 then 'Lv.4'" +
                "                                                                                                               when  count(*) >= 8 and  count(*) < 10 then 'Lv.5'" +
                "                                                                                                              when  count(*) >= 10 then 'Lv.6' end as level" +
                "                                                     from flag where  flag.userIdx=user.userIdx and user.status='t')" +
                "                                     as level,user.name as userName,flag.createdAt," +
                "                   flag.pictureUrl as pictureUrl, case when EXISTS(select flagsaveIdx from flagsave where flagsave.userIdx=? and flagsave.flagIdx=flag.flagIdx and flagsave.status='t') =1 then 'T' else 'F' end as isSaved," +
                "                                                       (select count(*) from flagrecomment where flagrecomment.flagcommentIdx=flagcomment.flagcommentIdx  ) +(select count(*) from flagcomment where flagcomment.flagIdx=flag.flagIdx)  as commentCount" +
                "                                                       ,(select count(*) from flagsave where flagsave.flagIdx=flag.flagIdx and flagsave.status='t') as saveCount" +
                "                                      from flag" +
                "                                          left join flagsave on flag.flagIdx = flagsave.flagIdx" +
                "                                         inner join user on flag.userIdx = user.userIdx" +
                "                                        left join flagcomment on flag.flagIdx = flagcomment.flagIdx" +
                "                                       where  user.status='t' and flag.userIdx=? and flag.status='t' group by flag.flagIdx order by flag.createdAt " ;

        Object[] param = new Object[]{userIdxByJwt,userIdx};
        List<GetFlagRes> getFlagRes = this.jdbcTemplate.query(query,param,(rs,rowNum) -> new GetFlagRes(
                rs.getLong("flagIdx"),
                rs.getString("userImgUrl"),
                rs.getInt("userIdx"),
                rs.getString("level"),
                rs.getString("userName"),
                rs.getTimestamp("createdAt").toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                rs.getString("pictureUrl"),

                rs.getString("isSaved"),
                rs.getInt("commentCount"),
                rs.getInt("saveCount")


        ));
        return getFlagRes;
    }

    public GetUserRes getUserRes(int userIdx) {
        String query = "select userImageUrl,name from user where userIdx =?";
        Object[] param = new Object[]{userIdx};
        return this.jdbcTemplate.queryForObject(query,param,(rs, rowNum) -> new GetUserRes(
                rs.getString("userImageUrl"),
                rs.getString("name")
        ));

    }


    public GetUserLoginInfoRes getUserLoginInfoRes(int userIdx) {
        String query = "\n" +
                "    select case when pw='kakao'  then  'kakao' when  pw='apple' then 'apple' else emailId end as userLoginInfo\n" +
                "    from user where userIdx =?";
        Object[] param = new Object[]{userIdx};
        return this.jdbcTemplate.queryForObject(query,param,(rs, rowNum) -> new GetUserLoginInfoRes(
                rs.getString("userLoginInfo")
        ));

    }

    public GetFlagCountRes getFlagCounts(int userIdx) {
        String query = "SELECT count(case when b.count>0 and b.count<4 then 1 end) as firstFlag, count(case when b.count>3 and b.count<7 then 1 end) as secondFlag\n" +
                ", count(case when b.count>6  then 1 end) as thirdFlag\n" +
                "FROM flag a\n" +
                "         INNER JOIN (\n" +
                "    SELECT max(count) as count, flagIdx,mountainIdx\n" +
                "    FROM flag b where userIdx=? and status='t' \n" +
                "             group by b.mountainIdx\n" +
                ") b ON b.flagIdx = a.flagIdx\n" +
                "where a.userIdx=? and a.mountainIdx=b.mountainIdx";
        Object[] param = new Object[]{userIdx,userIdx};
        return this.jdbcTemplate.queryForObject(query,param,(rs, rowNum) -> new GetFlagCountRes(
                rs.getInt("firstFlag"),
                rs.getInt("secondFlag"),
                rs.getInt("thirdFlag")
        ));

    }
    public List<GetMapRes> getMapRes(int userIdx) {
        String query = "Select ANY_VALUE(f.userIdx) as userIdx, ANY_VALUE(f.mountainIdx) as mountainIdx, COUNT(f.mountainIdx) as cnt, m.name, m.imageUrl, m.latitude, m.longitude, m.address from flag f LEFT JOIN mountain m ON f.mountainIdx = m.mountainIdx where f.useridx = ? group by f.mountainIdx";
        Object[] param = new Object[]{userIdx};
        List<GetMapRes> getMapRes = this.jdbcTemplate.query(query,param,(rs,rowNum) -> new GetMapRes(
                rs.getInt("userIdx"),
                rs.getLong("mountainIdx"),
                rs.getString("name"),
                rs.getString("imageUrl"),
                rs.getDouble("latitude"),
                rs.getDouble("longitude"),
                rs.getInt("cnt"),
                rs.getString("address")
        ));
        return getMapRes;
    }

    public List<GetMountainsRes> getMountainsRes(int userIdx) {
        String getMountainQuery = "select mountain.mountainIdx as mountainIdx,mountain.imageUrl as mountainImageUrl,\n" +
                "       case when   (select count(picklistIdx) as hot from picklist  where status='t' and picklist.mountainIdx=mountain.mountainIdx) > 10 then 't' else 'f'\n" +
                "end as isHot ,\n" +
                "\n" +
                "       case when mountain.high<500 then 1\n" +
                "            when mountain.high<800  then 2\n" +
                "            when mountain.high<1000 then 3\n" +
                "            when mountain.high<1300 then 4\n" +
                "            else 5 end as difficulty,\n" +
                "       mountain.name as mountainName,  concat('(', mountain.high, 'm)') as high,\n" +
                "       case when   (select exists(select picklistIdx  from picklist  where status='t' and picklist.mountainIdx=mountain.mountainIdx and picklist.userIdx=?)) =1 then 't' else 'f'\n" +
                "           end as isSaved\n" +
                "from mountain\n" +
                "                                                                  left join flag f on mountain.mountainIdx = f.mountainIdx\n" +
                "\n" +
                "                                                                  where f.userIdx=? and f.status='t' group by f.mountainIdx;\n";
        Object[] param = new Object[]{userIdx};
        List<GetMountainsRes> getMountainsRes = this.jdbcTemplate.query(getMountainQuery,param,(rs,rowNum) -> new GetMountainsRes(
                rs.getInt("mountainIdx"),
                rs.getString("mountainImageUrl"),
                rs.getString("isHot"),
                rs.getInt("difficulty"),
                rs.getString("mountainName"),
                rs.getString("high"),
                rs.getString("isSaved")
        ));
        return getMountainsRes;
    }


    public List<GetMountainsRes> getMountainsForListRes(int userIdx,int order) {
        String getMountainQuery="";
       if(order==1) {
           getMountainQuery = "select mountain.mountainIdx as mountainIdx,mountain.imageUrl as mountainImageUrl,\n" +
                   "       case when   (select count(picklistIdx) as hot from picklist  where status='t' and picklist.mountainIdx=mountain.mountainIdx) > 10 then 't' else 'f'\n" +
                   "end as isHot ,\n" +
                   "\n" +
                   "       case when mountain.high<500 then 1\n" +
                   "            when mountain.high<800  then 2\n" +
                   "            when mountain.high<1000 then 3\n" +
                   "            when mountain.high<1300 then 4\n" +
                   "            else 5 end as difficulty,\n" +
                   "       mountain.name as mountainName,  concat('(', mountain.high, 'm)') as high,\n" +
                   "       case when   (select exists(select picklistIdx  from picklist  where status='t' and picklist.mountainIdx=mountain.mountainIdx and picklist.userIdx=?)) =1 then 't' else 'f'\n" +
                   "           end as isSaved, MAX(f.createdAt) as orderColumn\n" +
                   "from mountain\n" +
                   "                                                                  left join flag f on mountain.mountainIdx = f.mountainIdx\n" +
                   "\n" +
                   "                                                                  where f.userIdx=? and f.status='t' group by f.mountainIdx\n" +
                   "order by orderColumn desc;";
       }
       else if (order==2){
           getMountainQuery="select mountain.mountainIdx as mountainIdx,mountain.imageUrl as mountainImageUrl,\n" +
                   "       case when   (select count(picklistIdx) as hot from picklist  where status='t' and picklist.mountainIdx=mountain.mountainIdx) > 10 then 't' else 'f'\n" +
                   "           end as isHot ,\n" +
                   "\n" +
                   "       case when mountain.high<500 then 1\n" +
                   "            when mountain.high<800  then 2\n" +
                   "            when mountain.high<1000 then 3\n" +
                   "            when mountain.high<1300 then 4\n" +
                   "            else 5 end as difficulty,\n" +
                   "       mountain.name as mountainName,  concat('(', mountain.high, 'm)') as high,\n" +
                   "       case when   (select exists(select picklistIdx  from picklist  where status='t' and picklist.mountainIdx=mountain.mountainIdx and picklist.userIdx=?)) =1 then 't' else 'f'\n" +
                   "           end as isSaved, MAX(f.createdAt) as orderColumn\n" +
                   "from mountain\n" +
                   "         left join flag f on mountain.mountainIdx = f.mountainIdx\n" +
                   "\n" +
                   "where f.userIdx=? and f.status='t' group by f.mountainIdx\n" +
                   "order by orderColumn ;";
       }
       else if (order==3)
       {
           getMountainQuery="select mountain.mountainIdx as mountainIdx,mountain.imageUrl as mountainImageUrl,\n" +
                   "       case when   (select count(picklistIdx) as hot from picklist  where status='t' and picklist.mountainIdx=mountain.mountainIdx) > 10 then 't' else 'f'\n" +
                   "           end as isHot ,\n" +
                   "\n" +
                   "       case when mountain.high<500 then 1\n" +
                   "            when mountain.high<800  then 2\n" +
                   "            when mountain.high<1000 then 3\n" +
                   "            when mountain.high<1300 then 4\n" +
                   "            else 5 end as difficulty,\n" +
                   "       mountain.name as mountainName,  concat('(', mountain.high, 'm)') as high,\n" +
                   "       case when   (select exists(select picklistIdx  from picklist  where status='t' and picklist.mountainIdx=mountain.mountainIdx and picklist.userIdx=?)) =1 then 't' else 'f'\n" +
                   "           end as isSaved, count(*) as orderColumn\n" +
                   "from mountain\n" +
                   "         left join flag f on mountain.mountainIdx = f.mountainIdx\n" +
                   "\n" +
                   "where f.userIdx=? and f.status='t' group by f.mountainIdx\n" +
                   " order by orderColumn desc;\n";
       }
       else if (order==4){
           getMountainQuery="select mountain.mountainIdx as mountainIdx,mountain.imageUrl as mountainImageUrl,\n" +
                   "       case when   (select count(picklistIdx) as hot from picklist  where status='t' and picklist.mountainIdx=mountain.mountainIdx) > 10 then 't' else 'f'\n" +
                   "           end as isHot ,\n" +
                   "\n" +
                   "       case when mountain.high<500 then 1\n" +
                   "            when mountain.high<800  then 2\n" +
                   "            when mountain.high<1000 then 3\n" +
                   "            when mountain.high<1300 then 4\n" +
                   "            else 5 end as difficulty,\n" +
                   "       mountain.name as mountainName,  concat('(', mountain.high, 'm)') as high,\n" +
                   "       case when   (select exists(select picklistIdx  from picklist  where status='t' and picklist.mountainIdx=mountain.mountainIdx and picklist.userIdx=?)) =1 then 't' else 'f'\n" +
                   "           end as isSaved, mountain.high as orderColumn\n" +
                   "from mountain\n" +
                   "         left join flag f on mountain.mountainIdx = f.mountainIdx\n" +
                   "\n" +
                   "where f.userIdx=? and f.status='t' group by f.mountainIdx\n" +
                   "order by orderColumn desc;";
       }
       else {
           getMountainQuery ="select mountain.mountainIdx as mountainIdx,mountain.imageUrl as mountainImageUrl,\n" +
                   "       case when   (select count(picklistIdx) as hot from picklist  where status='t' and picklist.mountainIdx=mountain.mountainIdx) > 10 then 't' else 'f'\n" +
                   "           end as isHot ,\n" +
                   "\n" +
                   "       case when mountain.high<500 then 1\n" +
                   "            when mountain.high<800  then 2\n" +
                   "            when mountain.high<1000 then 3\n" +
                   "            when mountain.high<1300 then 4\n" +
                   "            else 5 end as difficulty,\n" +
                   "       mountain.name as mountainName,  concat('(', mountain.high, 'm)') as high,\n" +
                   "       case when   (select exists(select picklistIdx  from picklist  where status='t' and picklist.mountainIdx=mountain.mountainIdx and picklist.userIdx=?)) =1 then 't' else 'f'\n" +
                   "           end as isSaved, mountain.high as orderColumn\n" +
                   "from mountain\n" +
                   "         left join flag f on mountain.mountainIdx = f.mountainIdx\n" +
                   "\n" +
                   "where f.userIdx=? group by f.mountainIdx\n" +
                   "order by orderColumn ;";
       }
        Object[] param = new Object[]{userIdx,userIdx};
        List<GetMountainsRes> getMountainsRes = this.jdbcTemplate.query(getMountainQuery,param,(rs,rowNum) -> new GetMountainsRes(
                rs.getInt("mountainIdx"),
                rs.getString("mountainImageUrl"),
                rs.getString("isHot"),
                rs.getInt("difficulty"),
                rs.getString("mountainName"),
                rs.getString("high"),
                rs.getString("isSaved")
        ));
        return getMountainsRes;
    }

    public int postPictureRes(int userIdx,String imageUrl) {
        String query = "insert into picture (userIdx, imgUrl) VALUES (?,?)";
        Object[] params = new Object[]{userIdx,imageUrl};
        this.jdbcTemplate.update(query, params);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);
    }

    public int patchProfileImg(int userIdx, String filename) {
        String query = "update user set userImageUrl = ? where userIdx = ? ";
        Object[] params = new Object[]{filename, userIdx};
        return this.jdbcTemplate.update(query,params);
    }


    public int deleteProfileImg(int userIdx) {
        String query = "update user set userImageUrl = null where userIdx = ? ";
        int param = userIdx;
        return this.jdbcTemplate.update(query,param);
    }

    public int getFlagCount(int userIdx) {
        return this.jdbcTemplate.queryForObject("SELECT COUNT(*) FROM flag WHERE userIdx = ?",new Object[]{userIdx}, Integer.class);
    }


    public int getDiffFlagCount(int userIdx) {
        return this.jdbcTemplate.queryForObject("SELECT COUNT(A.mountainIdx) AS cnt FROM (SELECT DISTINCT mountainIdx FROM flag WHERE userIdx = ?) A",new Object[]{userIdx}, Integer.class);
    }


    public int getHighSum(int userIdx) {
        return this.jdbcTemplate.queryForObject("SELECT COALESCE(SUM(height),0) as sum FROM flag WHERE userIdx = ?",new Object[]{userIdx}, Integer.class);
    }
    public List<GetCommentRes> getPictureCommentRes(long pictureIdx){
        return this.jdbcTemplate.query("select user.userIdx, user.userImageUrl,user.name as userName, picturecomment.contents  from picturecomment inner join user on picturecomment.userIdx = user.userIdx\n" +
                "  where picturecomment.pictureIdx=? and user.status='t' and picturecomment.status='t'  order by picturecomment.createdAt limit 1",(rs,rownum2)  -> new GetCommentRes(
                rs.getInt("userIdx"),
                rs.getString("userImageUrl"),
                rs.getString("userName"),
                rs.getString("contents"),
                this.jdbcTemplate.queryForObject("select ((select count(*) from picturerecomment where picturerecomment.picturecommentIdx=picturecomment.picturecommentIdx   ) +count(*) ) as count\n" +
                        "from picturecomment  where pictureIdx=?  order by createdAt limit 1;",(rl,rownum3) -> rl.getInt("count"),pictureIdx)

                ),pictureIdx);
    }
    public List<GetCommentRes> getFlagCommentRes(long flagIdx){
        return this.jdbcTemplate.query("select user.userIdx, user.userImageUrl,user.name as userName, flagcomment.contents  from flagcomment inner join user on flagcomment.userIdx = user.userIdx" +
                "               where flagcomment.flagIdx=? and user.status='t' and flagcomment.status='t' order by flagcomment.createdAt limit 1",(rs,rownum2)  -> new GetCommentRes(
                rs.getInt("userIdx"),
                rs.getString("userImageUrl"),
                rs.getString("userName"),
                rs.getString("contents"),
                this.jdbcTemplate.queryForObject("select ((select count(*) from flagrecomment where flagrecomment.flagcommentIdx=flagcomment.flagcommentIdx   ) +count(*) ) as count\n" +
                        "from flagcomment  where flagIdx=?  order by createdAt limit 1;",(rl,rownum3) -> rl.getInt("count"),flagIdx)

        ),flagIdx);
    }

}
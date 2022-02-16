package com.smileflower.santa.src.profile;



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
    public List<GetPicturesRes> getPicturesRes(int userIdx) {
        String query = "select * from picture where userIdx =?";
        Object[] param = new Object[]{userIdx};
        List<GetPicturesRes> getPicturesRes = this.jdbcTemplate.query(query,param,(rs,rowNum) -> new GetPicturesRes(
                rs.getLong("pictureIdx"),
                rs.getInt("userIdx"),
                rs.getString("imgUrl"),
                rs.getTimestamp("createdAt").toLocalDateTime(),
                rs.getTimestamp("updatedAt").toLocalDateTime(),
                rs.getString("status")
        ));
        return getPicturesRes;
    }

    public List<GetFlagRes> getFlagRes(int userIdx) {
        String query = "SELECT a.flagIdx, a.userIdx, a.mountainIdx, a.createdAt, a.pictureUrl, b.cnt, b.name from flag a left join (Select ANY_VALUE(f.userIdx) as userIdx, ANY_VALUE(f.mountainIdx) as mountainIdx, COUNT(f.mountainIdx) as cnt, m.name  from flag f LEFT JOIN mountain m ON f.mountainIdx = m.mountainIdx group by f.mountainIdx) b on a.mountainIdx = b.mountainIdx where a.useridx = ?";
        Object[] param = new Object[]{userIdx};
        List<GetFlagRes> getFlagRes = this.jdbcTemplate.query(query,param,(rs,rowNum) -> new GetFlagRes(
                rs.getLong("flagIdx"),
                rs.getInt("userIdx"),
                rs.getLong("mountainIdx"),
                rs.getTimestamp("createdAt").toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                rs.getString("pictureUrl"),
                rs.getInt("cnt"),
                rs.getString("name")
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

    public GetFlagCountRes getFlagCounts(int userIdx) {
        String query = "SELECT count(case when b.count>0 and b.count<4 then 1 end) as firstFlag, count(case when b.count>3 and b.count<7 then 1 end) as secondFlag\n" +
                ", count(case when b.count>6  then 1 end) as thirdFlag\n" +
                "FROM flag a\n" +
                "         INNER JOIN (\n" +
                "    SELECT max(count) as count, flagIdx,mountainIdx\n" +
                "    FROM flag b where userIdx=?\n" +
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
                "                                                                  where f.userIdx=? group by f.mountainIdx;\n";
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
                   "                                                                  where f.userIdx=? group by f.mountainIdx\n" +
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
                   "where f.userIdx=? group by f.mountainIdx\n" +
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
                   "where f.userIdx=? group by f.mountainIdx\n" +
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
                   "where f.userIdx=? group by f.mountainIdx\n" +
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

}
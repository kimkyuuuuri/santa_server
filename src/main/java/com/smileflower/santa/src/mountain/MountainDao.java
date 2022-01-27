package com.smileflower.santa.src.mountain;


import com.smileflower.santa.src.mountain.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class MountainDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {

        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<GetMountainRes> getMountain(int userIdx) {
        return this.jdbcTemplate.query("select m.mountainIdx,\n" +
                        "                                       m.imageUrl                as mountainImg,\n" +
                        "                                       m.name                    as mountainName,\n" +
                        "                                       case when m.high<500 then 1\n" +
                        "                                           when m.high<800  then 2\n" +
                        "                                           when m.high<1000 then 3\n" +
                        "                                           when m.high<1300 then 4\n" +
                        "                                           else 5 end as difficulty\n" +
                        "                                        ,\n" +
                        "                                       concat('(', m.high, 'm)') as high,\n" +
                        "                                       case when a.hot > 10 then 'T' else 'F' end as hot,\n" +
                        "                                       case when b.status = 'T' then 'T' else 'F' end as pick\n" +
                        "                                          , ( select ( case when EXISTS(select flagIdx from flag where flag.mountainIdx=m.mountainIdx and flag.status='t')\n" +
                        "                                                       then 'T' else 'F' end ) )as competing" +
                        "                                from mountain m\n" +
                        "                                         left join difficulty d on m.mountainIdx = d.mountainIdx\n" +
                        "                                         left join (select mountainIdx, count(picklistIdx) as hot from picklist group by mountainIdx) a\n" +
                        "                                                   on a.mountainIdx = m.mountainIdx\n" +
                        "                                left join (select mountainIdx,status from picklist where userIdx =?) b on b.mountainIdx=m.mountainIdx\n" +
                        "                                group by m.mountainIdx\n" +
                        "                                order by m.mountainIdx",
                (rs, rowNum) -> new GetMountainRes(
                        rs.getInt("mountainIdx"),
                        rs.getString("mountainImg"),
                        rs.getString("mountainName"),
                        rs.getInt("difficulty"),
                        rs.getString("high"),
                        rs.getString("hot"),
                        rs.getString("pick"),
                        rs.getString("competing")),
                userIdx);
    }

    public List<GetMountainRes> getMountainPaging(int userIdx,int index) {

        return this.jdbcTemplate.query("select m.mountainIdx,\n" +
                        "                                       m.imageUrl                as mountainImg,\n" +
                        "                                       m.name                    as mountainName,\n" +
                        "                                       case when m.high<500 then 1\n" +
                        "                                           when m.high<800  then 2\n" +
                        "                                           when m.high<1000 then 3\n" +
                        "                                           when m.high<1300 then 4\n" +
                        "                                           else 5 end as difficulty\n" +
                        "                                        ,\n" +
                        "                                       concat('(', m.high, 'm)') as high,\n" +
                        "                                       case when a.hot > 10 then 'T' else 'F' end as hot,\n" +
                        "                                       case when b.status = 'T' then 'T' else 'F' end as pick" +
                        "                                 , ( select ( case when EXISTS(select flagIdx from flag where flag.mountainIdx=m.mountainIdx and flag.status='t')\n" +
                        "                                   then 'T' else 'F' end ) )as competing" +

                        "                                from mountain m\n " +
                        "                                         left join difficulty d on m.mountainIdx = d.mountainIdx\n" +
                        "                                         left join (select mountainIdx, count(picklistIdx) as hot from picklist group by mountainIdx) a\n" +
                        "                                                   on a.mountainIdx = m.mountainIdx\n" +
                        "                                left join (select mountainIdx,status from picklist where userIdx =?) b on b.mountainIdx=m.mountainIdx\n" +
                        "                                group by m.mountainIdx\n" +
                        "                                order by m.mountainIdx limit 20 OFFSET ?;",
                (rs, rowNum) -> new GetMountainRes(
                        rs.getInt("mountainIdx"),
                        rs.getString("mountainImg"),
                        rs.getString("mountainName"),
                        rs.getInt("difficulty"),
                        rs.getString("high"),
                        rs.getString("hot"),
                        rs.getString("pick"),
                        rs.getString("competing")
                ),
                userIdx,(index-1)*20);
    }
    public GetMountainIdxRes getMountainIdx(String mountain) {
        return this.jdbcTemplate.queryForObject("select mountainIdx from mountain where mountain.name=?\n",
                (rs, rowNum) -> new GetMountainIdxRes(
                        rs.getInt("mountainIdx")),
                mountain);
    }
    public int checkMountain(String mountain){
        return this.jdbcTemplate.queryForObject("select exists(select mountainIdx from mountain where mountain.name=?)",int.class,mountain);
    }
    public String checkUserImage(int userIdx){
        return this.jdbcTemplate.queryForObject("" +
                "select userImageUrl from SantaDB.user where userIdx=?",String.class,userIdx);
    }

    public List<GetRoadRes> getRoad(int mountainIdx){
        return this.jdbcTemplate.query("select roadIdx,\n" +
                        "\n" +
                        "                       concat(row_number() over (order by roadIdx),'코스')              as courseNum,\n" +
                        "                       road.difficulty,\n" +
                        "                       concat(road.length, 'km') as length,\n" +
                        "                       road.time as time ,\n" +
                        "                       course,\n" +
                        "                       road.latitude,\n" +
                        "                       road.longitude\n" +
                        "                from road\n" +
                        "                where mountainIdx = ?;",
                (rs, rowNum) -> new GetRoadRes(
                        rs.getInt("roadIdx"),
                        rs.getString("courseNum"),
                        rs.getInt("difficulty"),
                        rs.getString("length"),
                        rs.getString("time"),
                        rs.getString("course"),
                        rs.getDouble("latitude"),
                        rs.getDouble("longitude")),
                mountainIdx);
    }

    public List<GetRankRes> getRank(int mountainIdx){
        return this.jdbcTemplate.query("select a.ranking, a.userIdx, userName, userImage, flagCount,b.agoTime,\n" +
                        "                                    case\n" +
                        "                                        when a.flagCount2 > 0 and a.flagCount2 < 2 then 'Lv1'\n" +
                        "                                          when a.flagCount2 >= 2 and a.flagCount2 < 4 then 'Lv2'\n" +
                        "                                           when a.flagCount2 >= 4 and a.flagCount2 < 6 then 'Lv3'\n" +
                        "                                            when a.flagCount2 >= 6 and a.flagCount2 < 8 then 'Lv4'\n" +
                        "                                           when a.flagCount2 >= 8 and a.flagCount2 < 10 then 'Lv5'\n" +
                        "                                               when a.flagCount2 >= 10 then 'Lv6' end as level\n" +
                        "                                   from (select row_number() over (order by COUNT(f.userIdx) desc, f.createdAt desc) as ranking,\n" +
                        "                                                   f.userIdx,\n" +
                        "                                                          u.name                                                               as userName,\n" +
                        "                                                           u.userImageUrl                                                       as userImage,\n" +
                        "                                                (select count(*) from flag where  userIdx=f.userIdx)\n" +
                        "                                                                                                      as flagCount2,\n" +
                        "                                             COUNT(f.userIdx)                                                     as flagCount\n" +
                        "\n" +
                        "                                                from flag f\n" +
                        "                                                             inner join mountain m on f.mountainIdx = m.mountainIdx\n" +
                        "                                                               inner join user u on f.userIdx = u.userIdx\n" +
                        "                                                      where f.mountainIdx = ?\n" +
                        "\n" +
                        "                                                     group by f.userIdx\n" +
                        "                                                     order by ranking) a inner join (select userIdx,(case\n" +
                        "                                                            when timestampdiff(minute , max(f.createdAt), current_timestamp()) < 60\n" +
                        "                                                              then concat(timestampdiff(minute, max(f.createdAt), current_timestamp()), '분전')\n" +
                        "                                                          when timestampdiff(hour, max(f.createdAt), current_timestamp()) < 24\n" +
                        "                                                              then concat(timestampdiff(hour , max(f.createdAt), current_timestamp()), '시간전')\n" +
                        "                                                          ELSE\n" +
                        "                                                              concat(timestampdiff(day, max(f.createdAt), current_timestamp()), '일전') end)      agoTime\n" +
                        "                                                from flag f where mountainIdx= ? group by userIdx)b on a.userIdx=b.userIdx order by ranking;;",
                (rs, rowNum) -> new GetRankRes(
                        rs.getInt("ranking"),
                        rs.getInt("userIdx"),
                        rs.getString("level"),
                        rs.getString("userName"),
                        rs.getString("userImage"),
                        rs.getInt("flagCount"),
                        rs.getString("agoTime")),
               mountainIdx,mountainIdx);
    }
    public GetRankRes getmyRank(int userIdx, int mountainIdx){
        return this.jdbcTemplate.queryForObject("select a.ranking, a.userIdx, userName, userImage, flagCount,b.agoTime,\n" +
                        "                                    case\n" +
                        "                                        when a.flagCount2 > 0 and a.flagCount2 < 2 then 'Lv1'\n" +
                        "                                          when a.flagCount2 >= 2 and a.flagCount2 < 4 then 'Lv2'\n" +
                        "                                           when a.flagCount2 >= 4 and a.flagCount2 < 6 then 'Lv3'\n" +
                        "                                            when a.flagCount2 >= 6 and a.flagCount2 < 8 then 'Lv4'\n" +
                        "                                           when a.flagCount2 >= 8 and a.flagCount2 < 10 then 'Lv5'\n" +
                        "                                               when a.flagCount2 >= 10 then 'Lv6' end as level\n" +
                        "                                   from (select row_number() over (order by COUNT(f.userIdx) desc, f.createdAt desc) as ranking,\n" +
                        "                                                   f.userIdx,\n" +
                        "                                                          u.name                                                               as userName,\n" +
                        "                                                           u.userImageUrl                                                       as userImage,\n" +
                        "                                                (select count(*) from flag where  userIdx=f.userIdx)\n" +
                        "                                                                                                      as flagCount2,\n" +
                        "                                             COUNT(f.userIdx)                                                     as flagCount\n" +
                        "\n" +
                        "                                                from flag f\n" +
                        "                                                             inner join mountain m on f.mountainIdx = m.mountainIdx\n" +
                        "                                                               inner join user u on f.userIdx = u.userIdx\n" +
                        "                                                      where f.mountainIdx = ?\n" +
                        "\n" +
                        "                                                     group by f.userIdx\n" +
                        "                                                     order by ranking) a inner join (select userIdx,(case\n" +
                        "                                                            when timestampdiff(minute , max(f.createdAt), current_timestamp()) < 60\n" +
                        "                                                              then concat(timestampdiff(minute, max(f.createdAt), current_timestamp()), '분전')\n" +
                        "                                                          when timestampdiff(hour, max(f.createdAt), current_timestamp()) < 24\n" +
                        "                                                              then concat(timestampdiff(hour , max(f.createdAt), current_timestamp()), '시간전')\n" +
                        "                                                          ELSE\n" +
                        "                                                              concat(timestampdiff(day, max(f.createdAt), current_timestamp()), '일전') end)      agoTime\n" +
                        "                                                from flag f where mountainIdx= ? group by userIdx)b on a.userIdx=b.userIdx where b.userIdx=? order by ranking;",
                (rs, rowNum) -> new GetRankRes(
                        rs.getInt("ranking"),
                        rs.getInt("userIdx"),
                        rs.getString("level"),
                        rs.getString("userName"),
                        rs.getString("userImage"),
                        rs.getInt("flagCount"),
                        rs.getString("agoTime")),
              mountainIdx,mountainIdx,userIdx);
    }

    public GetInfoRes getInfo(int userIdx, int mountainIdx){
        return this.jdbcTemplate.queryForObject("select m.mountainIdx,\n" +
                        "       m.imageUrl                                     as mountainImg,\n" +
                        "       m.name                                         as mountainName,\n" +
                        "\n" +
                        "       m.address,\n" +
                        "       case when m.high<500 then 1\n" +
                        "                                           when m.high<800  then 2\n" +
                        "                                           when m.high<1000 then 3\n" +
                        "                                           when m.high<1300 then 4\n" +
                        "                                           else 5 end as difficulty\n" +
                        "        ,\n" +
                        "       concat(m.high, 'm')                            as high,\n" +
                        " m.high as altitude,\n" +
                        "       case when a.hot > 10 then '인기' else null end    as hot,\n" +
                        "       case when b.status = 'T' then 'T' else 'F' end as pick\n" +
                        "\n" +
                        "from mountain m\n" +
                        "\n" +
                        "         left join difficulty d on m.mountainIdx = d.mountainIdx\n" +
                        "         left join (select mountainIdx, count(picklistIdx) as hot from picklist group by mountainIdx) a\n" +
                        "                   on a.mountainIdx = m.mountainIdx\n" +
                        "         left join (select mountainIdx, status from picklist where userIdx = ?) b on b.mountainIdx = m.mountainIdx\n" +
                        "\n" +
                        "where m.mountainIdx = ? group by m.mountainIdx\n" +
                        "                                order by m.mountainIdx;",
                (rs, rowNum) -> new GetInfoRes(
                        rs.getInt("mountainIdx"),
                        rs.getString("mountainImg"),
                        rs.getString("mountainName"),
                        rs.getString("address"),
                        rs.getInt("difficulty"),
                        rs.getString("high"),
                        rs.getString("hot"),
                        rs.getString("pick"),
                        rs.getInt("altitude")),
                userIdx,mountainIdx);
    }

    public GetMapRes getMap(int mountainIdx){
        return this.jdbcTemplate.queryForObject("select  latitude , longitude,high from mountain where mountainIdx =?",
                (rs, rowNum) -> new GetMapRes(
                        rs.getDouble("latitude"),
                        rs.getDouble("longitude"),
                        rs.getInt("high")),
                mountainIdx);
    }

    public int checkMyRank(int userIdx,int mountainIdx){
        return this.jdbcTemplate.queryForObject("select exists(select * from (select row_number() over (order by COUNT(f.userIdx) desc, f.createdAt desc) as ranking,\n" +
                "                       f.userIdx,\n" +
                "                       u.name as userName,\n" +
                "                       u.userImageUrl   as userImage,\n" +
                "                       COUNT(f.userIdx) as flagCount\n" +
                "                      ,case when timediff(now(),f.createdAt)<1 then concat(minute(timediff(now(),f.createdAt)),'분전')\n" +
                "when timediff(now(),f.createdAt)<24 then concat(hour(timediff(now(),f.createdAt)),'시간전') else concat(day(timediff(now(),f.createdAt)),'일전') end agoTime\n" +
                "\n" +
                "                from flag f\n" +
                "                         inner join mountain m on f.mountainIdx = m.mountainIdx\n" +
                "                         inner join user u on f.userIdx = u.userIdx\n" +
                "                where f.mountainIdx = ?\n" +
                "                group by f.userIdx\n" +
                "                order by ranking)a where userIdx=?)",int.class,mountainIdx,userIdx);
    }

    public int updateMountainImg(String name,String imageUrl) {
        String query = "update mountain set imageUrl = ? where name = ?";
        Object[] params = new Object[]{imageUrl, name};
        return this.jdbcTemplate.update(query,params);
    }

    public String findMountainImgByName(String name) {
        String query = "select imageUrl from mountain where name =?";
        try {
            return this.jdbcTemplate.queryForObject(query, new Object[]{name}, String.class);
        } catch (EmptyResultDataAccessException e) {
            // EmptyResultDataAccessException 예외 발생시 null 리턴
            return null;
        }
    }
    public String findImageUrlByName(String name) {
        String query = "select imageUrl from mountain where name = ?";
        String imageUrl = this.jdbcTemplate.queryForObject(query,new Object[]{name},String.class);

        return imageUrl;
    }
}
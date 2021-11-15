package com.smileflower.santa.src.flags;


import com.smileflower.santa.src.flags.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class FlagDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {

        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
    public int checkMountain(String mountain){
        return this.jdbcTemplate.queryForObject("select exists(select * from mountain where name=?)",int.class,mountain);
    }
    public int checkMountainIdx(String mountain){
        return this.jdbcTemplate.queryForObject("select mountainIdx from mountain where name = ?",int.class,mountain);
    }
    public int createFlag(PostFlagPictureReq postFlagPictureReq, int mountainIdx, int userIdx){
        this.jdbcTemplate.update("insert into flag (userIdx,mountainIdx,pictureUrl) Values (?,?,?)",
                userIdx,mountainIdx,postFlagPictureReq.getPictureUrl()
        );
        return this.jdbcTemplate.queryForObject("select last_insert_id()",int.class);
    }
    public int createHard(PostFlagHardReq postFlagHardReq, int mountainIdx, int userIdx){
        this.jdbcTemplate.update("insert into difficulty (userIdx,mountainIdx,difficulty) Values (?,?,?)",
                userIdx,mountainIdx,postFlagHardReq.getDifficulty()
        );
        return this.jdbcTemplate.queryForObject("select last_insert_id()",int.class);
    }

    public int checkPickExist(int userIdx,int mountainIdx){
        return this.jdbcTemplate.queryForObject("select Exists(select status from picklist\n" +
                "where mountainIdx=? and userIdx=? ) as PickExist", int.class,mountainIdx,userIdx);
    }
    public int checkhigh(int mountainIdx){
        return this.jdbcTemplate.queryForObject("select high from mountain where mountainIdx =?", int.class,mountainIdx);
    }

    public char checkPick(int userIdx,int mountainIdx){
        return this.jdbcTemplate.queryForObject("select status from picklist \n" +
                "where mountainIdx =? and userIdx=?", char.class,mountainIdx,userIdx);
    }
    public PatchPickRes patchPick(String status, int userIdx, int mountainIdx){
        this.jdbcTemplate.update("UPDATE picklist set status=? \n" +
                        "where userIdx=? and mountainIdx=?",
                status,userIdx,mountainIdx);
        return this.jdbcTemplate.queryForObject("select picklistIdx as picklistIdx,status,userIdx,mountainIdx from picklist\n" +
                        "where userIdx=? and mountainIdx=?",
                (rs, rowNum) -> new PatchPickRes(
                        rs.getInt("picklistIdx"),
                        rs.getInt("userIdx"),
                        rs.getInt("mountainIdx"),
                        rs.getString("status")),
                userIdx,mountainIdx);
    }


    public PatchPickRes createPick(String status, int userIdx,int mountainIdx){
        this.jdbcTemplate.update("insert into picklist (userIdx,mountainIdx,createdAt,status) VALUES (?,?,now(),?)",
                userIdx,mountainIdx,status);
        return this.jdbcTemplate.queryForObject("select picklistIdx as picklistIdx,userIdx,mountainIdx,status from picklist\n" +
                        "where userIdx=? and mountainIdx=?",
                (rs, rowNum) -> new PatchPickRes(
                        rs.getInt("picklistIdx"),
                        rs.getInt("userIdx"),
                        rs.getInt("mountainIdx"),
                        rs.getString("status")),
                userIdx,mountainIdx);
    }

    public GetRankRes getmyRank(int userIdx,int mountainIdx) {
        return this.jdbcTemplate.queryForObject("select *\n" +
                        "from (select row_number() over (order by COUNT(f.userIdx) desc) as ranking,\n" +
                        "             m.mountainIdx,\n" +
                        "             f.userIdx,\n" +
                        "             u.name                                             as userName,\n" +
                        "             u.userImageUrl                                     as userImage,\n" +
                        "             COUNT(f.userIdx)                                   as flagCount,\n" +
                        "             case\n" +
                        "                 when a.level > 0 and a.level<2 then 'Lv1'\n" +
                        "                 when a.level >= 2 and a.level<4 then 'Lv2'\n" +
                        "                 when a.level >= 4 and a.level<6 then 'Lv3'\n" +
                        "                 when a.level >= 6 and a.level< 8 then 'Lv4'\n" +
                        "                 when a.level >= 8 and a.level<10 then 'Lv5'\n" +
                        "                 when a.level >= 10 then 'Lv6'end               as level\n" +
                        "      from flag f\n" +
                        "               inner join mountain m on f.mountainIdx = m.mountainIdx\n" +
                        "               inner join user u on f.userIdx = u.userIdx\n" +
                        "               inner join (select count(userIdx) as level from flag where userIdx = ?) a\n" +
                        "      where f.mountainIdx = ?\n" +
                        "      group by f.userIdx\n" +
                        "      order by flagCount desc) a\n" +
                        "where userIdx = ?;",
                (rs, rowNum) -> new GetRankRes(
                        rs.getInt("userIdx"),
                        rs.getString("userName"),
                        rs.getString("userImage"),
                        rs.getInt("ranking"),
                        rs.getString("level"),
                        rs.getInt("flagCount")),
                userIdx,mountainIdx, userIdx);
    }



    public GetRankRes getfirstRank(int mountainIdx) {
        return this.jdbcTemplate.queryForObject("select * from (select row_number() over (order by COUNT(f.userIdx) desc) as ranking,\n" +
                        "                       m.mountainIdx,\n" +
                        "                       f.userIdx,\n" +
                        "                       u.name as userName,\n" +
                        "                       u.userImageUrl   as userImage,\n" +
                        "                       COUNT(f.userIdx) as flagCount,\n" +
                        "                      case\n" +
                        "                 when a.level > 0 and a.level<2 then 'Lv1'\n" +
                        "                 when a.level >= 2 and a.level<4 then 'Lv2'\n" +
                        "                 when a.level >= 4 and a.level<6 then 'Lv3'\n" +
                        "                 when a.level >= 6 and a.level< 8 then 'Lv4'\n" +
                        "                 when a.level >= 8 and a.level<10 then 'Lv5'\n" +
                        "                 when a.level >= 10 then 'Lv6'end               as level\n" +
                        "                from flag f\n" +
                        "                         inner join mountain m on f.mountainIdx = m.mountainIdx\n" +
                        "                         inner join user u on f.userIdx = u.userIdx\n" +
                        "inner join (select count(flag.userIdx) as level from flag inner join (select * from (select row_number() over (order by COUNT(f.userIdx) desc) as ranking,\n" +
                        "                               m.mountainIdx,\n" +
                        "                               f.userIdx,\n" +
                        "                               u.name as userName,\n" +
                        "                               u.userImageUrl   as userImage,\n" +
                        "                               COUNT(f.userIdx) as flagCount\n" +
                        "                        from flag f\n" +
                        "                                 inner join mountain m on f.mountainIdx = m.mountainIdx\n" +
                        "                                 inner join user u on f.userIdx = u.userIdx\n" +
                        "                        where f.mountainIdx = ?\n" +
                        "                        group by f.userIdx\n" +
                        "                        order by flagCount desc)a where ranking = 1)c where flag.userIdx =c.userIdx ) a\n" +
                        "                where f.mountainIdx = ?\n" +
                        "                group by f.userIdx\n" +
                        "                order by flagCount desc)a where ranking = 1;",
                (rs, rowNum) -> new GetRankRes(
                        rs.getInt("userIdx"),
                        rs.getString("userName"),
                        rs.getString("userImage"),
                        rs.getInt("ranking"),
                        rs.getString("level"),
                        rs.getInt("flagCount")),
                mountainIdx,mountainIdx);
    }

    public List<GetPickRes> getPick(int userIdx) {
        return this.jdbcTemplate.query("select m.mountainIdx,\n" +
                        "                       m.name as mountainName,\n" +
                        "                       m.imageUrl as mountainImg,\n" +
                        "                       concat('(',m.high,'m)') as high,\n" +
                        "                       case when m.high<500 then 1\n" +
                        "                                           when m.high<800  then 2\n" +
                        "                                           when m.high<1000 then 3\n" +
                        "                                           when m.high<1300 then 4\n" +
                        "                                           else 5 end as difficulty\n" +
                        "                from picklist\n" +
                        "                         inner join mountain m on picklist.mountainIdx = m.mountainIdx\n" +
                        "                where picklist.userIdx = ?\n" +
                        "                  and picklist.status = 'T';",
                (rs, rowNum) -> new GetPickRes(
                        rs.getInt("mountainIdx"),
                        rs.getString("mountainName"),
                        rs.getString("mountainImg"),
                        rs.getInt("difficulty"),
                        rs.getString("high")),
                userIdx);
    }
}
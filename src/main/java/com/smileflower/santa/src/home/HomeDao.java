package com.smileflower.santa.src.home;


import com.smileflower.santa.src.home.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class HomeDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {

        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<GetHomeMountainRes> getHomeMountains(int userIdx,int mountainIdx) {
        return this.jdbcTemplate.query("select m.mountainIdx,\n" +
                        "                                m.name as mountainName,\n" +
                        "                                       m.imageUrl       as mountainImage,\n" +
                        "                                       case when m.high<500 then 1\n" +
                        "                                                           when m.high<800  then 2\n" +
                        "                                                           when m.high<1000 then 3\n" +
                        "                                                           when m.high<1300 then 4\n" +
                        "                                                           else 5 end as difficulty,\n" +
                        "                                       f.userIdx,\n" +
                        "                                       u.name as userName,\n" +
                        "                                       u.userImageUrl   as userImage,\n" +
                        "                                       COUNT(f.userIdx) as flagCount\n" +
                        "                                from flag f\n" +
                        "                                         left join mountain m on f.mountainIdx = m.mountainIdx\n" +
                        "                                         left join user u on f.userIdx = u.userIdx\n" +
                        "                                where f.mountainIdx = ? and f.status='T'\n" +
                        "                                group by f.userIdx\n" +
                        "                                order by flagCount desc, f.createdAt desc\n" +
                        "                                limit 1",
                (rs, rowNum) -> new GetHomeMountainRes(
                        rs.getInt("mountainIdx"),
                        rs.getString("mountainName"),
                        rs.getString("mountainImage"),
                        rs.getInt("difficulty"),
                        rs.getInt("userIdx"),
                        rs.getString("userName"),
                        rs.getString("userImage"),
                        rs.getInt("flagCount")),
                mountainIdx);
    }

    public int checkFlagMountain(int mountainIdx){
        return this.jdbcTemplate.queryForObject("select exists(select m.name as mountainName,\n" +
                "       m.imageUrl       as mountainImage,\n" +
                "       f.userIdx,\n" +
                "       u.name as userName,\n" +
                "       u.userImageUrl   as userImage,\n" +
                "       COUNT(f.userIdx) as flagCount\n" +
                "from flag f\n" +
                "         inner join mountain m on f.mountainIdx = m.mountainIdx\n" +
                "         inner join user u on f.userIdx = u.userIdx\n" +
                "where f.mountainIdx = ? and f.status='f' \n" +
                "group by f.userIdx\n" +
                "order by flagCount desc\n" +
                "limit 1)",int.class,mountainIdx);
    }
    public int checkFlagUser(int userIdx,int mountainIdx){
        return this.jdbcTemplate.queryForObject("select exists(select * from flag where userIdx=? and mountainIdx=? and flag.status='T')",int.class,userIdx,mountainIdx);
    }
    public String getUserImage(int userIdx){
        return this.jdbcTemplate.queryForObject("select userImageUrl from user where userIdx = ?",String.class,userIdx);
    }
}
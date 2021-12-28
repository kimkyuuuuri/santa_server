package com.smileflower.santa.src.profile;


import com.smileflower.santa.profile.model.dto.FlagsForMapResponse;
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

    public List<GetFlagRes>getFlagRes(int userIdx) {
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

    public int postPictureRes(int userIdx,String imageUrl) {
        String query = "insert into picture (userIdx, imgUrl) VALUES (?,?)";
        Object[] params = new Object[]{userIdx,imageUrl};
        this.jdbcTemplate.update(query, params);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);
    }
}
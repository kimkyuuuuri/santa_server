package com.smileflower.santa.flag.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class FlagJdbcRepository implements FlagRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public int updateImageUrlByIdx(int userIdx, Long mountainIdx, String filename, Double altitude) {
        String query = "insert into flag (userIdx, mountainIdx,pictureUrl,height) VALUES (?,?,?,?)";
        Object[] params = new Object[]{userIdx, mountainIdx, filename,altitude};
        this.jdbcTemplate.update(query, params);
        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery, int.class);
    }

    @Override
    public int findTodayFlagByIdx(int userIdx){
        return this.jdbcTemplate.queryForObject("select COUNT(*) from flag " +
                "where userIdx = ? and " +
                "CURRENT_DATE() = date_format(createdAt,'%Y-%m-%d')",new Object[]{userIdx},Integer.class);

    }

    @Override
    public int findIsFlagByLatAndLong(double latitude, double longitude, Long mountainIdx) {
        return this.jdbcTemplate.queryForObject("SELECT DATA.distance < 20 as isFlag\n" +
                        "FROM (\n" +
                        "SELECT (6371 * acos(cos(radians(?)) * cos(radians(m.latitude)) * cos(radians(m.longitude) - radians(?)) + sin(radians(?)) * sin(radians(m.latitude)))) \n" +
                        "\tAS distance\n" +
                        "\tFROM mountain m\n" +
                        "\tWHERE m.mountainIdx = ?\n" +
                        ") DATA\n",
                new Object[]{latitude, longitude, latitude, mountainIdx}, Integer.class);
    }
}

package com.smileflower.santa.src.new_home;


import com.smileflower.santa.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;

// Service Create, Update, Delete 의 로직 처리
@Service
public class New_HomeService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final New_HomeDao newHomeDao;
    private final New_HomeProvider newHomeProvider;
    private final JwtService jwtService;
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Autowired
    public New_HomeService(New_HomeDao newHomeDao, New_HomeProvider newHomeProvider, JwtService jwtService) {
        this.newHomeDao = newHomeDao;
        this.newHomeProvider = newHomeProvider;
        this.jwtService = jwtService;

    }
}
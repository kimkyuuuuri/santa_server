package com.smileflower.santa.src.picture;


import com.smileflower.santa.utils.JwtService;
import com.smileflower.santa.utils.S3Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;



@Service
public class PictureService{

    private final PictureDao pictureDao;
    private final JwtService jwtService;
    private final S3Service s3Service;

    private JdbcTemplate jdbcTemplate;
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    public PictureService(PictureDao pictureDao, JwtService jwtService, S3Service s3Service) {

        this.pictureDao = pictureDao;
        this.jwtService = jwtService;
        this.s3Service = s3Service;
    }


}
package com.smileflower.santa.src.profile;


import com.amazonaws.services.s3.model.ObjectMetadata;
import com.smileflower.santa.profile.model.dto.CreatePictureResponse;
import com.smileflower.santa.src.profile.model.PostPictureRes;
import com.smileflower.santa.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.smileflower.santa.utils.S3Service;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

// Service Create, Update, Delete 의 로직 처리
@Service
public class New_ProfileService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final New_ProfileDao newProfileDao;
    private final New_ProfileProvider newProfileProvider;
    private final JwtService jwtService;
    private JdbcTemplate jdbcTemplate;
    private final S3Service s3Service;
    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Autowired
    public New_ProfileService(New_ProfileDao newProfileDao, New_ProfileProvider newProfileProvider, JwtService jwtService,S3Service s3Service) {
        this.newProfileDao = newProfileDao;
        this.newProfileProvider = newProfileProvider;
        this.jwtService = jwtService;
        this.s3Service = s3Service;

    }
    public PostPictureRes postPictureRes(int userIdx, MultipartFile file) {
        String fileName = createFileName(file.getOriginalFilename());

        ObjectMetadata objectMetadata = new ObjectMetadata();

        objectMetadata.setContentType(file.getContentType());

        try (InputStream inputStream = file.getInputStream()) {
            s3Service.uploadFile(inputStream, objectMetadata, fileName);

        } catch (IOException e) {
            throw new IllegalArgumentException(String.format("파일 변환 중 에러가 발생하였습니다 (%s)", file.getOriginalFilename()));
        }
        return new PostPictureRes(newProfileDao.postPictureRes(userIdx, fileName));
    }

    private String createFileName(String originalFileName){
        return
                UUID.randomUUID().toString().concat(getFileExtension(originalFileName));
    }
    private String getFileExtension(String fileName){
        try{
            return fileName.substring(fileName.lastIndexOf("."));
        }catch(StringIndexOutOfBoundsException e){
            throw new IllegalArgumentException(String.format("잘못된 형식의 파일 (%s) 입니다",fileName));
        }
    }
}
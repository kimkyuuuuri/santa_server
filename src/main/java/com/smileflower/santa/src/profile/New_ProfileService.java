package com.smileflower.santa.src.profile;


import com.amazonaws.services.kms.model.NotFoundException;
import com.amazonaws.services.s3.model.ObjectMetadata;

import com.smileflower.santa.config.BaseException;
import com.smileflower.santa.src.profile.model.GetProfileImgRes;
import com.smileflower.santa.src.profile.model.GetResultRes;
import com.smileflower.santa.src.profile.model.GetUserRes;
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

import static com.smileflower.santa.config.BaseResponseStatus.FILE_ERROR;

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
    public PostPictureRes postPictureRes(int userIdx, MultipartFile file) throws BaseException {
        String fileName = createFileName(file.getOriginalFilename());

        ObjectMetadata objectMetadata = new ObjectMetadata();

        objectMetadata.setContentType(file.getContentType());

        try (InputStream inputStream = file.getInputStream()) {
            s3Service.uploadFile(inputStream, objectMetadata, fileName);

        } catch (IOException e) {
             }
        return new PostPictureRes(newProfileDao.postPictureRes(userIdx, fileName));
    }

    private String createFileName(String originalFileName)throws BaseException{
        return
                UUID.randomUUID().toString().concat(getFileExtension(originalFileName));
    }
    private String getFileExtension(String fileName) throws BaseException {
        try{
            return fileName.substring(fileName.lastIndexOf("."));
        }catch(StringIndexOutOfBoundsException e){
            throw new BaseException(FILE_ERROR);
        }
    }


    public GetProfileImgRes patchProfileImg(MultipartFile file, int userIdx)throws BaseException{

        //delete pre file
        GetUserRes getUserRes= newProfileDao.getUserRes(userIdx);

        if(getUserRes.getUserImageUrl()!=null){
            s3Service.deleteFile(getUserRes.getUserImageUrl());
        }
        String fileName = createFileName(file.getOriginalFilename());

        ObjectMetadata objectMetadata = new ObjectMetadata();

        objectMetadata.setContentType(file.getContentType());

        try(InputStream inputStream = file.getInputStream()){
            s3Service.uploadFile(inputStream,objectMetadata,fileName);
            newProfileDao.patchProfileImg(userIdx,fileName);
        }catch(IOException e){
            throw new BaseException(FILE_ERROR);   }
        return new GetProfileImgRes(s3Service.getFileUrl(fileName));
    }
    public GetProfileImgRes deleteProfileImg(int userIdx){
        //delete file
        GetUserRes getUserRes= newProfileDao.getUserRes(userIdx);
        s3Service.deleteFile(getUserRes.getUserImageUrl());

        newProfileDao.deleteProfileImg(userIdx);

        return new GetProfileImgRes(null);
    }


}
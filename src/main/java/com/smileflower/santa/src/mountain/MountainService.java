package com.smileflower.santa.src.mountain;


import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.util.IOUtils;
import com.smileflower.santa.config.BaseException;
import com.smileflower.santa.config.secret.Secret;
import com.smileflower.santa.flag.model.UploadImageResponse;
import com.smileflower.santa.src.mountain.model.*;
import com.smileflower.santa.src.mountain.MountainDao;
import com.smileflower.santa.src.mountain.MountainProvider;
import com.smileflower.santa.utils.AES128;
import com.smileflower.santa.utils.JwtService;
import com.smileflower.santa.utils.S3Service;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.fasterxml.jackson.databind.ser.Serializers;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;


import javax.sql.DataSource;
import javax.transaction.Transactional;

import java.io.*;
import java.nio.file.Files;
import java.util.UUID;

import static com.smileflower.santa.config.BaseResponseStatus.*;
@Transactional
// Service Create, Update, Delete 의 로직 처리
@Service
public class MountainService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final MountainDao mountainDao;
    private final MountainProvider mountainProvider;
    private final JwtService jwtService;
    private JdbcTemplate jdbcTemplate;
    private final S3Service s3Service;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Autowired
    public MountainService(MountainDao mountainDao, MountainProvider mountainProvider, JwtService jwtService,S3Service s3Service) throws IOException {
        this.mountainDao = mountainDao;
        this.mountainProvider = mountainProvider;
        this.jwtService = jwtService;
        this.s3Service = s3Service;
        //uploadImage();
    }

    public void uploadImage() throws IOException {

        MultipartFile multipartFile;
        /*파일 경로에 있는 파일 가져오기*/
        File rw = new File("/Users/heo/santa/src/main/resources/mountainImage");
        /*파일 경로에 있는 파일 리스트 fileList[] 에 넣기*/
        File[] fileList = rw.listFiles();

        /*fileList에 있는거 for 문 돌려서 출력*/
        for (File file : fileList) {
            if (file.isFile()) {
                String fileName = file.getName();
                System.out.println("fileName : " + fileName);
                DiskFileItem fileItem = new DiskFileItem("file", Files.probeContentType(file.toPath()), false, file.getName(), (int) file.length(), file.getParentFile());
                InputStream input = new FileInputStream(file);
                OutputStream os = fileItem.getOutputStream();
                IOUtils.copy(input, os);
                multipartFile = new CommonsMultipartFile(fileItem);
                String mountainName = FilenameUtils.getBaseName(multipartFile.getOriginalFilename());
                System.out.println("mountainName : " + mountainName);
                String multiPartFileName = createFileName(multipartFile.getOriginalFilename());
                System.out.println("multiPartFileName : " + multiPartFileName);
                if(mountainDao.findMountainImgByName(mountainName)!=null) {
                    s3Service.deleteFile(mountainDao.findMountainImgByName(mountainName));
                    System.out.println("deleteFile : " + mountainName);
                }
                ObjectMetadata objectMetadata = new ObjectMetadata();
                objectMetadata.setContentType(multipartFile.getContentType());
                try (InputStream inputStream = multipartFile.getInputStream()) {
                    String existUrl = mountainDao.findImageUrlByName(mountainName);
                    if(existUrl !=null){
                        System.out.println("deleteFile : " + existUrl);
                        s3Service.deleteFile(existUrl);
                    }
                    s3Service.uploadFile(inputStream, objectMetadata, multiPartFileName);
                    updateMountainImg(mountainName, multiPartFileName);
                } catch (IOException e) {
                    throw new IllegalArgumentException(String.format("파일 변환 중 에러가 발생하였습니다 (%s)", multipartFile.getOriginalFilename()));
                }
            }

        }
    }

    private int updateMountainImg(String name,String imageUrl) {
        return mountainDao.updateMountainImg(name,imageUrl);
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
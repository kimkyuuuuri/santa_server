package com.smileflower.santa.src.picture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.smileflower.santa.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/app/pictures")
public class PictureController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final PictureProvider pictureProvider;
    @Autowired
    private final PictureService pictureService;
    @Autowired
    private final JwtService jwtService;


    public PictureController(PictureProvider pictureProvider, PictureService pictureService, JwtService jwtService) {
        this.pictureProvider = pictureProvider;
        this.pictureService = pictureService;
        this.jwtService = jwtService;
    }


}
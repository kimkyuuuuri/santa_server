package com.smileflower.santa.exception;


import com.amazonaws.services.kms.model.NotFoundException;
import com.smileflower.santa.flag.controller.FlagsController;
import com.smileflower.santa.profile.controller.ProfileController;
import feign.FeignException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import static com.smileflower.santa.exception.ApiResult.ERROR;


@ControllerAdvice(assignableTypes = {ProfileController.class, FlagsController.class})
public class GeneralExceptionHandler {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private ResponseEntity<ApiResult<?>> newResponse(Throwable throwable, HttpStatus status) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<>(ERROR(throwable, status), headers, status);
    }

    // TODO REST API 처리 중 발생한 예외를 catch 하고, 로그를 남기고, ApiResult를 사용해 오류 응답을 전달(완료)
    // ========== HTTP 400 오류 처리 ==========
    @ExceptionHandler({
            IllegalStateException.class, IllegalArgumentException.class,
            TypeMismatchException.class, MissingServletRequestParameterException.class,
    })
    private ResponseEntity<?> handleBadRequestException(Exception e) {
        log.debug("Bad request exception occurred: {}", e.getMessage(), e);
        return newResponse(e, HttpStatus.BAD_REQUEST);
    }
    // ========== HTTP 400 오류 처리 ==========

    // ========== HTTP 405 오류 처리 ==========
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    private ResponseEntity<?> handleHHttpRequestMethodNotSupportedException(Exception e) {
        log.debug("Bad request exception occurred: {}", e.getMessage(), e);
        return newResponse(e, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }
    // ========== HTTP 401 오류 처리 ==========
    @ExceptionHandler({FeignException.class, ExpiredJwtException.class})
    private ResponseEntity<?> FeignException(Exception e) {
        log.debug("Bad request exception occurred: {}", e.getMessage(), e);
        return newResponse(e, HttpStatus.UNAUTHORIZED);
    }

    // ========== HTTP 415 오류 처리 ==========
    @ExceptionHandler({HttpMediaTypeException.class})
    private ResponseEntity<?> handleHttpMediaTypeException(Exception e) {
        log.debug("Bad request exception occurred: {}", e.getMessage(), e);
        return newResponse(e, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }
    @ExceptionHandler({MultipartException.class, MissingServletRequestPartException.class})
    private ResponseEntity<?> multipartException(Exception e) {
        log.debug("Bad request exception occurred: {}", e.getMessage(), e);
        return newResponse(e, HttpStatus.BAD_REQUEST);
    }

    // ========== HTTP 401 오류 처리 ==========
    @ExceptionHandler({MissingRequestHeaderException.class})
    private ResponseEntity<?> missingRequestHeaderException(Exception e) {
        log.debug("Bad request exception occurred: {}", e.getMessage(), e);
        return newResponse(e, HttpStatus.UNAUTHORIZED);
    }
    // ========== HTTP 401 오류 처리 ==========
    @ExceptionHandler({SignatureException.class,MalformedJwtException.class,SecurityException.class})
    private ResponseEntity<?> signatureAndMalformedJwtException(Exception e) {
        log.debug("Bad request exception occurred: {}", e.getMessage(), e);
        return newResponse(e, HttpStatus.UNAUTHORIZED);
    }

    // ========== HTTP 401 오류 처리 ==========
    //데이터 없을 때 디비에-1
    @ExceptionHandler({EmptyResultDataAccessException.class,NullPointerException.class})
    private ResponseEntity<?> emptyResultDataAccessException(Exception e) {
        log.debug("Bad request exception occurred: {}", e.getMessage(), e);
        return newResponse(e, HttpStatus.BAD_REQUEST);
    }
    // ========== HTTP 401 오류 처리 ==========
    //데이터 없을 때 디비에-2
    @ExceptionHandler({NotFoundException.class})
    private ResponseEntity<?> notFoundException(Exception e) {
        log.debug("Bad request exception occurred: {}", e.getMessage(), e);
        return newResponse(e, HttpStatus.UNAUTHORIZED);
    }
    // ========== HTTP 404 오류 처리 ==========

}
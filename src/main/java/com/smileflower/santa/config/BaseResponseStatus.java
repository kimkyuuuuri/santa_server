package com.smileflower.santa.config;

import lombok.Getter;

/**
 * 에러 코드 관리
 */
@Getter
public enum BaseResponseStatus {
    /**
     * 1000 : 요청 성공
     */
    SUCCESS(true, 1000, "요청에 성공하였습니다."),


    /**
     * 2000 : Request 오류
     */
    // Common
    REQUEST_ERROR(false, 2000, "입력값을 확인해주세요."),
    EMPTY_JWT(false, 2001, "JWT를 입력해주세요."),
    INVALID_JWT(false, 2002, "유효하지 않은 JWT입니다."),
    INVALID_USER_JWT(false,2003,"권한이 없는 유저의 접근입니다."),
    INVALID_USER(false,2004,"유저가 존재하지 않습니다"),
    // users
    USERS_EMPTY_USER_ID(false, 2010, "유저 아이디 값을 확인해주세요."),

    // [POST] /users
    POST_USERS_EMPTY(false,2014,"아직 입력하지 않은 필수항목이 있습니다."),
    POST_USERS_EMPTY_EMAIL(false, 2015, "이메일을 입력해주세요."),
    POST_USERS_INVALID_EMAIL(false, 2016, "이메일 형식을 확인해주세요."),
    POST_USERS_EXISTS_EMAIL(false,2017,"중복된 이메일입니다."),
    PASSWORD_CONFIRM_ERROR(false,2018,"두 비밀번호가 일치하지 않습니다."),
    POST_USER_EXISTS_NAME(false,2019,"사용 중인 별명입니다."),
    INSUFFICIENT_PW_RANGE(false,2020,"비밀번호를 8자 이상 입력해주세요."),
    EXCEED_PW_RANGE(false,2021,"비밀번호를 16자 이하로 입력해주세요."),
    INSUFFICIENT_NAME_RANGE(false,2022,"별명을 2~15자 내로 입력해주세요."),
    USERS_INVALID_ACCESS(false,2023,"본인만 접근 가능합니다."),
    NON_EXIST_MOUNTAIN(false,2024,"해당 산이 없습니다."),
    POST_AUTH_EMPTY_CODE(false, 2025, "인증코드를 입력해주세요."),
    POST_AUTH_EMPTY_EMAIL(false, 2026, "인증코드를 보낸 이메일이 없습니다."),
    POST_AUTH_EXISTS_EMAIL(false, 2027, "이미 회원가입이 완료된 이메일입니다."),
    INVALID_AUTH_EMAIL_CODE(false, 2028, "인증코드가 틀렸습니다."),
    SEND_MAIL_ERROR(false, 2029, "인증코드 전송에 실패하였습니다."),
    POST_USERS_EMPTY_USER(false, 2030, "산타에 가입되어있지 않습니다."),
    FAIL_LOCATION(false,2031,"현재 해당 산의 위치가 아닙니다."),





    /**
     * 3000 : Response 오류
     */
    // Common
    RESPONSE_ERROR(false, 3000, "값을 불러오는데 실패하였습니다."),

    // [POST] /users
    DUPLICATED_EMAIL(false, 3013, "중복된 이메일입니다."),
    POST_USERS_NONEXIST_ACCOUNT(false,3014,"아이디 또는 비밀번호가 잘못되었습니다."),
    ALREADY_LOGGED(false,3015,"이미 로그인되어 있습니다"),
    ALREADY_LOGOUT(false,3016,"이미 로그아웃되어 있습니다."),
    EMPTY_MOUNTAIN_INPUT(false,3017,"검색할 산 이름을 입력해주세요."),


    /**
     * 4000 : Database, Server 오류
     */
    DATABASE_ERROR(false, 4000, "데이터베이스 연결에 실패하였습니다."),
    SERVER_ERROR(false, 4001, "서버와의 연결에 실패하였습니다."),

    //[PATCH] /users/{userIdx}
    MODIFY_FAIL_USERNAME(false,4014,"유저네임 수정 실패"),

    PASSWORD_ENCRYPTION_ERROR(false, 4011, "비밀번호 암호화에 실패하였습니다."),
    PASSWORD_DECRYPTION_ERROR(false, 4012, "비밀번호 복호화에 실패하였습니다.");
    

    private final boolean isSuccess;
    private final int code;
    private final String message;

    private BaseResponseStatus(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}

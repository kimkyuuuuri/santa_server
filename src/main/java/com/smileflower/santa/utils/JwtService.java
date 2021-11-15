package com.smileflower.santa.utils;


import com.smileflower.santa.config.BaseException;
import com.smileflower.santa.config.secret.Secret;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

import static com.smileflower.santa.config.BaseResponseStatus.*;

@Service
public class JwtService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());
    /*
    JWT 생성
    @param userIdx
    @return String
     */
    public String createJwt(int userIdx){
        Date now = new Date();
        return Jwts.builder()
                .setHeaderParam("type","jwt")
                .claim("userIdx",userIdx)
                .setIssuedAt(now)
                .setExpiration(new Date(System.currentTimeMillis()+1*(1000*60*60*24*365)))
                .signWith(SignatureAlgorithm.HS256, Secret.JWT_SECRET_KEY)
                .compact();
    }

    /*
    Header에서 X-ACCESS-TOKEN 으로 JWT 추출
    @return String
     */
    public String getJwt(){
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
        return request.getHeader("X-ACCESS-TOKEN");
    }

    /*
    JWT에서 userIdx 추출
    @return int
    @throws BaseException
     */
    public int getUserIdx() throws BaseException{
        //1. JWT 추출
        String accessToken = getJwt();
        if(accessToken == null || accessToken.length() == 0){
            throw new BaseException(EMPTY_JWT);
        }

        // 2. JWT parsing
        Jws<Claims> claims;
        try{
            claims = Jwts.parser()
                    .setSigningKey(Secret.JWT_SECRET_KEY)
                    .parseClaimsJws(accessToken);
        } catch (Exception ignored) {
            throw new BaseException(INVALID_JWT);
        }

        // 3. userIdx 추출
        return claims.getBody().get("userIdx",Integer.class);
    }

    public boolean validateToken() throws SecurityException,MalformedJwtException,ExpiredJwtException,UnsupportedJwtException,IllegalArgumentException {
        //1. JWT 추출
        String accessToken = getJwt();
        try {
            Jwts.parser().setSigningKey(Secret.JWT_SECRET_KEY).parseClaimsJws(accessToken);
            return true;
        } catch (SecurityException e) {
            logger.info("잘못된 JWT 서명입니다.");
            throw new SecurityException(e.getMessage());
        } catch(MalformedJwtException e){
            logger.info("잘못된 JWT 서명입니다.");
            throw new MalformedJwtException(e.getMessage());
        } catch(ExpiredJwtException e) {
            logger.info("만료된 JWT 토큰입니다.");
            throw new ExpiredJwtException(e.getHeader(),e.getClaims(), e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.info("지원되지 않는 JWT 토큰입니다.");
            throw new UnsupportedJwtException(e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.info("JWT 토큰이 잘못되었습니다.");
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public int getUserIdxV2(){
        //1. JWT 추출
        String accessToken = getJwt();

        // 2. JWT parsing
        Jws<Claims> claims;
        try{
            // 3. userIdx 추출
            return Jwts.parser()
                    .setSigningKey(Secret.JWT_SECRET_KEY)
                    .parseClaimsJws(accessToken)
                    .getBody().get("userIdx",Integer.class);
        } catch (SignatureException | MalformedJwtException e){
            //토큰 서명 검증 or 구조 문제 (Invalid token)
        } catch(ExpiredJwtException e){
            //토큰이 만료됐기 때문에 클라이언트는 토큰을 refresh 해야함.
        } catch(Exception e){
        }
        return -1;
    }


}


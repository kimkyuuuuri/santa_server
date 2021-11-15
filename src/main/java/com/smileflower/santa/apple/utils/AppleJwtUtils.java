package com.smileflower.santa.apple.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smileflower.santa.apple.model.domain.Email;
import com.smileflower.santa.apple.model.dto.AppleAuthResponse;
import com.smileflower.santa.apple.model.dto.AppleLoginResponse;
import com.smileflower.santa.apple.model.dto.ApplePublicKeyResponse;
import com.smileflower.santa.apple.model.dto.AppleToken;
import feign.FeignException;
import io.jsonwebtoken.*;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

@Component
public class AppleJwtUtils {
    private final AppleClient appleClient;

    @Value("${APPLE.PUBLICKEY.URL}")
    private String APPLE_PUBLIC_KEYS_URL;

    @Value("${APPLE.ISS}")
    private String ISS;

    @Value("${APPLE.AUD}")
    private String AUD;

    @Value("${APPLE.CLIENT_ID}")
    private String CLIENT_ID;

    @Value("${APPLE.TEAM.ID}")
    private String TEAM_ID;

    @Value("${APPLE.KEY.ID}")
    private String KEY_ID;

    @Value("${APPLE.KEY.PATH}")
    private String KEY_PATH;

    @Value("${APPLE.AUTH.TOKEN.URL}")
    private String AUTH_TOKEN_URL;

    @Value("${APPLE.WEBSITE.URL}")
    private String APPLE_WEBSITE_URL;

    public AppleJwtUtils(AppleClient appleClient) {
        this.appleClient = appleClient;
    }

    public Claims getClaimsBy(String identityToken) {
        try {
            ApplePublicKeyResponse response = appleClient.getAppleAuthPublicKey();

            String headerOfIdentityToken = identityToken.substring(0, identityToken.indexOf("."));

            Map<String, String> header = null;
            try {
                header = new ObjectMapper().readValue(new String(Base64.getDecoder().decode(headerOfIdentityToken), "UTF-8"), Map.class);
            } catch (JsonProcessingException | UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            ApplePublicKeyResponse.Key key = response.getMatchedKeyBy(header.get("kid"), header.get("alg"))
                    .orElseThrow(() -> new NullPointerException("Failed get public key from apple's id server."));

            byte[] nBytes = Base64.getUrlDecoder().decode(key.getN());
            byte[] eBytes = Base64.getUrlDecoder().decode(key.getE());

            BigInteger n = new BigInteger(1, nBytes);
            BigInteger e = new BigInteger(1, eBytes);

            RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(n, e);
            KeyFactory keyFactory = KeyFactory.getInstance(key.getKty());
            PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

            return Jwts.parser().setSigningKey(publicKey).parseClaimsJws(identityToken).getBody();
        } catch (NoSuchAlgorithmException e) {
        } catch (InvalidKeySpecException e) {
        } catch (SignatureException | MalformedJwtException e){
            //토큰 서명 검증 or 구조 문제 (Invalid token)
        } catch(ExpiredJwtException e){
            //토큰이 만료됐기 때문에 클라이언트는 토큰을 refresh 해야함.
        } catch(Exception e){
        }
        return null;
    }

    public Email getEmail(String identityToken){
        return new Email((String)getClaimsBy(identityToken).get("email"));
    }

    public Email getEmailByRefreshToken(String refreshToken){
        Email email = null;
        try {
            email =  new Email((String)getClaimsBy(getTokenByRefreshToken(makeClientSecret(), refreshToken).getId_token()).get("email"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return email;
    }

    public String makeClientSecret() throws IOException {
        Date expirationDate = Date.from(LocalDateTime.now().plusDays(30).atZone(ZoneId.systemDefault()).toInstant());
        
        return Jwts.builder()
                .setHeaderParam("kid", KEY_ID)
                .setHeaderParam("alg", "ES256")
                .setIssuer(TEAM_ID)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(expirationDate)
                .setAudience("https://appleid.apple.com")
                .setSubject(CLIENT_ID)
                .signWith(SignatureAlgorithm.ES256, getPrivateKey())
                .compact();
    }

    private PrivateKey getPrivateKey() throws IOException {
        ClassPathResource resource = new ClassPathResource(KEY_PATH);
        String privateKey = new String(Files.readAllBytes(Paths.get(resource.getURI())));
        Reader pemReader = new StringReader(privateKey);
        PEMParser pemParser = new PEMParser(pemReader);
        JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
        PrivateKeyInfo object = (PrivateKeyInfo) pemParser.readObject();
        return converter.getPrivateKey(object);
    }

    public AppleToken.Response getTokenByCode(String client_secret, String code) throws IOException {
        AppleToken.Request request = AppleToken.Request.of(code,CLIENT_ID, client_secret,"authorization_code",null);

        AppleToken.Response response = appleClient.getToken(request);
        return response;
    }
    public AppleToken.Response getTokenByRefreshToken(String client_secret, String refresh_token) throws IOException {
        AppleToken.Request request = AppleToken.Request.of(null,CLIENT_ID,client_secret,"refresh_token",refresh_token);
        AppleToken.Response response = appleClient.getToken(request);
        return response;
    }
}
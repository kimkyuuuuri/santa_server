package com.smileflower.santa.src.social_login.utils;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.ReadOnlyJWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.smileflower.santa.src.social_login.model.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.*;
import org.apache.http.client.utils.HttpClientUtils;
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
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class NewAppleJwtUtils {
    private final NewAppleClient newAppleClient;

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

    public NewAppleJwtUtils(NewAppleClient newAppleClient) {
        this.newAppleClient = newAppleClient;
    }

    public Claims getClaimsBy(String identityToken) {
        try {
            ApplePublicKeyRes response = newAppleClient.getAppleAuthPublicKey();

            String headerOfIdentityToken = identityToken.substring(0, identityToken.indexOf("."));

            Map<String, String> header = null;
            try {
                header = new ObjectMapper().readValue(new String(Base64.getDecoder().decode(headerOfIdentityToken), "UTF-8"), Map.class);
            } catch (JsonProcessingException | UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            ApplePublicKeyRes.Key key = response.getMatchedKeyBy(header.get("kid"), header.get("alg"))
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
            //?????? ?????? ?????? or ?????? ?????? (Invalid token)
        } catch(ExpiredJwtException e){
            //????????? ???????????? ????????? ?????????????????? ????????? refresh ?????????.
        } catch(Exception e){
        }
        return null;
    }

    public Email getEmail(String identityToken){

        return new Email((String)getClaimsBy(identityToken).get("email"));
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

}


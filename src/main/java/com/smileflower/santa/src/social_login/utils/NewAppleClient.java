package com.smileflower.santa.src.social_login.utils;


import com.smileflower.santa.config.FeignConfig;
import com.smileflower.santa.src.social_login.model.*;
import feign.auth.BasicAuthRequestInterceptor;
import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "appleClient", url = "https://appleid.apple.com/auth", configuration = FeignConfig.class)
public interface NewAppleClient {
    @GetMapping(value = "/keys")
    ApplePublicKeyRes getAppleAuthPublicKey();
    @PostMapping(value = "/token", consumes = "application/x-www-form-urlencoded", produces = "application/json")
    AppleToken.Response getToken(@RequestBody AppleToken.Request request);

    class Configuration {
        @Bean
        public BasicAuthRequestInterceptor basicAuthRequestInterceptor(
                @Value("${scrooge-mcduck.authentication.username}") String username,
                @Value("${scrooge-mcduck.authentication.password}") String password) {
            return new BasicAuthRequestInterceptor(username, password);
        }

        @Bean
        Encoder feignFormEncoder(ObjectFactory<HttpMessageConverters> converters) {
            return new SpringFormEncoder(new SpringEncoder(converters));
        }
    }
}
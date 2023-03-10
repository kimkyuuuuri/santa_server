package com.smileflower.santa.src.email.model;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor(access=AccessLevel.PUBLIC)
@AllArgsConstructor
public class PostAuthReq {
    private String email;
    private Integer code;
}

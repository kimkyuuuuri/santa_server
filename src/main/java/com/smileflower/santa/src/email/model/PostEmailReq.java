
package com.smileflower.santa.src.email.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor (access = AccessLevel.PUBLIC)
public class PostEmailReq {
    private String email;
}
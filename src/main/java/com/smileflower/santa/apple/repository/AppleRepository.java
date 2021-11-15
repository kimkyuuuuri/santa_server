package com.smileflower.santa.apple.repository;

import com.smileflower.santa.apple.model.domain.AppleUser;
import com.smileflower.santa.apple.model.domain.Email;

public interface AppleRepository {
    AppleUser save(AppleUser user);

    boolean findByEmail(Email email);
    boolean findByIdx(Long userIdx);

    String setStatus(String status);
    String getStatusByToken(String refreshToken);
}

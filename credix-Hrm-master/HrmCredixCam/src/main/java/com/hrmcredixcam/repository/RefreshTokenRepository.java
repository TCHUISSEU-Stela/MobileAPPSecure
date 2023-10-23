package com.hrmcredixcam.repository;

import com.hrmcredixcam.model.RefreshToken;
import org.springframework.data.mongodb.repository.MongoRepository;


import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository extends MongoRepository<RefreshToken, String> {

    Optional<RefreshToken> findByRefreshTokenId(UUID refreshTokenId);

}

package com.hrmcredixcam.service;

import com.hrmcredixcam.model.RefreshToken;

import java.util.UUID;

public interface RefreshTokenService {

    RefreshToken saveRefreshToken(RefreshToken refreshToken);

    RefreshToken findByRefreshTokenId(UUID value);
}

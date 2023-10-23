package com.hrmcredixcam.service.impl;

import com.hrmcredixcam.exception.TokenRefreshException;
import com.hrmcredixcam.model.RefreshToken;
import com.hrmcredixcam.repository.RefreshTokenRepository;
import com.hrmcredixcam.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public RefreshToken saveRefreshToken(RefreshToken refreshToken){
        return    refreshTokenRepository.save(refreshToken);
    }


    @Override
    public RefreshToken findByRefreshTokenId(UUID value){
        return  refreshTokenRepository.findByRefreshTokenId(value)
                .orElseThrow(()->new TokenRefreshException(value,"Does Not Exist:"));

    }



}

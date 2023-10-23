package com.hrmcredixcam.service.impl;

import com.hrmcredixcam.model.User;
import com.hrmcredixcam.model.PasswordResetToken;
import com.hrmcredixcam.repository.PasswordResetTokenRepository;
import com.hrmcredixcam.service.PasswordResetTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class PasswordResetTokenServiceImpl implements PasswordResetTokenService {

    private final PasswordResetTokenRepository tokenRepository;

    @Override
    public PasswordResetToken createToken(User employee, String token, Date expirationDate) {
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setEmployee(employee);
        resetToken.setToken(token);
        resetToken.setExpirationDate(expirationDate);
        return tokenRepository.save(resetToken);
    }

    @Override
    public PasswordResetToken findByToken(String token) {
        return tokenRepository.findByToken(token);
    }

    @Override
    public void deleteToken(PasswordResetToken token) {
        tokenRepository.delete(token);
    }
}

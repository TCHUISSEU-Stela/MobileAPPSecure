package com.hrmcredixcam.service;

import com.hrmcredixcam.model.User;
import com.hrmcredixcam.model.PasswordResetToken;

import java.util.Date;

public interface PasswordResetTokenService {

    PasswordResetToken createToken(User employee, String token, Date expirationDate);

    PasswordResetToken findByToken(String token);

    void deleteToken(PasswordResetToken token);
}

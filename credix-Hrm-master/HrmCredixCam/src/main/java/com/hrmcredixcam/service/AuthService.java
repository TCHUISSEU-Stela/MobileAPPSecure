package com.hrmcredixcam.service;

import com.hrmcredixcam.authdtos.*;

public interface AuthService {


    UserInfoResponseDTO login(LoginRequestDTO loginDto);

    String registerByAdmin(SignupRequestDTO registerDto);


    String registerByUser(SignupUserRequestDTO registerDto);

    UserInfoResponseDTO refreshToken(RefreshTokenDTO refreshTokendto);

}

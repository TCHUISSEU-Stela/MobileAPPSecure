package com.hrmcredixcam.authdtos;

import lombok.Data;

import java.util.UUID;

@Data
public class RefreshTokenDTO {
    private UUID token;
}

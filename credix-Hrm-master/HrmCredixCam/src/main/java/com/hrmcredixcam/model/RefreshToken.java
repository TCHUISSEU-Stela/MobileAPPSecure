package com.hrmcredixcam.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;
@Data
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document("RefreshToken")
public class RefreshToken {

        @Id
        private String id;
        @Indexed
        private UUID refreshTokenId;
        private String refreshToken;
        private String token;
        private String user;
        private Date expiryDate;
        private LocalDateTime dateTime;



}

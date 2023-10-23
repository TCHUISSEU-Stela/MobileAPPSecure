package com.hrmcredixcam.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "passwordResetTokens")
public class PasswordResetToken {
    @Id
    private String id;
    private String token;
    private Date expirationDate;
    private User employee;

    public boolean isExpired() {
        return new Date().after(this.expirationDate);
    }

}

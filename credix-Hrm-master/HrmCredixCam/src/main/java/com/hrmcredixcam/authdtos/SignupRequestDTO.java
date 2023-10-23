package com.hrmcredixcam.authdtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequestDTO {
    private String userName;
    private String firstName;
    private String lastName;
    private String password;
    private String telephone;
    private String email;
    private Set<String> role;
}

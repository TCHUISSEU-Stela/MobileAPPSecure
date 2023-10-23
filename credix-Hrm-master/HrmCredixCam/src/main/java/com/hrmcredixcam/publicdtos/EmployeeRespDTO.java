package com.hrmcredixcam.publicdtos;

import com.hrmcredixcam.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeRespDTO {
    private String id;

    private String userName;

    private String firstName;

    private String lastName;

    private String telephone;

    private String email;

    private LocalDateTime creationDate;

    private boolean isActive=true;

    private Set<String> roles;



}

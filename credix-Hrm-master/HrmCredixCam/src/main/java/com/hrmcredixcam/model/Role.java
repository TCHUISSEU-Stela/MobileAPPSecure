package com.hrmcredixcam.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document("Role")
public class Role {
    @Id
    private String id;
    @Indexed
    private String role;
    private String roleDescription;

}

package com.hrmcredixcam.publicdtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UpdateEmployeeDTO {

    private String userName;

    private String firstName;

    private String lastName;

    private String telephone;

    private String email;

}

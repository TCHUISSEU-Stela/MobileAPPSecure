package com.hrmcredixcam.utils;

import com.hrmcredixcam.publicdtos.MetaDTO;
import com.hrmcredixcam.publicdtos.ResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EntityResponseUtils {

    public ResponseDTO ErrorResponse(String statusDescription){
        return  ResponseDTO.builder()
                .meta(MetaDTO.builder()
                        .message("unsuccessful")
                        .statusCode(400)
                        .statusDescription(statusDescription).build()
                )
                .data(null)
                .error(statusDescription)
                .build();
    }

    public ResponseDTO ErrorResponse2(String statusDescription, String error){
        return  ResponseDTO.builder()
                .meta(MetaDTO.builder()
                        .message("unsuccessful")
                        .statusCode(400)
                        .statusDescription(statusDescription).build()
                )
                .data(null)
                .error(error)
                .build();
    }


    public ResponseDTO SuccessFullResponse(String statusDescription, Object data, int count){
        return  ResponseDTO.builder()
                .meta(MetaDTO.builder()
                        .message("successful")
                        .statusCode(200)
                        .statusDescription("successful").build()
                )
                .data(data)
                .error(null)
                .build();
    }

}

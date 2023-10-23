package com.hrmcredixcam.utils;

import com.hrmcredixcam.publicdtos.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static java.time.LocalDateTime.now;


public class UserUtils {

    private UserUtils(){

    }


    public static ResponseEntity<Response> getResponseEntityG(String responseMessage, HttpStatus httpStatus){

        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .message(responseMessage)
                        .status(httpStatus)
                        .statusCode(httpStatus.value())
                        .build()
        );
    }








//    public static ResponseEntity<Response> getResponseEntitySub(String responseMessage, HttpStatus httpStatus){
////        return new ResponseEntity<String>(
////                "{\"message\":\"" + responseMessage + "\",\n\"code\":\"" + httpStatus.value() + "\",\n\"body\":\"" + object + "\"}",
////                httpStatus
////        );
//        return ResponseEntity.ok(
//                Response.builder()
//                        .timeStamp(now())
//                        .message(responseMessage)
//                        .status(httpStatus)
//                        .statusCode(httpStatus.value())
//                        .build()
//        );
//    }
}

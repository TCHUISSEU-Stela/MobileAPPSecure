package com.hrmcredixcam.publicdtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class Response {

    public LocalDateTime timeStamp;
    public int statusCode;
    public HttpStatus status;
    public String message;
    public Map<?, ?> data;
}



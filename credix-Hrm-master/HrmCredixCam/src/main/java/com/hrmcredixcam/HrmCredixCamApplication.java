package com.hrmcredixcam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
public class HrmCredixCamApplication {

    public static void main(String[] args) {
        SpringApplication.run(HrmCredixCamApplication.class, args);
    }

}

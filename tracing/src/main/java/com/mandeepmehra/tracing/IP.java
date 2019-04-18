package com.mandeepmehra.tracing;

import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class IP {

    @Bean
    RestTemplate getRestTemnplate(){
        return new RestTemplate();
    }


    @GetMapping(value = "/ip")
    public String getIP(){


        RestTemplate restTemplate = getRestTemnplate();

        return null;
    }
}

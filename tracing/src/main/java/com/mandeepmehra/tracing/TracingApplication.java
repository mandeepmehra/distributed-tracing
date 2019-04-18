package com.mandeepmehra.tracing;

import brave.sampler.Sampler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


@SpringBootApplication
@RestController
public class TracingApplication {

	Logger logger = LoggerFactory.getLogger(TracingApplication.class);
	public static void main(String[] args) {
		SpringApplication.run(TracingApplication.class, args);
	}

	final String HTTPBIN_IP_URL = "https://httpbin.org/ip";
	final String HTTPBIN_HEADERS_URL = "https://httpbin.org/user-agent";
	final String HEADERS_URL = "http://localhost:8080/headers";

	@Bean
	Sampler getSampler() {
		return Sampler.ALWAYS_SAMPLE;
	}

	@Bean
	RestTemplate getRestTemnplate(){
		return new RestTemplate();
	}

	@GetMapping(value = "/ip")
	public String getIP(){
		logger.info("Feching IP..");
		RestTemplate restTemplate = getRestTemnplate();
		ResponseEntity<String> response = restTemplate.getForEntity(HTTPBIN_IP_URL, String.class);

		response = restTemplate.getForEntity(HEADERS_URL, String.class);

		logger.info("Feching Headers from header service..");

		return response.getBody();
	}

	@GetMapping(value = "/headers")
	public String getHeaders() throws InterruptedException {
		logger.info("Fetching Headers from HTTPBIN..");
		RestTemplate restTemplate = getRestTemnplate();
		ResponseEntity<String> response = restTemplate.getForEntity(HTTPBIN_HEADERS_URL, String.class);
		Thread.sleep(3000);
		return response.getBody();
	}
}

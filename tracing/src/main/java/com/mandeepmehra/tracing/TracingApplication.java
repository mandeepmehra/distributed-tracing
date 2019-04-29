package com.mandeepmehra.tracing;

import brave.sampler.Sampler;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@SpringBootApplication
@RestController
public class TracingApplication {

	Logger logger = LoggerFactory.getLogger(TracingApplication.class);
	int i=10;
	public static void main(String[] args) {


		List<String> symbols1= Arrays.asList("GOOG","AMZN","ITC");

//		Observable observable = Observable.fromArray(symbols);
//		Disposable disposable =  observable.subscribe(t -> System.out.println(t),e-> System.err.println(e) ) ;
//
//		//SpringApplication.run(TracingApplication.class, args);
		TracingApplication application = new TracingApplication();
		application.learnReactive();;

	}


	void learnReactive(){
		String[] symbols = {"GOOG","AMZN","ITC"};
		i=20;
		Observable<String> observable1 = Observable.fromArray(symbols);
		observable1.subscribe( new TracingApplication()::doSomething , Throwable::printStackTrace, () -> {
			System.out.println("Done");
		});
	}


	public  void doSomething(String  s ){
		System.out.println(s);
		System.out.println(i);
	}


	final String HTTPBIN_IP_URL = "https://httpbin.org/ip";
	final String HTTPBIN_HEADERS_URL = "https://httpbin.org/user-agent";
	final String HEADERS_URL = "http://localhost:8080/headers";

	ExecutorService cpuBound = Executors.newFixedThreadPool(4);
	ExecutorService ioBound = Executors.newCachedThreadPool();

	@Bean
	Sampler getSampler() {
		return Sampler.ALWAYS_SAMPLE;
	}

	@Bean
	RestTemplate getRestTemnplate(){
		return new RestTemplate();
	}


	@GetMapping(value = "/wait")
	public void waitFor30Minutes() throws InterruptedException {
		logger.info("Request received");
		Thread.sleep(2000);

	}


	@PostMapping(value = "/book")
	public void bookFlight(){
		CompletableFuture.supplyAsync(() -> getFlightFromCache(), cpuBound)
				.thenApplyAsync(itenerary -> createBooking(itenerary), ioBound)
				.exceptionally(e -> handleFailedBoking())
				.thenApplyAsync(itenerary -> createPNRs(itenerary),ioBound)
				.thenAcceptAsync(itenerary -> updateBookingStatus(itenerary),ioBound);
		logger.info("Booking done");
	}

	private Object handleFailedBoking() {
		return null;
	}

	private Object updateBookingStatus(Object itenerary) {
		logger.info("Updating PNR numbers");
		try {
			Thread.sleep(6000);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return null;
	}

	private Object createBooking(Object itenerary) {
		logger.info("Creating booking in DB");
		return null;
	}

	private Object createPNRs(Object itenerary) {
		logger.info("Creating PNRs for the flights in itenarary ");
		return null;
	}



	private <Itenerary> Itenerary getFlightFromCache() {
		Itenerary itenerary =null;
		return itenerary;
	}

	@GetMapping(value = "/ip")
	public String getIP() {
		logger.info("Feching IP..");

		RestTemplate restTemplate = getRestTemnplate();
		ResponseEntity<String> response = restTemplate.getForEntity(HTTPBIN_IP_URL, String.class);
		try {
			response = restTemplate.getForEntity(HEADERS_URL, String.class);
		} catch (RuntimeException e) {
			logger.error("Error while executing {}", HEADERS_URL);
		}

		logger.info("Feching Headers from header service..");

		return response.getBody();
	}

	@GetMapping(value = "/headers")
	public String getHeaders() throws InterruptedException {
		logger.info("Fetching Headers from HTTPBIN..");
		RestTemplate restTemplate = getRestTemnplate();
		ResponseEntity<String> response = restTemplate.getForEntity(HTTPBIN_HEADERS_URL, String.class);
		Thread.sleep(3000);
		if (response!=null)
			throw new RuntimeException("Could not fetch header");
		return response.getBody();
	}
}

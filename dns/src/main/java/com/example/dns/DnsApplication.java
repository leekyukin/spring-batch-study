package com.example.dns;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class DnsApplication {

	@RestController
	class HelloController {

		@GetMapping("/hello")
		public String hello() {
			return "long time no see";
		}
	}

	public static void main(String[] args) {
		SpringApplication.run(DnsApplication.class, args);
	}

}

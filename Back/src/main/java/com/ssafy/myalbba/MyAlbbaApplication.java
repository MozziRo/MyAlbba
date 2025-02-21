package com.ssafy.myalbba;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class MyAlbbaApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyAlbbaApplication.class, args);
	}

}

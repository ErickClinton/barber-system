package br.com.barber.jhow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BarberJhowApplication {

	public static void main(String[] args) {
		SpringApplication.run(BarberJhowApplication.class, args);
	}

}

package br.com.boletos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class BoletosApplication {

	public static void main(String[] args) {
		SpringApplication.run(BoletosApplication.class, args);
	}

}

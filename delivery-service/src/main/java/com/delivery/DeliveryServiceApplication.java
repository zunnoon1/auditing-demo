package com.delivery;

import com.core.CoreAppApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@EnableKafka
@SpringBootApplication
public class DeliveryServiceApplication extends CoreAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(DeliveryServiceApplication.class, args);
	}

}

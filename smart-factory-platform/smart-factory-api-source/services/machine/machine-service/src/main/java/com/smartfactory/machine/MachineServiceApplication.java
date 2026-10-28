package com.smartfactory.machine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MachineServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MachineServiceApplication.class, args);
	}

}

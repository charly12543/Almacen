package com.charlyCorporation.productos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;


@SpringBootApplication(scanBasePackages = "com.charlyCorporation")
@EnableDiscoveryClient
@EnableFeignClients
public class ProductosApplication {

	public static void main(String[] args) {

		SpringApplication.run(ProductosApplication.class, args);
	}

}

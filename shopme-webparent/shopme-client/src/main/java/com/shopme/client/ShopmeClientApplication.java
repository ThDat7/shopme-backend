package com.shopme.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan({"com.shopme.common.entity"})
public class ShopmeClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShopmeClientApplication.class, args);
	}

}

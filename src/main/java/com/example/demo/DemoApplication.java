package com.example.demo;

import com.example.demo.data.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	public CommandLineRunner dataLoader(CustomerRepository customerRepo) {
		return args -> {
			Random random = new Random();
			for (String name : List.of("James", "Robert", "John", "Michael", "David", "William")) {
				Customer customer = new Customer();
				customer.setAccountName(name);
				List<Purchase> purchases = new ArrayList<>();
				int numOfPurchases = random.nextInt(200) + 1;
				for (int i = 0; i < numOfPurchases; i++) {
					Purchase purchase = new Purchase();
					purchase.setAmount(random.nextLong(200));
					Date purchaseAt = new Date();
					purchaseAt.setTime(purchaseAt.getTime() - random.nextLong(6L * 30 * 24 * 60 * 60 * 1000));
					purchase.setCreatedAt(purchaseAt);
					purchases.add(purchase);
				}
				customer.setPurchases(purchases);
				customerRepo.save(customer);
			}

		};
	}

}

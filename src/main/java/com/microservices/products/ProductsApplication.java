package com.microservices.products;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class ProductsApplication {

	public static void main(String[] args) {
		System.out.println("=== STARTING APPLICATION ===");
		ConfigurableApplicationContext context = SpringApplication.run(ProductsApplication.class, args);

		// Debug: Listar todos los beans que contienen "controller"
		String[] beanNames = context.getBeanDefinitionNames();
		System.out.println("=== BEANS QUE CONTIENE EL 'controller' ===");
		for (String beanName : beanNames) {
			if (beanName.toLowerCase().contains("controller")) {
				System.out.println("Bean controller encontrados: " + beanName);
			}
		}
	}
}
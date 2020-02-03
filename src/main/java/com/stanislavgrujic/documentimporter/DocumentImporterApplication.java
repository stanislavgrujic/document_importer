package com.stanislavgrujic.documentimporter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class DocumentImporterApplication {

	public static void main(String[] args) {
		SpringApplication.run(DocumentImporterApplication.class, args);
	}

}

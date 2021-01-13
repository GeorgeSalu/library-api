package com.library;

import java.util.Arrays;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.library.api.service.EmailService;

@SpringBootApplication
@EnableScheduling
public class LibraryApplication {
	
	/*
	@Autowired
	private EmailService emailService;
	*/

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
	
	/*
	@Bean
	public CommandLineRunner runner() {
		return args -> {
			List<String> emails = Arrays.asList("9c454516d6-05d2e2@inbox.mailtrap.io");
			emailService.sendEmails("testando servico de emails", emails);
			System.out.println("emails envidos");
		};
	}
	*/
	
	public static void main(String[] args) {
		SpringApplication.run(LibraryApplication.class, args);
	}

}

package com.felipeveiga.config;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.felipeveiga.services.DBService;

@Configuration
@Profile("dev")
public class DevConfig {

	@Autowired
	private DBService service;
	
	@Bean
	public boolean instantiateDataBase() throws ParseException {
		service.instantiateTestDataBase();
		return true;
	}
	
}

package com.fintech.financialservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@EnableCaching
class FinancialServiceApplicationTests {

	@Autowired
	private ApplicationContext context;

	@Autowired
	private RestTemplate restTemplate;

	@Test
	void contextLoads() {
		assertNotNull(context, "The application context should be loaded");
	}

	@Test
	void restTemplateBeanIsAvailable() {
		assertNotNull(restTemplate, "RestTemplate bean should be available");
	}
}

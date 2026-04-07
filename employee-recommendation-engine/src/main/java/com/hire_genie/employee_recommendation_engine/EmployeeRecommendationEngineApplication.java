package com.hire_genie.employee_recommendation_engine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class EmployeeRecommendationEngineApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmployeeRecommendationEngineApplication.class, args);
	}

}

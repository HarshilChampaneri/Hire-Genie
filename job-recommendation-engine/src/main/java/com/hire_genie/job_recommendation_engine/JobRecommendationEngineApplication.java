package com.hire_genie.job_recommendation_engine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class JobRecommendationEngineApplication {

	public static void main(String[] args) {
		SpringApplication.run(JobRecommendationEngineApplication.class, args);
	}

}

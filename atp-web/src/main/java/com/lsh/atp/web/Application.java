package com.lsh.atp.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.support.SpringBootServletInitializer;


@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, ElasticsearchAutoConfiguration.class})
public class Application extends SpringBootServletInitializer {
	

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}

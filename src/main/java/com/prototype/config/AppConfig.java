package com.prototype.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.prototype.config.AppConfig.ApplicationConfiguration;

import lombok.Data;

@Configuration
@ComponentScan("com.prototype")
@EnableAutoConfiguration
@EnableConfigurationProperties(ApplicationConfiguration.class)
@EnableAspectJAutoProxy
public class AppConfig {

	@Data
	@ConfigurationProperties("application")
	public static class ApplicationConfiguration {
		String host;
		String context;
		String key;
	}

	@Bean
	public ObjectMapper objectMapper() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		return objectMapper;
	}
}

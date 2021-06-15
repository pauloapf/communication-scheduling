package com.desafio.scheduling.communication.dao.mock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
@EnableAspectJAutoProxy
@ConditionalOnProperty(name = "database.communicationScheduling.find.h2.enabled", havingValue = "true")
public class DatabaseMockAutoConfiguration {

	@Autowired
    private JdbcTemplate jdbcTemplate;
	
	@Bean
	public DatabaseMock databaseMock() {
		DatabaseMock dmock = new DatabaseMock();
		dmock.setJdbcTemplate(jdbcTemplate);
		dmock.setupDatabase();
		return dmock;
	}	
}

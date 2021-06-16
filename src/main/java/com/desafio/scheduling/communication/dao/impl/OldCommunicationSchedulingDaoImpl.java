package com.desafio.scheduling.communication.dao.impl;

import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import com.desafio.scheduling.communication.dao.domain.CommunicationSchedulingOld;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class OldCommunicationSchedulingDaoImpl{

	private JdbcTemplate jdbcTemplate;
	
	private SimpleJdbcInsert simpleJdbcInsert;

	private String find;
	
	private String insert;
	
	private SimpleDateFormat dateFormat;
	
	public OldCommunicationSchedulingDaoImpl(
			@Autowired JdbcTemplate jdbcTemplate,
			@Value("${database.communicationScheduling.find: SELECT ID, CREATION_DATE,SCHEDULE_DATE, PHONE_NUMBER, EMAIL, SEND_TYPE, MESSAGE, STATUS,STATUS_DESCRIPTION FROM COMMUNICATION_SCHEDULING WHERE ID = ?}") String find,
			@Value("${database.communicationScheduling.insert: INSERT INTO COMMUNICATION_SCHEDULING "
					+ " (CREATION_DATE,SCHEDULE_DATE, PHONE_NUMBER, EMAIL, SEND_TYPE, MESSAGE, STATUS,STATUS_DESCRIPTION) "
					+ " VALUES "
					+ " (?,?,?,?,?,?,?)}") String insert,
			@Value("${database.communicationScheduling.dateFormat: yyyy-MM-dd'T'HH}") String dateFormat) {
		this.find = find;
		this.insert = insert;
		this.jdbcTemplate = jdbcTemplate;
		this.dateFormat = new SimpleDateFormat(dateFormat);
		this.dateFormat.setLenient(false);
		simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
	}

	public CommunicationSchedulingOld insert(CommunicationSchedulingOld communicationScheduling) {
		jdbcTemplate.execute("SELECT 1 FROM PUBLIC.COMMUNICATION_SCHEDULING");
		
		simpleJdbcInsert.withTableName("COMMUNICATION_SCHEDULING").usingGeneratedKeyColumns("ID").compile();
		SqlParameterSource params = new MapSqlParameterSource()
			    .addValue("CREATION_DATE", communicationScheduling.getCreationDate())
			    .addValue("SCHEDULE_DATE", communicationScheduling.getScheduleDate())
			    .addValue("PHONE_NUMBER", communicationScheduling.getPhoneNumber())
			    .addValue("EMAIL", communicationScheduling.getEmail())
			    .addValue("SEND_TYPE", communicationScheduling.getSendType())
			    .addValue("MESSAGE", communicationScheduling.getMessage())
			    .addValue("STATUS", communicationScheduling.getStatus())
			    .addValue("STATUS_DESCRIPTION", communicationScheduling.getStatusDescription());
		Number id = simpleJdbcInsert.executeAndReturnKey(params);   		
		communicationScheduling.setId(id.longValue());
		
		return communicationScheduling;
	}
	
}

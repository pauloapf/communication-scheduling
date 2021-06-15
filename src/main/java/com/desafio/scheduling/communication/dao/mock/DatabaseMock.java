package com.desafio.scheduling.communication.dao.mock;

import java.util.Arrays;

import org.springframework.jdbc.core.JdbcTemplate;

import lombok.Data;

@Data
public class DatabaseMock {
    private JdbcTemplate jdbcTemplate;

    public void setupDatabase() {
        String[] sqlStatements = { 
                // Create table
                "CREATE TABLE COMMUNICATION_SCHEDULING ("
                + "ID INT NOT NULL AUTO_INCREMENT,"
                + "CREATION_DATE NOT NULL TIMESTAMP,"
                + "SCHEDULE_DATE NOT NULL TIMESTAMP,"
                + "PHONE_NUMBER varchar2(20),"
                + "EMAIL varchar2(100),"
                + "SEND_TYPE NOT NULL INT,"
                + "MESSAGE NOT NULL varchar2(100),"
                + "STATUS INT,"
                + "STATUS_DESCRIPTION varchar2(100)"
                + " CONSTRAINT PK_CI PRIMARY KEY (ID))"};

        Arrays.asList(sqlStatements).stream().forEach(sql -> jdbcTemplate.execute(sql));

    }

}

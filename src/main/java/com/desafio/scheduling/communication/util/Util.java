package com.desafio.scheduling.communication.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.desafio.scheduling.communication.api.response.ResponseCodeValues;
import com.desafio.scheduling.communication.exception.BusinessException;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class Util {

	private SimpleDateFormat dateFormat;
	
	public Util(
			@Value("${format.date: yyyy-MM-dd'T'HH}") String dateFormat) {
		this.dateFormat = new SimpleDateFormat(dateFormat);
		this.dateFormat.setLenient(false);
	}

	public LocalDateTime apiFormatToLocalDateTime(@NotNull String scheduleDate) throws ParseException{
		return dateFormat.parse(scheduleDate).toInstant()
			      .atZone(ZoneId.systemDefault())
			      .toLocalDateTime();
	}

	public String localDateTimeToApi(LocalDateTime scheduleDate) {
		return dateFormat.format(java.util.Date.from(scheduleDate
				.atZone(ZoneId.systemDefault())
			      .toInstant()));
	}

}

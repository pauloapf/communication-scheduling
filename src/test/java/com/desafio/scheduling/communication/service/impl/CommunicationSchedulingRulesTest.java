package com.desafio.scheduling.communication.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpStatus;

import com.desafio.scheduling.communication.api.response.ResponseCodeValues;
import com.desafio.scheduling.communication.business.CommunicationSchedulingRules;
import com.desafio.scheduling.communication.exception.BusinessException;
import com.desafio.scheduling.communication.model.SendTypeEnum;
import com.desafio.scheduling.communication.util.Util;

@TestInstance(Lifecycle.PER_CLASS)
public class CommunicationSchedulingRulesTest {

	private CommunicationSchedulingRules communicationSchedulingRules;
	private Util communicationSchedulingUtil;

	@BeforeAll
	public void setup() {
		communicationSchedulingUtil = new Util("yyyy-MM-dd'T'HH");
		communicationSchedulingRules = new CommunicationSchedulingRules(communicationSchedulingUtil);
	}
	
	@DisplayName("Recupera uma data no formato valido e acima da data corrente")
	@Test
	public void getValidScheduleDate() {
		LocalDate now = LocalDate.now();
		LocalDateTime scheduleDateTime = now.plusDays(1).atStartOfDay();
		String scheduleDate = communicationSchedulingUtil.localDateTimeToApi(scheduleDateTime);
		assertEquals(scheduleDateTime, communicationSchedulingRules.getValidScheduleDate(scheduleDate, now));
	}
	
	@DisplayName("Captura erro quando schedule date no mesmo dia que a data corrente")
	@Test
	public void validScheduleDateButEqualsCurrentDate() {
		LocalDate now = LocalDate.now();
		LocalDateTime scheduleDateTime = now.atTime(23, 59);
		String scheduleDate = communicationSchedulingUtil.localDateTimeToApi(scheduleDateTime);
		BusinessException be = assertThrows(BusinessException.class,()->communicationSchedulingRules.getValidScheduleDate(scheduleDate, now));
		assertEquals(ResponseCodeValues.SCHEDULE_DATE_NOT_GREATHER_THAN_CURRENT_DATE, be.getInternalCode());
		assertEquals(HttpStatus.BAD_REQUEST, be.getStatus());
	}	
	
	@DisplayName("Captura erro quando schedule date fora do formato")
	@Test
	public void invalidScheduleDateFormat() {
		LocalDate now = LocalDate.now();
		String scheduleDate = "2021-06-16'T'";
		BusinessException be = assertThrows(BusinessException.class,()->communicationSchedulingRules.getValidScheduleDate(scheduleDate, now));
		assertEquals(ResponseCodeValues.INVALID_SCHEDULE_DATE_FORMAT, be.getInternalCode());
		assertEquals(HttpStatus.BAD_REQUEST, be.getStatus());
	}	
	
	@DisplayName("Valida que se send type email e email enviado entao request valid")
	@Test
	public void validSendTypeEmail() {
		
		String phoneNumber = null;
		String email = "xx@xx.com.br";
		SendTypeEnum sendType = SendTypeEnum.EMAIL;
		communicationSchedulingRules.validateRecipientForSendType(phoneNumber, email, sendType.toString());
	}
	
	@ParameterizedTest(name="Validando sendType {0}")
	@ValueSource(strings = {"1","2","3","4"})
	public void validSendType(String sendType) {
		
		String phoneNumber = null;
		String email = null;
		SendTypeEnum sendTypeValue = SendTypeEnum.fromValue(sendType);
		if(SendTypeEnum.EMAIL==sendTypeValue) {
			email = "xx@xx.com.br";
		}else {
			phoneNumber = "1190000000";
		}
		communicationSchedulingRules.validateRecipientForSendType(phoneNumber, email, sendTypeValue.toString());
	}

	@ParameterizedTest(name="Invalidando sendType {0}")
	@ValueSource(strings = {"1","2","3","4"})
	public void invalidSendType(String sendType) {
		
		final String phoneNumber = "11";
		final String email = "xx";
		BusinessException be = null;
		SendTypeEnum sendTypeValue = SendTypeEnum.fromValue(sendType);
		if(SendTypeEnum.EMAIL==sendTypeValue) {
			be = assertThrows(BusinessException.class,()->communicationSchedulingRules.validateRecipientForSendType(phoneNumber, null, sendTypeValue.toString()));
			assertEquals(ResponseCodeValues.EMAIL_MANDATORY, be.getInternalCode());
		}else {
			be = assertThrows(BusinessException.class,()->communicationSchedulingRules.validateRecipientForSendType(null, email, sendTypeValue.toString()));
			assertEquals(ResponseCodeValues.PHONE_MANDATORY, be.getInternalCode());
		}
		assertEquals(HttpStatus.BAD_REQUEST, be.getStatus());
	}	

}

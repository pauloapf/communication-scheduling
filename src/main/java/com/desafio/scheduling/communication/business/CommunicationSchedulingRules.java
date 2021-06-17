package com.desafio.scheduling.communication.business;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.desafio.scheduling.communication.api.response.ResponseCodeValues;
import com.desafio.scheduling.communication.dao.domain.CommunicationScheduling;
import com.desafio.scheduling.communication.exception.BusinessException;
import com.desafio.scheduling.communication.model.SendTypeEnum;
import com.desafio.scheduling.communication.model.StatusCommunicationEnum;
import com.desafio.scheduling.communication.util.Util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CommunicationSchedulingRules {

	private Util communicationSchedulingUtil;

	public CommunicationSchedulingRules(
			@Autowired Util communicationSchedulingUtil
			) {
		this.communicationSchedulingUtil = communicationSchedulingUtil;
	}
	
	public LocalDateTime getValidScheduleDate(String scheduleDate, LocalDate currentDate) {
		LocalDateTime scheduleDateTime;
		try {
			scheduleDateTime = communicationSchedulingUtil.apiFormatToLocalDateTime(scheduleDate);
		} catch (ParseException e) {
			log.error("scheduleDate {} não está no formato esperado", scheduleDate);
			throw new BusinessException(ResponseCodeValues.INVALID_SCHEDULE_DATE_FORMAT, HttpStatus.BAD_REQUEST, "Data fora do formato esperado", e);
		}
		if(!scheduleDateTime.toLocalDate().isAfter(currentDate)) {
			throw new BusinessException(ResponseCodeValues.SCHEDULE_DATE_NOT_GREATHER_THAN_CURRENT_DATE,HttpStatus.BAD_REQUEST, "Dia do schedule date precisa ser superior a data atual");
		}
		return scheduleDateTime;
	}
	
	public void validateRecipientForSendType(String phoneNumber, String email, String sendType) {
		if(SendTypeEnum.EMAIL==SendTypeEnum.fromValue(sendType) && !StringUtils.hasText(email)) {
			throw new BusinessException(ResponseCodeValues.EMAIL_MANDATORY, HttpStatus.BAD_REQUEST, "Email obrigatorio para tipo de envio "+ sendType.toString());
		}
		if(SendTypeEnum.EMAIL!=SendTypeEnum.fromValue(sendType) && !StringUtils.hasText(phoneNumber)) {
			throw new BusinessException(ResponseCodeValues.PHONE_MANDATORY, HttpStatus.BAD_REQUEST, "Phone Number obrigatorio para tipo de envio "+ sendType.toString());
		}
	}
	
	public String getScheduleLocalDateTimeToString(LocalDateTime scheduleDate){
		return communicationSchedulingUtil.localDateTimeToApi(scheduleDate);		
	}

	/**
	 * Data 
	 * @param scheduleDate
	 * @return
	 */
	public boolean isCommunicationAvailableToBeDeleted(LocalDateTime scheduleDate, String status) {
		return scheduleDate.isAfter(LocalDate.now().plusDays(1).atStartOfDay()) && StatusCommunicationEnum.AGENDADO.toString().equals(status);
	}

	public void isValidSendType(@NotNull String sendType) {
		if(null==SendTypeEnum.fromValue(sendType)) {
			throw new BusinessException(ResponseCodeValues.SEND_TYPE_INVALID, HttpStatus.BAD_REQUEST, "Send Type deve estar contido em valores de 1 a 4");
		}
		
	}
}

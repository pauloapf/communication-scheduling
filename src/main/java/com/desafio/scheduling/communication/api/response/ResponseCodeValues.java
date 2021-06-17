package com.desafio.scheduling.communication.api.response;

public class ResponseCodeValues {

    public static final String INVALID_SCHEDULE_DATE_FORMAT = "invalid.schedule.date.format";
	public static final String SCHEDULE_DATE_NOT_GREATHER_THAN_CURRENT_DATE = "schedule.date.not.greater.than.today";
	public static final String EMAIL_MANDATORY = "-1001";
	public static final String PHONE_MANDATORY = "-1002";
	public static final String ID_NOT_FOUND = "-1003";
	public static final String DELETE_UNAVAILABLE_DUE_TO_DATE_OR_STATUS = "-1004";
	public static final String SEND_TYPE_INVALID = "-1005";
	
}
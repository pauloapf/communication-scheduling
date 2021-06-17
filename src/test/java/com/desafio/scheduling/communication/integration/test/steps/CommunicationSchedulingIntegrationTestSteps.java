package com.desafio.scheduling.communication.integration.test.steps;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextLoader;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.desafio.scheduling.communication.Swagger2SpringBoot;
import com.desafio.scheduling.communication.dao.domain.CommunicationScheduling;
import com.desafio.scheduling.communication.model.ResponseError;
import com.desafio.scheduling.communication.model.SchedulingCreationRequest;
import com.desafio.scheduling.communication.model.SchedulingCreationResponse;
import com.desafio.scheduling.communication.model.SchedulingStatusResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import cucumber.api.java.Before;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

@ContextConfiguration(classes = Swagger2SpringBoot.class, loader = ContextLoader.class)
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = { 
		"spring.profiles.active=dev",
		"spring.application.name=desafio-communication-scheduling", })
public class CommunicationSchedulingIntegrationTestSteps {

	@LocalServerPort
	int port;
	private String host = "http://localhost:";
	private String path = "/communication/";
	private String url = null;
	private ResponseEntity<ResponseError> responseError;	
	CloseableHttpClient httpClient;
	private ObjectMapper objectMapper;
	private SimpleDateFormat sdf;
	private ResponseEntity<SchedulingCreationResponse> schedulingCreationResponse = null;
	private ResponseEntity<SchedulingStatusResponse> schedulingStatusResponse = null;
	private HttpHeaders headers;

	@Before
	public void setup() {
		objectMapper = new ObjectMapper();
		JacksonTester.initFields(this, objectMapper);
		httpClient = HttpClients.createDefault();

		url = host + Integer.toString(port) + path;
		sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH");
		sdf.setLenient(false);
		
		headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		
	}	
	
	@Autowired
	private TestRestTemplate restTemplate;
	
	@When("^I create a communication scheduling with \"([^\"]*)\",\"([^\"]*)\",\"([^\"]*)\",\"([^\"]*)\"$")
	public void i_create_a_communication_scheduling_with(String email, String phoneNumber, String sendType, String message) throws Throwable {
		SchedulingCreationRequest request = new SchedulingCreationRequest();
		request.setEmail(email);
		request.setMessage(message);
		request.setPhoneNumber(phoneNumber);
		request.setScheduleDate(localDateTimeToString(LocalDateTime.now().plusDays(1)));
		request.setSendType(sendType);
		
		HttpEntity<?> requestEntity = new HttpEntity<Object>(request, headers);
		schedulingCreationResponse = restTemplate.exchange(url, HttpMethod.POST, requestEntity, SchedulingCreationResponse.class);
	}

	@Then("^I validate that create returns \"([^\"]*)\",\"([^\"]*)\",\"([^\"]*)\",\"([^\"]*)\",(\\d+),(\\d+),\"([^\"]*)\"$")
	public void i_validate_that_create_returns(String email, String phoneNumber, String sendType, String message, int id, int httpStatus, String internalCode) throws Throwable {
		
		assertEquals(httpStatus, schedulingCreationResponse.getStatusCodeValue());
		assertEquals(email, schedulingCreationResponse.getBody().getEmail());
		assertEquals(message, schedulingCreationResponse.getBody().getMessage());
		assertEquals(phoneNumber, schedulingCreationResponse.getBody().getPhoneNumber());
		assertEquals(localDateTimeToString(LocalDateTime.now().plusDays(1)), schedulingCreationResponse.getBody().getScheduleDate());
		assertEquals(sendType, schedulingCreationResponse.getBody().getSendType());
		assertEquals(id, schedulingCreationResponse.getBody().getId());
		assertEquals(CommunicationScheduling.STATUS_AGENDADO, schedulingCreationResponse.getBody().getStatus().toString());
		assertEquals(CommunicationScheduling.STATUS_DESCRIPTION_AGENDADO, schedulingCreationResponse.getBody().getStatusDescription());
		
	}
	

	@When("^I get a communication scheduling with \"([^\"]*)\"$")
	public void i_get_a_communication_scheduling_with(String id) throws Throwable {
		HttpEntity<?> requestEntity = new HttpEntity<Object>(headers);
		schedulingStatusResponse = restTemplate.exchange(url+id,HttpMethod.GET, requestEntity, SchedulingStatusResponse.class);
	}

	@Then("^I get that returns \"([^\"]*)\",\"([^\"]*)\",\"([^\"]*)\",\"([^\"]*)\",(\\d+),(\\d+),\"([^\"]*)\"$")
	public void i_get_that_returns(String email, String phoneNumber, String sendType, String message, int id, int httpStatus, String internalCode) throws Throwable {
		assertEquals(HttpStatus.OK, schedulingStatusResponse.getStatusCode());
		assertEquals(email, schedulingStatusResponse.getBody().getEmail());
		assertEquals(message, schedulingStatusResponse.getBody().getMessage());
		assertEquals(phoneNumber, schedulingStatusResponse.getBody().getPhoneNumber());
		assertEquals(localDateTimeToString(LocalDateTime.now().plusDays(1)), schedulingStatusResponse.getBody().getScheduleDate());
		assertEquals(sendType, schedulingStatusResponse.getBody().getSendType());
		assertEquals(id, schedulingStatusResponse.getBody().getId());
		assertEquals(CommunicationScheduling.STATUS_AGENDADO, schedulingStatusResponse.getBody().getStatus().toString());
		assertEquals(CommunicationScheduling.STATUS_DESCRIPTION_AGENDADO, schedulingStatusResponse.getBody().getStatusDescription());
	}
	
	@When("^I delete a communication scheduling with \"([^\"]*)\"$")
	public void i_delete_a_communication_scheduling_with(String id) throws Throwable {
		HttpEntity<?> requestEntity = new HttpEntity<Object>(headers);
		schedulingStatusResponse = restTemplate.exchange(url+id,HttpMethod.DELETE, requestEntity, SchedulingStatusResponse.class);
	}

	@Then("^I delete that returns (\\d+)$")
	public void i_delete_that_returns(int httpStatus) throws Throwable {
		assertEquals(HttpStatus.NO_CONTENT, schedulingStatusResponse.getStatusCode());
	}	
	
	@When("^I get a communication scheduling after delete with \"([^\"]*)\"$")
	public void i_get_a_communication_scheduling_after_delete_with(String id) throws Throwable {
		HttpEntity<?> requestEntity = new HttpEntity<Object>(headers);
		schedulingStatusResponse = restTemplate.exchange(url+id,HttpMethod.GET, requestEntity, SchedulingStatusResponse.class);
	}

	@Then("^I get after delete that returns$")
	public void i_get_after_delete_that_returns() throws Throwable {
		assertEquals(HttpStatus.NOT_FOUND, schedulingStatusResponse.getStatusCode());
	}	
	
	private String localDateTimeToString(LocalDateTime localDateTime) {
		return sdf.format(java.util.Date.from(localDateTime
				.atZone(ZoneId.systemDefault())
			      .toInstant()));
	}
	
}

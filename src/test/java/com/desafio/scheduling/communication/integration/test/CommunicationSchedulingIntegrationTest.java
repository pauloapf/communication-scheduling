package com.desafio.scheduling.communication.integration.test;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(
	glue = {"com.desafio.scheduling.communication.integration.test.steps"},
    plugin = {"pretty"},
    features = {"src/test/resources/it.feature"},
    monochrome = true
    //Utilize tags para rodar Scenarios especificos
    //, tags = "@tag1"
)
public class CommunicationSchedulingIntegrationTest {

}

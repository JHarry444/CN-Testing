package com.qa.cne.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qa.cne.persistence.domain.Badger;

//Launches the Spring context (on a random port to avoid conflicts)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)

//Automatically configures the MockMvc object we'll use to perform our tests
@AutoConfigureMockMvc

//Executes the schema and data files to reset the Badger table BEFORE each test
@Sql(scripts = { "classpath:badger-schema.sql",
		"classpath:badger-data.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
public class BadgerIntegrationTest {

	@Autowired
	// Object provided by the Spring testing dependency to allow for easy
	// integration testing
	private MockMvc mockMVC;

	@Autowired
	// Object Spring uses to convert objects sent from/to the @Controller's to and
	// from JSON
	private ObjectMapper mapper;

	@Test
	void testDelete() throws Exception {
		this.mockMVC.perform(delete("/remove/1")).andExpect(status().isOk());
	}

	@Test
	void testCreate() throws Exception {
		// Create the request body
		Badger testBadger = new Badger("Bodger", 31, "BBC");
		// Convert request body to JSON
		String badgerAsJSON = this.mapper.writeValueAsString(testBadger);

		// Create EXPECTED response body
		Badger expectedBadger = new Badger(2L, testBadger.getName(), testBadger.getAge(), testBadger.getHabitat());
		// Convert it to JSON
		String excpectedBadgerAsJSON = this.mapper.writeValueAsString(expectedBadger);

		// I've broken the next bit apart so I can comment each part seperately

		// BUILD THE TEST REQUEST
		// Method = POST
		// URL = /create
		// body = badgerAsJSON
		// headers = { "content-type": "application/json" }
		RequestBuilder request = post("/create").content(badgerAsJSON).contentType(MediaType.APPLICATION_JSON);

		// TEST THE STATUS
		// For a create method it should be 201 (CREATED)
		ResultMatcher checkStatus = status().isCreated();

		// TEST THE RESPONSE BODY
		// Should match excpectedBadgerAsJSON
		ResultMatcher checkBody = content().json(excpectedBadgerAsJSON);

		// PERFORM THE TEST REQUEST
		// Sends the POST request we created earlier
		// Checks the response has the correct status code
		// Checks the response body matches our expected badger.
		this.mockMVC.perform(request).andExpect(checkStatus).andExpect(checkBody);
	}

	@Test
	void testUpdate() throws Exception {
		Badger testBadger = new Badger("Honey", 12, "Sahara");
		String badgerAsJSON = this.mapper.writeValueAsString(testBadger);
		Long id = 1L;
		Badger expectedBadger = new Badger(id, testBadger.getName(), testBadger.getAge(), testBadger.getHabitat());
		String excpectedBadgerAsJSON = this.mapper.writeValueAsString(expectedBadger);

		this.mockMVC.perform(put("/update/" + id).content(badgerAsJSON).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isAccepted()).andExpect(content().json(excpectedBadgerAsJSON));
	}

	@Test
	void testRead() throws Exception {
		List<Badger> testBadgers = new ArrayList<>();
		testBadgers.add(new Badger(1L, "Barry", 26, "Chase"));
		String testBadgersAsJSON = this.mapper.writeValueAsString(testBadgers);

		this.mockMVC.perform(get("/get")).andExpect(status().isOk()).andExpect(content().json(testBadgersAsJSON));
	}

}

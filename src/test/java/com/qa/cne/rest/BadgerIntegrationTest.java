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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qa.cne.persistence.domain.Badger;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Sql(scripts = { "classpath:badger-schema.sql",
		"classpath:badger-data.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
public class BadgerIntegrationTest {

	@Autowired
	private MockMvc mockMVC;

	@Autowired
	private ObjectMapper mapper;

	@Test
	void testDelete() throws Exception {
		this.mockMVC.perform(delete("/remove/1")).andExpect(status().isOk());
	}

	@Test
	void testCreate() throws Exception {
		Badger testBadger = new Badger("Bodger", 31, "BBC");
		String badgerAsJSON = this.mapper.writeValueAsString(testBadger);

		Badger expectedBadger = new Badger(2L, testBadger.getName(), testBadger.getAge(), testBadger.getHabitat());
		String excpectedBadgerAsJSON = this.mapper.writeValueAsString(expectedBadger);

		this.mockMVC.perform(post("/create").content(badgerAsJSON).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated()).andExpect(content().json(excpectedBadgerAsJSON));
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

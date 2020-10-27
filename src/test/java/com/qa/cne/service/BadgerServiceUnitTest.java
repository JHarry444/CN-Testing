package com.qa.cne.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import com.qa.cne.persistence.domain.Badger;
import com.qa.cne.persistence.repo.BadgerRepo;

@SpringBootTest
@ActiveProfiles("test")
public class BadgerServiceUnitTest {

	@Autowired
	private BadgerService service;

	// Bean - Managed Object - Any object that Spring creates for you
	@MockBean // Instructs Spring to create a mock BadgerRepo and inject it into the
				// BadgerService
	private BadgerRepo repo;

	@Test
	void testCreate() {
		// GIVEN
		Badger testBadger = new Badger("bodger", 22, "Woods");
		Badger savedBadger = new Badger(1L, "bodger", 22, "Woods");

		// WHEN
		Mockito.when(this.repo.save(testBadger)).thenReturn(savedBadger);
		// this.repo.save is called with testBadger as the value then return savedBadger

		// THEN
		assertThat(this.service.createBadger(testBadger)).isEqualTo(savedBadger);

		Mockito.verify(this.repo, Mockito.times(1)).save(testBadger);

	}

	@Test
	void testRead() {
		// GIVEN
		List<Badger> woods = new ArrayList<>();
		woods.add(new Badger(1L, "Barry", 26, "Chase"));

		// WHEN
		Mockito.when(this.repo.findAll()).thenReturn(woods);

		// THEN
		assertThat(this.service.getBadger()).isEqualTo(woods);

		Mockito.verify(this.repo, Mockito.times(1)).findAll();
	}

	@Test
	void testUpdate() {
		// GIVEN
		Badger newBadger = new Badger("paul", 24, "Chase"); // new info
		Long id = 1L;

		Badger foundBadger = new Badger(id, "Barry", 26, "Chase"); // existing one in db

		Badger updatedBadger = new Badger(id, "paul", 24, "Chase");

		// WHEN
		Mockito.when(this.repo.findById(id)).thenReturn(Optional.of(foundBadger));
		Mockito.when(this.repo.save(updatedBadger)).thenReturn(updatedBadger);

		// THEN
		assertThat(this.service.updateBadger(newBadger, id)).isEqualTo(updatedBadger);

		Mockito.verify(this.repo, Mockito.times(1)).findById(id);
		Mockito.verify(this.repo, Mockito.times(1)).save(updatedBadger);
	}

	@Test
	void testDelete() {
		// GIVEN
		Long id = 1L;

		// WHEN
		Mockito.when(this.repo.existsById(id)).thenReturn(false);

		// THEN
		assertThat(this.service.deleteBadger(id)).isEqualTo(true);

		Mockito.verify(this.repo, Mockito.times(1)).existsById(id);
	}

}

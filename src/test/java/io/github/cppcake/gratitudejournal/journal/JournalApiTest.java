package io.github.cppcake.gratitudejournal.journal;

import io.github.cppcake.gratitudejournal.journal.entry.JournalEntry;
import io.github.cppcake.gratitudejournal.journal.entry.JournalEntryDTO;
import io.github.cppcake.gratitudejournal.journal.id_date_pair.IdDatePairDTO;

import io.github.cppcake.gratitudejournal.TestcontainersConfiguration;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.JsonNode;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.client.RestTestClient;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.client.RestTestClient.ResponseSpec;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.JsonMappingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Unit-Tests for the endpoints in
 * {@link io.github.cppcake.gratitudejournal.journal.JournalController}, testing the
 * presentation layer of the Journal-API.
 * 
 * @author Afeef Neiroukh
 */
@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureRestTestClient
@ActiveProfiles("test")
class JournalApiTest {
	/**
	 * Port of the testing service.
	 */
	@LocalServerPort
	private int port;

	/**
	 * HTTP-Client to interact with the presentation layer.
	 */
	@Autowired
	private RestTestClient restTestClient;

	/**
	 * Helper method to perform a POST-Request to add a new entry to a user's
	 * journal.
	 * 
	 * @param userName        The user name of the user to add the entry to.
	 * @param journalEntryDTO JournalEntryDTO object describing the fields of the
	 *                        new entry.
	 * @return {@link ResponseSpec} of the request.
	 */
	ResponseSpec requestAddEntry(String userName, JournalEntryDTO journalEntryDTO) {
		return restTestClient.post()
				.uri("http://localhost:%d/journal/%s".formatted(port, userName))
				.body(journalEntryDTO)
				.exchange();
	}

	/**
	 * Helper method to perform a GET-Request for a user's journal.
	 * 
	 * @param userName The user name of the user to retrieve the journal from.
	 * @return {@link ResponseSpec} of the request.
	 */
	ResponseSpec requestGetEntries(String userName) {
		return restTestClient.get()
				.uri("http://localhost:%d/journal/%s".formatted(port, userName))
				.exchange();
	}

	/**
	 * Helper method to perform a GET-Request for a user's journal and return the
	 * results.
	 * 
	 * @param userName The user name of the user to retrieve the journal from.
	 * @return Array containing an IdDatePairDTO for every entry in the journal.
	 */
	IdDatePairDTO[] requestGetEntriesWithResult(String userName) {
		String jsonResult = requestGetEntries(userName)
				.expectBody(String.class).returnResult().getResponseBody();
		ObjectMapper mapper = new ObjectMapper();

		try {
			JsonNode embedded = mapper.readTree(jsonResult).path("_embedded").path("idDatePairDTOList");
			List<IdDatePairDTO> idDatePairList = new ArrayList<>();
			for (JsonNode node : embedded) {
				Long id = node.get("id").asLong();
				LocalDate date = LocalDate.parse(node.get("date").asText());
				idDatePairList.add(new IdDatePairDTO(id, date));
			}
			return idDatePairList.toArray(new IdDatePairDTO[0]);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		return new IdDatePairDTO[0];
	}

	/**
	 * Helper method to perform a GET-Request on a specific journal entry.
	 * 
	 * @param journalEntryId The id of the journal entry to retrieve.
	 * @return {@link ResponseSpec} of the request.
	 */
	ResponseSpec requestGetEntry(Long journalEntryId) {
		return restTestClient.get()
				.uri("http://localhost:%d/journal/entry/%d".formatted(port, journalEntryId))
				.exchange();
	}

	/**
	 * Helper method to perform a PUT-Request on a specific journal entry.
	 * 
	 * @param journalEntryId  The id of the journal entry to update.
	 * @param journalEntryDTO JournalEntryDTO object containing the necessary fields
	 *                        to update the journal entry.
	 * @return {@link ResponseSpec} of the request.
	 */
	ResponseSpec requestPutEntry(Long journalEntryId, JournalEntryDTO journalEntryDTO) {
		return restTestClient.put()
				.uri("http://localhost:%d/journal/entry/%d".formatted(port, journalEntryId))
				.body(journalEntryDTO)
				.exchange();
	}

	/**
	 * Helper method to perform a DELETE-Request on a specific journal entry.
	 * 
	 * @param journalEntryId The id of the journal entry to delete.
	 * @return {@link ResponseSpec} of the request.
	 */
	ResponseSpec requestDeleteEntry(Long journalEntryId) {
		return restTestClient.delete()
				.uri("http://localhost:%d/journal/entry/%d".formatted(port, journalEntryId))
				.exchange();
	}

	/**
	 * Unit-Test for POST-Request adding a new entry to a user's journal.
	 */
	@Test
	void addEntry() {
		JournalEntryDTO journalEntryDTO = new JournalEntryDTO(JournalEntry.WellBeing.GOOD, "Cake", "Cake is tasty.",
				"Computers", "They empower me to do awesome things.");
		requestAddEntry("test1UserNameJournal", journalEntryDTO).expectStatus().isCreated();

		IdDatePairDTO[] entries = requestGetEntriesWithResult("test1UserNameJournal");

		assertNotNull(entries);
		assertEquals(1, entries.length);
		assertNotNull(entries[0].id());
		assertEquals(entries[0].date(), LocalDate.now());

		requestGetEntry(entries[0].id()).expectStatus().isOk().expectBody(JournalEntry.class)
				.value(entry -> {
					assertTrue(JournalEntryDTO.compareToEntry(journalEntryDTO, entry));
				});
	}

	/**
	 * Unit-Test for POST-Request adding a new entry when one already exists for
	 * today.
	 */
	@Test
	void addEntryConflictingDate() {
		JournalEntryDTO journalEntryDTO = new JournalEntryDTO(JournalEntry.WellBeing.GOOD, "A", "AAA", "B",
				"BBB");

		requestAddEntry("test1UserNameJournal", journalEntryDTO).expectStatus();
	}

	/**
	 * Unit-Test for POST-Request adding a new entry for a non-existing user.
	 */
	@Test
	void addEntryForMissingUser() {
		JournalEntryDTO journalEntryDTO = new JournalEntryDTO(JournalEntry.WellBeing.GOOD, "A", "AAA", "B",
				"BBB");

		requestAddEntry("thisUserDoesNotExist", journalEntryDTO).expectStatus().isNotFound();
	}

	/**
	 * Unit-Test for GET-Request on a user's journal.
	 */
	@Test
	void getEntries() {
		IdDatePairDTO[] entries = requestGetEntriesWithResult("test2UserNameJournal");
		assertNotNull(entries);
		assertEquals(0, entries.length);

		JournalEntryDTO journalEntryDTO_1 = new JournalEntryDTO(JournalEntry.WellBeing.FANTASTIC, "A", "AAA", "B",
				"BBB");

		requestAddEntry("test2UserNameJournal", journalEntryDTO_1).expectStatus().isCreated();

		entries = requestGetEntriesWithResult("test2UserNameJournal");
		assertNotNull(entries);
		assertEquals(1, entries.length);
		assertNotNull(entries[0].id());
		assertEquals(LocalDate.now(), entries[0].date());

		requestGetEntry(entries[0].id()).expectStatus().isOk().expectBody(JournalEntry.class)
				.value(entry -> {
					assertTrue(JournalEntryDTO.compareToEntry(journalEntryDTO_1, entry));
				});

		requestAddEntry("test2UserNameJournal", journalEntryDTO_1).expectStatus().isEqualTo(409);

		entries = requestGetEntriesWithResult("test2UserNameJournal");
		assertNotNull(entries);
		assertEquals(1, entries.length);
	}

	/**
	 * Unit-Test for GET-Request on the journal of a non-existing user.
	 */
	@Test
	void getEntriesForMissingUser() {
		requestGetEntries("thisUserDoesNotExist").expectStatus().isNotFound();
	}

	/**
	 * Unit-Test for PUT-Request on a journal entry.
	 */
	@Test
	void putEntry() {
		JournalEntryDTO journalEntryDTO = new JournalEntryDTO(JournalEntry.WellBeing.FANTASTIC, "A", "AAA", "B",
				"BBB");

		requestAddEntry("test3UserNameJournal", journalEntryDTO).expectStatus().isCreated();

		IdDatePairDTO[] entries = requestGetEntriesWithResult("test3UserNameJournal");
		assertNotNull(entries);
		assertEquals(1, entries.length);
		assertNotNull(entries[0].id());
		assertEquals(LocalDate.now(), entries[0].date());

		Long journalEntryId = entries[0].id();

		requestGetEntry(entries[0].id()).expectStatus().isOk().expectBody(JournalEntry.class)
				.value(entry -> {
					assertTrue(JournalEntryDTO.compareToEntry(journalEntryDTO, entry));
				});

		JournalEntryDTO updatedEntryDTO = new JournalEntryDTO(JournalEntry.WellBeing.GOOD, "C", "CCC", "DDD",
				"DDD");
		requestPutEntry(journalEntryId, updatedEntryDTO).expectStatus().isOk();

		requestGetEntry(entries[0].id()).expectStatus().isOk().expectBody(JournalEntry.class)
				.value(entry -> {
					assertTrue(JournalEntryDTO.compareToEntry(updatedEntryDTO, entry));
				});
	}

	/**
	 * Unit-Test for PUT-Request on a non-existing journal entry.
	 */
	@Test
	void putEntryForInvalidEntryId() {
		JournalEntryDTO journalEntryDTO = new JournalEntryDTO(JournalEntry.WellBeing.FANTASTIC, "A", "AAA", "B",
				"BBB");
		requestPutEntry(Long.MIN_VALUE, journalEntryDTO).expectStatus().isNotFound();
	}

	/**
	 * Unit-Test for DELETE-Request on a journal entry.
	 */
	@Test
	void deleteEntry() {
		JournalEntryDTO journalEntryDTO = new JournalEntryDTO(JournalEntry.WellBeing.FANTASTIC, "A", "AAA", "B",
				"BBB");

		requestAddEntry("test4UserNameJournal", journalEntryDTO).expectStatus().isCreated();

		IdDatePairDTO[] entries = requestGetEntriesWithResult("test4UserNameJournal");
		assertNotNull(entries);
		assertEquals(1, entries.length);
		assertNotNull(entries[0].id());

		Long entryId = entries[0].id();

		requestGetEntry(entryId).expectStatus().isOk();

		requestDeleteEntry(entryId).expectStatus().isNoContent();

		entries = requestGetEntriesWithResult("test4UserNameJournal");
		assertNotNull(entries);
		assertEquals(0, entries.length);

		requestGetEntry(entryId).expectStatus().isNotFound();
	}

	/**
	 * Unit-Test for DELETE-Request on a non-existing journal entry.
	 */
	@Test
	void deleteEntryForInvalidEntryId() {
		requestDeleteEntry(Long.MIN_VALUE).expectStatus().isNotFound();
	}
}
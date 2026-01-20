package com.example.gratitude_journal.journal;

import com.example.gratitude_journal.journal.entry.JournalEntry;
import com.example.gratitude_journal.journal.entry.JournalEntryDTO;
import com.example.gratitude_journal.journal.id_date_pair.IdDatePairDTO;

import com.example.gratitude_journal.TestcontainersConfiguration;
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
import org.springframework.test.web.servlet.client.RestTestClient;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.client.RestTestClient.ResponseSpec;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.JsonMappingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureRestTestClient
class JournalApiTest {

	@LocalServerPort
	private int port;

	@Autowired
	private RestTestClient restTestClient;

	public JournalApiTest() {
	}

	ResponseSpec requestAddEntry(String userName, JournalEntryDTO entryDTO) {
		return restTestClient.post()
				.uri("http://localhost:%d/journal/%s".formatted(port, userName))
				.body(entryDTO)
				.exchange();
	}

	ResponseSpec requestGetEntries(String userName) {
		return restTestClient.get()
				.uri("http://localhost:%d/journal/%s".formatted(port, userName))
				.exchange();
	}

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

	ResponseSpec requestGetEntry(Long journalEntryId) {
		return restTestClient.get()
				.uri("http://localhost:%d/journal/entry/%d".formatted(port, journalEntryId))
				.exchange();
	}

	ResponseSpec requestPutEntry(Long journalEntryId, JournalEntryDTO updateEntryDTO) {
		return restTestClient.put()
				.uri("http://localhost:%d/journal/entry/%d".formatted(port, journalEntryId))
				.body(updateEntryDTO)
				.exchange();
	}

	ResponseSpec requestDeleteEntry(Long journalEntryId) {
		return restTestClient.delete()
				.uri("http://localhost:%d/journal/entry/%d".formatted(port, journalEntryId))
				.exchange();
	}

	@Test
	void addEntry() {
		JournalEntryDTO entryDTO = new JournalEntryDTO(JournalEntry.WellBeing.GOOD, "Cake", "Cake is tasty.",
				"Computers", "They empower me to do awesome things.");
		requestAddEntry("test1UserNameJournal", entryDTO).expectStatus().isCreated();

		IdDatePairDTO[] entries = requestGetEntriesWithResult("test1UserNameJournal");

		assertNotNull(entries);
		assertEquals(1, entries.length);
		assertNotNull(entries[0].id());
		assertEquals(entries[0].date(), LocalDate.now());

		requestGetEntry(entries[0].id()).expectStatus().isOk().expectBody(JournalEntry.class)
				.value(entry -> {
					assertTrue(JournalEntryDTO.compareToEntry(entryDTO, entry));
				});
	}

	@Test
	void addEntryThatDoesExist() {
		JournalEntryDTO entryDTO = new JournalEntryDTO(JournalEntry.WellBeing.GOOD, "A", "AAA", "B",
				"BBB");

		requestAddEntry("test1UserNameJournal", entryDTO).expectStatus();
	}

	@Test
	void addEntryForInvalidUser() {
		JournalEntryDTO entryDTO = new JournalEntryDTO(JournalEntry.WellBeing.GOOD, "A", "AAA", "B",
				"BBB");

		requestAddEntry("thisUserDoesNotExist", entryDTO).expectStatus().isNotFound();
	}

	@Test
	void getEntries() {
		IdDatePairDTO[] entries = requestGetEntriesWithResult("test2UserNameJournal");
		assertNotNull(entries);
		assertEquals(0, entries.length);

		JournalEntryDTO entryDTO_1 = new JournalEntryDTO(JournalEntry.WellBeing.FANTASTIC, "A", "AAA", "B",
				"BBB");

		requestAddEntry("test2UserNameJournal", entryDTO_1).expectStatus().isCreated();

		entries = requestGetEntriesWithResult("test2UserNameJournal");
		assertNotNull(entries);
		assertEquals(1, entries.length);
		assertNotNull(entries[0].id());
		assertEquals(LocalDate.now(), entries[0].date());

		requestGetEntry(entries[0].id()).expectStatus().isOk().expectBody(JournalEntry.class)
				.value(entry -> {
					assertTrue(JournalEntryDTO.compareToEntry(entryDTO_1, entry));
				});

		requestAddEntry("test2UserNameJournal", entryDTO_1).expectStatus().isEqualTo(409);

		entries = requestGetEntriesWithResult("test2UserNameJournal");
		assertNotNull(entries);
		assertEquals(1, entries.length);
	}

	@Test
	void getEntriesForInvalidUser() {
		requestGetEntries("thisUserDoesNotExist").expectStatus().isNotFound();
	}

	@Test
	void putEntry() {
		JournalEntryDTO entryDTO = new JournalEntryDTO(JournalEntry.WellBeing.FANTASTIC, "A", "AAA", "B",
				"BBB");

		requestAddEntry("test3UserNameJournal", entryDTO).expectStatus().isCreated();

		IdDatePairDTO[] entries = requestGetEntriesWithResult("test3UserNameJournal");
		assertNotNull(entries);
		assertEquals(1, entries.length);
		assertNotNull(entries[0].id());
		assertEquals(LocalDate.now(), entries[0].date());

		Long journalEntryId = entries[0].id();

		requestGetEntry(entries[0].id()).expectStatus().isOk().expectBody(JournalEntry.class)
				.value(entry -> {
					assertTrue(JournalEntryDTO.compareToEntry(entryDTO, entry));
				});

		JournalEntryDTO updatedEntryDTO = new JournalEntryDTO(JournalEntry.WellBeing.GOOD, "C", "CCC", "DDD",
				"DDD");
		requestPutEntry(journalEntryId, updatedEntryDTO).expectStatus().isOk();

		requestGetEntry(entries[0].id()).expectStatus().isOk().expectBody(JournalEntry.class)
				.value(entry -> {
					assertTrue(JournalEntryDTO.compareToEntry(updatedEntryDTO, entry));
				});
	}

	@Test
	void putEntryForInvalidEntryId() {
		JournalEntryDTO entryDTO = new JournalEntryDTO(JournalEntry.WellBeing.FANTASTIC, "A", "AAA", "B",
				"BBB");
		requestPutEntry(Long.MIN_VALUE, entryDTO).expectStatus().isNotFound();
	}

	@Test
	void deleteEntry() {
		JournalEntryDTO entryDTO = new JournalEntryDTO(JournalEntry.WellBeing.FANTASTIC, "A", "AAA", "B",
				"BBB");

		requestAddEntry("test4UserNameJournal", entryDTO).expectStatus().isCreated();

		IdDatePairDTO[] entries = requestGetEntriesWithResult("test4UserNameJournal");
		assertNotNull(entries);
		assertEquals(1, entries.length);
		assertNotNull(entries[0].id());

		Long enryId = entries[0].id();

		requestGetEntry(enryId).expectStatus().isOk();

		requestDeleteEntry(enryId).expectStatus().isNoContent();

		entries = requestGetEntriesWithResult("test4UserNameJournal");
		assertNotNull(entries);
		assertEquals(0, entries.length);

		requestGetEntry(enryId).expectStatus().isNotFound();
	}

	@Test
	void deleteEntryForInvalidEntryId() {
		requestDeleteEntry(Long.MIN_VALUE).expectStatus().isNotFound();
	}
}
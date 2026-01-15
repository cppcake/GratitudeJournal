package com.example.gratidude_journal.journal;

import com.example.gratidude_journal.journal.entry.IdDatePairDTO;
import com.example.gratidude_journal.journal.entry.JournalEntry;
import com.example.gratidude_journal.journal.entry.JournalEntryDTO;

import com.example.gratidude_journal.TestcontainersConfiguration;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.web.servlet.client.RestTestClient;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.client.RestTestClient.ResponseSpec;

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
		return requestGetEntries(userName).expectStatus().isOk()
				.expectBody(IdDatePairDTO[].class).returnResult().getResponseBody();
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

	@Test
	void addEntry() {
		JournalEntryDTO entryDTO = new JournalEntryDTO(JournalEntry.WellBeing.GOOD, "Cake", "Cake is tasty.",
				"Computers", "They empower me to do awesome things.");
		requestAddEntry("test1UserNameJournal", entryDTO).expectStatus().isCreated();

		IdDatePairDTO[] entries = requestGetEntriesWithResult("test1UserNameJournal");

		assertNotNull(entries);
		assertEquals(entries.length, 1);
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

		requestAddEntry("test1UserNameJournal", entryDTO).expectStatus().isForbidden();
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
		assertEquals(entries.length, 0);

		JournalEntryDTO entryDTO_1 = new JournalEntryDTO(JournalEntry.WellBeing.FANTASTIC, "A", "AAA", "B",
				"BBB");

		requestAddEntry("test2UserNameJournal", entryDTO_1).expectStatus().isCreated();

		entries = requestGetEntriesWithResult("test2UserNameJournal");
		assertNotNull(entries);
		assertEquals(entries.length, 1);
		assertNotNull(entries[0].id());
		assertEquals(entries[0].date(), LocalDate.now());

		requestGetEntry(entries[0].id()).expectStatus().isOk().expectBody(JournalEntry.class)
				.value(entry -> {
					assertTrue(JournalEntryDTO.compareToEntry(entryDTO_1, entry));
				});

		requestAddEntry("test2UserNameJournal", entryDTO_1).expectStatus().isForbidden();

		entries = requestGetEntriesWithResult("test2UserNameJournal");
		assertNotNull(entries);
		assertEquals(entries.length, 1);
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
		assertEquals(entries.length, 1);
		assertNotNull(entries[0].id());
		assertEquals(entries[0].date(), LocalDate.now());

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
}
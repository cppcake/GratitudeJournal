package com.example.gratidude_journal.journal;

import com.example.gratidude_journal.journal.entry.IdDatePairDTO;
import com.example.gratidude_journal.journal.entry.JournalEntry;
import com.example.gratidude_journal.journal.entry.JournalEntry.WellBeing;
import com.example.gratidude_journal.journal.entry.JournalEntryDTO;

import com.example.gratidude_journal.TestcontainersConfiguration;

import org.apache.catalina.connector.Response;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.util.Collection;

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

	// @Autowired
	// private JournalService journalService;

	public JournalApiTest() {
	}

	ResponseSpec requestAddEntry(String userName, JournalEntry.WellBeing wellBeing, String gratefullForToday,
			String gratefullForTodayDescription, String gratefullForInLife, String gratefullForInLifeDescription) {
		JournalEntryDTO newEntry = new JournalEntryDTO(wellBeing, gratefullForToday, gratefullForTodayDescription,
				gratefullForInLife, gratefullForInLifeDescription);
		return restTestClient.post()
				.uri("http://localhost:%d/journal/%s".formatted(port, userName))
				.body(newEntry)
				.exchange();
	}

	ResponseSpec requestGetEntries(String userName) {
		return restTestClient.get()
				.uri("http://localhost:%d/journal/%s".formatted(port, userName))
				.exchange();
	}

	ResponseSpec requestGetEntry(Long journalEntryId) {
		return restTestClient.get()
				.uri("http://localhost:%d/journal/entry/%d".formatted(port, journalEntryId))
				.exchange();
	}

	@Test
	void addEntry() {
		WellBeing wellBeing = JournalEntry.WellBeing.GOOD;
		String gft = "A";
		String gft_desc = "AAA";
		String gfl = "B";
		String gfl_desc = "BBB";
		requestAddEntry("test1UserNameJournal", wellBeing, gft, gft_desc, gfl, gfl_desc).expectStatus().isCreated();

		IdDatePairDTO[] entries = requestGetEntries("test1UserNameJournal").expectStatus().isOk()
				.expectBody(IdDatePairDTO[].class).returnResult().getResponseBody();

		assertNotNull(entries);
		assertEquals(entries.length, 1);
		assertNotNull(entries[0].id());
		assertEquals(entries[0].date(), LocalDate.now());

		requestGetEntry(entries[0].id()).expectStatus().isOk().expectBody(JournalEntry.class)
				.value(entry -> {
					org.junit.jupiter.api.Assertions.assertEquals(wellBeing, entry.getWellBeing());
					org.junit.jupiter.api.Assertions.assertEquals(gft, entry.getGratefullForToday());
					org.junit.jupiter.api.Assertions.assertEquals(gft_desc, entry.getGratefullForTodayDescription());
					org.junit.jupiter.api.Assertions.assertEquals(gfl, entry.getGratefullForInLife());
					org.junit.jupiter.api.Assertions.assertEquals(gfl_desc, entry.getGratefullForInLifeDescription());
				});
		;
	}

	@Test
	void addEntryThatDoesExist() {
		requestAddEntry("test1UserNameJournal", JournalEntry.WellBeing.GOOD, "A", "AAA", "B",
				"BBB")
				.expectStatus().isForbidden();
	}

	@Test
	void addEntryForInvalidUser() {
		requestAddEntry("thisUserDoesNotExist", JournalEntry.WellBeing.GOOD, "A", "AAA", "B",
				"BBB")
				.expectStatus().isNotFound();
	}

	@Test
	void getEntries() {
		requestGetEntries("test2UserNameJournal").expectStatus().isOk();
	}

	@Test
	void getEntriesForInvalidUser() {
		requestGetEntries("thisUserDoesNotExist").expectStatus().isNotFound();
	}
}
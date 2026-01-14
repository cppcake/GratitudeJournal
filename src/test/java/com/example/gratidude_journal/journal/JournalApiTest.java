package com.example.gratidude_journal.journal;

import com.example.gratidude_journal.TestcontainersConfiguration;

import org.junit.jupiter.api.Test;

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

	//@Autowired
	//private JournalService journalService;

	public JournalApiTest() {}

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
		return restTestClient.post()
				.uri("http://localhost:%d/journal/%s".formatted(port, userName))
				.exchange();
	}

	@Test
	void addEntry() {
		requestAddEntry("test1UserNameJournal", JournalEntry.WellBeing.GOOD, "A", "AAA", "B",
				"BBB")
				.expectStatus().isCreated();
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
}
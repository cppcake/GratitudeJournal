package com.example.gratitude_journal;

import org.springframework.boot.SpringApplication;

/**
 * Testing Application for the GratitudeJournal Service.
 * 
 * @author Afeef Neiroukh
 */
public class TestGratitudeJournalApplication {
	/**
	 * Default constructor.
	 */
	TestGratitudeJournalApplication() {

	}

	/**
	 * Main method of the Testing Application. Sets up the testing database.
	 * 
	 * @param args Array of arguments for configuring the application.
	 */
	public static void main(String[] args) {
		SpringApplication.from(GratitudeJournalApplication::main).with(TestcontainersConfiguration.class).run(args);
	}
}
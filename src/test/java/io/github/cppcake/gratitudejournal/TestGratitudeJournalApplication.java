package io.github.cppcake.gratitudejournal;

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
	 * Main method of the Testing Application. Connects to the test MySQL container
	 * and runs the tests.
	 * 
	 * @param args Array of arguments for configuring the application.
	 */
	public static void main(String[] args) {
		SpringApplication.from(GratitudeJournalApplication::main).with(TestcontainersConfiguration.class).run(args);
	}
}
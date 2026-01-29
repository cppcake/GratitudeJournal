package io.github.cppcake.gratitudejournal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * GratitudeJournal Application.
 * 
 * @author Afeef Neiroukh
 */
@SpringBootApplication
public class GratitudeJournalApplication {
	/**
	 * Default constructor.
	 */
	GratitudeJournalApplication() {
	}

	/**
	 * Main method of the GratitudeJournal Application.
	 * 
	 * @param args Array of arguments for configuring the application.
	 */
	public static void main(String[] args) {
		SpringApplication.run(GratitudeJournalApplication.class, args);
	}
}
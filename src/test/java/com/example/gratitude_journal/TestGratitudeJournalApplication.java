package com.example.gratitude_journal;

import org.springframework.boot.SpringApplication;

import com.example.gratitude_journal.GratitudeJournalApplication;

public class TestGratitudeJournalApplication {

	public static void main(String[] args) {
		SpringApplication.from(GratitudeJournalApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
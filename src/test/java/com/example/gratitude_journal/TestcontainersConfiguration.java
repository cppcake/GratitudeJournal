package com.example.gratitude_journal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.mysql.MySQLContainer;
import org.testcontainers.utility.DockerImageName;

import com.example.gratitude_journal.user.User;
import com.example.gratitude_journal.user.UserRepository;

/**
 * Testcontainer class. Provides a preconfigured database for the tests.
 * 
 * @author Afeef Neiroukh
 */
@TestConfiguration(proxyBeanMethods = false)
public class TestcontainersConfiguration {
	/**
	 * Default constructor.
	 */
	TestcontainersConfiguration() {

	}

	/**
	 * Logger to log the bean operations.
	 */
	private static final Logger log = LoggerFactory.getLogger(TestcontainersConfiguration.class);

	/**
	 * Bean creating and returning the test database.
	 * 
	 * @return {@link MySQLContainer} containing the MySQL instance for the tests.
	 */
	@Bean
	@ServiceConnection
	MySQLContainer mysqlContainer() {
		return new MySQLContainer(DockerImageName.parse("mysql:latest"));
	}

	/**
	 * Bean that creates a {@link CommandLineRunner} to set up the database state for
	 * testing.
	 * 
	 * @param repository User repository injected by Spring.
	 * @return {@link CommandLineRunner} to set up the database state for testing.
	 */
	@Bean
	CommandLineRunner testcontainersConfiguration(UserRepository repository) {
		return args -> {
			log.info("Repository reset");
			repository.deleteAll();
			log.info("Preloading " + repository.save(new User("test1UserName", "test1FirstName", "test1LastName")));
			log.info("Preloading " + repository.save(new User("test2UserName", "test2FirstName", "test2LastName")));
			log.info("Preloading " + repository.save(new User("test3UserName", "test3FirstName", "test3LastName")));
			log.info("Preloading " + repository
					.save(new User("test1UserNameJournal", "test1FirstNameJournal", "test1LastNameJournal")));
			log.info("Preloading " + repository
					.save(new User("test2UserNameJournal", "test2FirstNameJournal", "test2LastNameJournal")));
			log.info("Preloading " + repository
					.save(new User("test3UserNameJournal", "test3FirstNameJournal", "test3LastNameJournal")));
			log.info("Preloading " + repository
					.save(new User("test4UserNameJournal", "test4FirstNameJournal", "test4LastNameJournal")));
		};
	}
}
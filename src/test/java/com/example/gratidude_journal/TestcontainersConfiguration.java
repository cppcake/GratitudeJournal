package com.example.gratidude_journal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.mysql.MySQLContainer;
import org.testcontainers.utility.DockerImageName;

import com.example.gratidude_journal.user.User;
import com.example.gratidude_journal.user.UserRepository;

@TestConfiguration(proxyBeanMethods = false)
public class TestcontainersConfiguration {
	private static final Logger log = LoggerFactory.getLogger(TestcontainersConfiguration.class);

	@Bean
	@ServiceConnection
	MySQLContainer mysqlContainer() {
		return new MySQLContainer(DockerImageName.parse("mysql:latest"));
	}

	@Bean
	CommandLineRunner testcontainersConfiguration(UserRepository repository) {
		return args -> {
			repository.deleteAll();
			log.info("Preloading " + repository.save(new User("test1UserName", "test1FirstName", "test1LastName")));
			log.info("Preloading " + repository.save(new User("test2UserName", "test2FirstName", "test2LastName")));
			log.info("Preloading " + repository.save(new User("test3UserName", "test3FirstName", "test3LastName")));
		};
	}
}
package com.example.gratitude_journal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.gratitude_journal.user.User;
import com.example.gratitude_journal.user.UserRepository;

@Configuration
class LoadDatabase {
    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(UserRepository repository) {
        return args -> {
            repository.deleteAll();
            log.info("Preloading " + repository.save(new User("test1UserName", "test1FirstName", "test1LastName")));
            log.info("Preloading " + repository.save(new User("test2UserName", "test2FirstName", "test2LastName")));
            log.info("Preloading " + repository.save(new User("test3UserName", "test3FirstName", "test3LastName")));
        };
    }
}
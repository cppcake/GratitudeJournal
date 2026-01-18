package com.example.gratitude_journal.user;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/*
    Persistence Layer for User-API
*/
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserName(String userName);

    void deleteByUserName(String userName);
}
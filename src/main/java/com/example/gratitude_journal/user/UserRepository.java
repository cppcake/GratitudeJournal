package com.example.gratitude_journal.user;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * JpaRepository for the User Entity. Part of the persistence layer of the
 * User-API.
 * 
 * @author Afeef Neiroukh
 */
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Searches the repository for a user matching the provided user name. Returns
     * an Optional<User> object containing the user matching the provided user name
     * or an empty Optional<User> object if no user is found.
     * 
     * @param userName The user name of the requested user.
     * @return An Optional<User> object containing the user matching the provided
     *         user name or an empty Optional<User> object if no user is found.
     */
    Optional<User> findByUserName(String userName);
}
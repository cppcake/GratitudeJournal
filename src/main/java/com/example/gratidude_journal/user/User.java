package com.example.gratidude_journal.user;

import com.example.gratidude_journal.user.exception.*;

import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;

import com.example.gratidude_journal.journal.Journal;
import com.example.gratidude_journal.journal.JournalEntry;

/*
    Part of the Persistence Layer for User-API
*/
@Entity
public class User {
    private @Id @GeneratedValue Long userId;
    @Column(unique = true, updatable = false)
    private String userName;
    private String firstName;
    private String lastName;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "journal_id", unique = true)
    private Journal journal;

    public static boolean validateName(String name) {
        if (name == null)
            return false;

        if (name.length() < 2)
            return false;

        if (name.contains(" "))
            return false;

        return true;
    }

    public static boolean validateAllNames(String userName, String firstName, String lastName) {
        return validateName(userName) && validateName(firstName) && validateName(lastName);
    }

    protected User() {
        this.journal = new Journal();
    }

    public User(String userName, String firstName, String lastName) {
        this.setUserName(userName);
        this.setFirstName(firstName);
        this.setLastName(lastName);

        this.journal = new Journal();
    }

    public void addJournalEntry(JournalEntry entry) {
        journal.addEntry(entry);
    }

    public Long getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    private void setUserName(String userName) {
        if (!validateName(userName))
            throw new NameInvalidException(userName);

        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        if (!validateName(firstName))
            throw new NameInvalidException(firstName);

        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        if (!validateName(lastName))
            throw new NameInvalidException(lastName);

        this.lastName = lastName;
    }

    public Journal getJournal() {
        return journal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof User))
            return false;
        User user = (User) o;
        return Objects.equals(this.userId, user.userId) && Objects.equals(this.userName, user.userName)
                && Objects.equals(this.firstName, user.firstName) && Objects.equals(this.lastName, user.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.userId, this.userName);
    }

    @Override
    public String toString() {
        return "user{" + "userId=" + this.userId + ", userName='" + this.userName + '\'' + ", firstName='"
                + this.firstName + '\'' + ", lastName='" + this.lastName + '}';
    }
}
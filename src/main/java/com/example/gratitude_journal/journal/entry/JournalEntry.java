package com.example.gratitude_journal.journal.entry;

import com.example.gratitude_journal.journal.Journal;

import java.time.LocalDate;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

/*
    Part of the Persistence Layer for Journal-API
*/
@Entity
public class JournalEntry {
    @Id
    @GeneratedValue
    Long journalEntryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "journal_id")
    private Journal journal;

    public enum WellBeing {
        AWFUL,
        BAD,
        MILDLY_BAD,
        NEUTRAL,
        MILDLY_GOOD,
        GOOD,
        FANTASTIC
    }

    @Column(updatable = false)
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    WellBeing wellBeing;

    String gratefulForToday;
    String gratefulForTodayDescription;
    String gratefulForInLife;
    String gratefulForInLifeDescription;

    public JournalEntry() {
        this.date = LocalDate.now();
    }

    public JournalEntry(Journal journal) {
        this.journal = journal;
        this.date = LocalDate.now();
    }

    public void setJournal(Journal journal) {
        if (this.journal == null)
            this.journal = journal;
    }

    public LocalDate getDate() {
        return date;
    }

    public WellBeing getWellBeing() {
        return this.wellBeing;
    }

    public void setWellBeing(WellBeing wellBeing) {
        this.wellBeing = wellBeing;
    }

    public String getGratefulForToday() {
        return this.gratefulForToday;
    }

    public void setGratefulForToday(String gratefulForToday) {
        this.gratefulForToday = gratefulForToday;
    }

    public String getGratefulForTodayDescription() {
        return this.gratefulForTodayDescription;
    }

    public void setGratefulForTodayDescription(String gratefulForTodayDescription) {
        this.gratefulForTodayDescription = gratefulForTodayDescription;
    }

    public String getGratefulForInLife() {
        return this.gratefulForInLife;
    }

    public void setGratefulForInLife(String gratefulForInLife) {
        this.gratefulForInLife = gratefulForInLife;
    }

    public String getGratefulForInLifeDescription() {
        return this.gratefulForInLifeDescription;
    }

    public void setGratefulForInLifeDescription(String gratefulForInLifeDescription) {
        this.gratefulForInLifeDescription = gratefulForInLifeDescription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof JournalEntry))
            return false;
        JournalEntry journalEntry = (JournalEntry) o;
        return Objects.equals(this.journalEntryId, journalEntry.journalEntryId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.journalEntryId);
    }

    @Override
    public String toString() {
        return "JournalEntry of " + date.toString() + "\n" + "Grateful for today: " + gratefulForToday + ". Reason: "
                + gratefulForTodayDescription + ".\n" + "Grateful for in life: " + gratefulForInLife + ". Reason: "
                + gratefulForTodayDescription + ".";
    }
}
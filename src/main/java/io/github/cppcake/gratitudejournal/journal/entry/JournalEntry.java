package io.github.cppcake.gratitudejournal.journal.entry;

import io.github.cppcake.gratitudejournal.journal.Journal;

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

/**
 * JPA-Entity to represent a journal entry in the GratitudeJournal Service. Part
 * of the persistence layer of the Journal-API.
 * 
 * Every journal entry is uniquely identified by its primary key journalEntryId,
 * which is assigned automatically. Every journal entry belongs to exactly one
 * Journal, which is expressed using a ManyToOne Relation.
 * 
 * @author Afeef Neiroukh
 */
@Entity
public class JournalEntry {
    /**
     * The primary key of the journal entry.
     */
    @Id
    @GeneratedValue
    private Long journalEntryId;

    /**
     * The ManyToOne relation that maps every journal entry to exactly one
     * journal.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "journal_id")
    private Journal journal;

    /**
     * Enum containing the possible states of wellbeing.
     */
    public enum WellBeing {
        /** User feels awful. */
        AWFUL,
        /** User feels bad. */
        BAD,
        /** User feels mildly bad. */
        MILDLY_BAD,
        /** User feels neutral. */
        NEUTRAL,
        /** User feels mildly good. */
        MILDLY_GOOD,
        /** User feels good. */
        GOOD,
        /** User feels fantastic. */
        FANTASTIC
    }

    /**
     * The immutable creation date of the journal entry.
     */
    @Column(updatable = false)
    private LocalDate date;

    /**
     * The wellBeing reported by the user on this day.
     */
    @Enumerated(EnumType.STRING)
    private WellBeing wellBeing;

    /**
     * String saving what the user is grateful for today.
     */
    private String gratefulForToday;

    /**
     * String saving details about what the user is grateful for today.
     */
    private String gratefulForTodayDescription;

    /**
     * String saving what the user is grateful for in life.
     */
    private String gratefulForInLife;

    /**
     * String saving details about what the user is grateful for in life.
     */
    private String gratefulForInLifeDescription;

    /**
     * Default constructor for JPA.
     */
    JournalEntry() {
        this.date = LocalDate.now();
    }

    /**
     * Public constructor mapping directly to the journal.
     * 
     * @param journal The journal this journal entry should map to.
     */
    public JournalEntry(Journal journal) {
        this.journal = journal;
        this.date = LocalDate.now();
    }

    /**
     * Get the id of this journal entry.
     * 
     * @return id (primary key) of this journal entry.
     */
    public Long getJournalEntryId() {
        return journalEntryId;
    }

    /**
     * Assigns this journal entry to a journal.
     * 
     * @param journal The journal to assign this journal entry to.
     */
    public void setJournal(Journal journal) {
        if (this.journal == null)
            this.journal = journal;
    }

    /**
     * Get the creation date of this journal entry.
     * 
     * @return The creation date of this journal entry.
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Get the value of {@link #wellBeing}.
     * 
     * @return the value of the {@link #wellBeing} field
     */
    public WellBeing getWellBeing() {
        return this.wellBeing;
    }

    /**
     * Set the value of {@link #wellBeing}
     * 
     * @param wellBeing Value to set {@link #wellBeing} to.
     */
    public void setWellBeing(WellBeing wellBeing) {
        this.wellBeing = wellBeing;
    }

    /**
     * Get the value of {@link #gratefulForToday}.
     * 
     * @return the value of the {@link #gratefulForToday} field.
     */
    public String getGratefulForToday() {
        return this.gratefulForToday;
    }

    /**
     * Set the value of {@link #gratefulForToday}
     * 
     * @param gratefulForToday Value to set {@link #gratefulForToday} to.
     */
    public void setGratefulForToday(String gratefulForToday) {
        this.gratefulForToday = gratefulForToday;
    }

    /**
     * Get the value of {@link #gratefulForTodayDescription}.
     * 
     * @return the value of the {@link #gratefulForTodayDescription} field.
     */
    public String getGratefulForTodayDescription() {
        return this.gratefulForTodayDescription;
    }

    /**
     * Set the value of {@link #gratefulForTodayDescription}
     * 
     * @param gratefulForTodayDescription Value to set
     *                                    {@link #gratefulForTodayDescription} to.
     */
    public void setGratefulForTodayDescription(String gratefulForTodayDescription) {
        this.gratefulForTodayDescription = gratefulForTodayDescription;
    }

    /**
     * Get the value of {@link #gratefulForInLife}.
     * 
     * @return the value of the {@link #gratefulForInLife} field.
     */
    public String getGratefulForInLife() {
        return this.gratefulForInLife;
    }

    /**
     * Set the value of {@link #gratefulForInLife}
     * 
     * @param gratefulForInLife Value to set {@link #gratefulForInLife} to.
     */
    public void setGratefulForInLife(String gratefulForInLife) {
        this.gratefulForInLife = gratefulForInLife;
    }

    /**
     * Get the value of {@link #gratefulForInLifeDescription}.
     * 
     * @return the value of the {@link #gratefulForInLifeDescription} field.
     */
    public String getGratefulForInLifeDescription() {
        return this.gratefulForInLifeDescription;
    }

    /**
     * Set the value of {@link #gratefulForInLifeDescription}
     * 
     * @param gratefulForInLifeDescription Value to set
     *                                     {@link #gratefulForInLifeDescription} to.
     */
    public void setGratefulForInLifeDescription(String gratefulForInLifeDescription) {
        this.gratefulForInLifeDescription = gratefulForInLifeDescription;
    }

    /**
     * Compare two journal entries based on their primary key.
     * 
     * @param o The object to compare this journal entry to.
     * @return true if objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof JournalEntry))
            return false;
        JournalEntry journalEntry = (JournalEntry) o;
        return Objects.equals(this.journalEntryId, journalEntry.journalEntryId);
    }

    /**
     * Generates the hash code of a journal entry based on its primary key.
     * 
     * @return The hash code of this object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.journalEntryId);
    }

    /**
     * Provides a basic String representation of this object.
     * 
     * @return The basic String representation of this object.
     */
    @Override
    public String toString() {
        return "JournalEntry of " + date.toString() + "\n" + "Grateful for today: " + gratefulForToday + ". Reason: "
                + gratefulForTodayDescription + ".\n" + "Grateful for in life: " + gratefulForInLife + ". Reason: "
                + gratefulForInLifeDescription + ".";
    }
}
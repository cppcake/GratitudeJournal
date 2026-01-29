package io.github.cppcake.gratitudejournal.user;

import io.github.cppcake.gratitudejournal.user.dto.ReturnUserDTO;
import io.github.cppcake.gratitudejournal.user.exception.NameInvalidException;

import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;

import io.github.cppcake.gratitudejournal.journal.Journal;

/**
 * JPA-Entity to represent a user in the GratitudeJournal Service. Part of the
 * persistence layer of the User-API.
 * 
 * Every user is uniquely identified either by its primary key userId or by the
 * unique userName. Furthermore a user consists of a firstName, lastName and a
 * journal.
 * 
 * The journal and userId are assigned automatically on creation, while the
 * userName, firstName and lastName must be passed on creation. The userId and
 * userName are immutable, the firstName and lastName are mutable and the state
 * of journal is mutable (but the reference is immutable).
 * 
 * The DTOs {@link io.github.cppcake.gratitudejournal.user.dto.SimpleUserDTO} and
 * {@link ReturnUserDTO} represent parts of the User. These are used in the
 * User-API to avoid exposing or demanding irrelevant fields and improve the
 * separation of concerns.
 * 
 * @author Afeef Neiroukh
 */
@Entity
public class User {
    /**
     * The private primary key of the user.
     */
    private @Id @GeneratedValue Long userId;

    /**
     * The private unique name of the user. Used in the API to identify users.
     */
    @Column(unique = true, updatable = false)
    private String userName;

    /**
     * The private first name of the user.
     */
    private String firstName;

    /**
     * The private last name of the user.
     */
    private String lastName;

    /**
     * The journal of the user. Every user has exactly one journal and every journal
     * belongs to exactly one user (One-To-One Mapping). The journal and its content
     * are updated/deleted automatically upon updating/deleting the user.
     */
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "journal_id", unique = true)
    private Journal journal;

    /**
     * Static method to validate if a given name is valid. A name is valid if it
     * fulfills the following conditions:
     * <ul>
     * <li>It is not null</li>
     * <li>Its length is 2 or greater</li>
     * <li>It contains no spaces</li>
     * </ul>
     * These rules are relaxed and could be tightened in the future by using regex.
     * 
     * @param name The name to validate.
     * @return Returns true if the name is valid and otherwise false.
     */
    public static boolean validateName(String name) {
        if (name == null)
            return false;

        if (name.length() < 2)
            return false;

        if (name.contains(" "))
            return false;

        return true;
    }

    /**
     * Convenient method to validate three names at once, typically to validate the
     * user name, first name and last name of a user.
     * 
     * @param userName  The first name to validate, usually the user name of the
     *                  user.
     * @param firstName The second name to validate, usually the first name of the
     *                  user.
     * @param lastName  The third name to validate, usually the last name of the
     *                  user.
     * @return True if all three names are valid, otherwise false.
     */
    public static boolean validateAllNames(String userName, String firstName, String lastName) {
        return validateName(userName) && validateName(firstName) && validateName(lastName);
    }

    /**
     * Constructs an empty user and assigns it a new Journal instance.
     */
    protected User() {
        this.journal = new Journal();
    }

    /**
     * Constructs a user and assigns it a new Journal instance.
     * 
     * @param userName  The user name of the user to create. Is validated
     *                  according to {@link User#validateName(String)} before
     *                  applying it.
     * @param firstName The first name of the user to create. Is validated
     *                  according to {@link User#validateName(String)} before
     *                  applying it.
     * @param lastName  The last name of the user to create. Is validated
     *                  according to {@link User#validateName(String)} before
     *                  applying it.
     * 
     * @throws NameInvalidException One or more of the names are invalid.
     */
    public User(String userName, String firstName, String lastName) {
        this.setUserName(userName);
        this.setFirstName(firstName);
        this.setLastName(lastName);

        this.journal = new Journal();
    }

    /**
     * Returns the userId (primary key) of the user. Because the userId is
     * immutable, there is no setter for this field.
     * 
     * @return The userId (primary key) of the user.
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * Returns the userName of the user.
     * 
     * @return The userName of the user.
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Sets the user name of the user with the userName given. userName is validated
     * according to {@link User#validateName(String)} before applying it.
     * 
     * @param userName The userName to validate and apply.
     *
     * @throws NameInvalidException userName is invalid.
     */
    private void setUserName(String userName) {
        if (!validateName(userName))
            throw new NameInvalidException(userName);

        this.userName = userName;
    }

    /**
     * Returns the firstName of the user.
     * 
     * @return The firstName of the user.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name of the user with the firstName given. firstName is
     * validated according to {@link User#validateName(String)} before applying it.
     * 
     * @param firstName The firstName to validate and apply.
     * 
     * @throws NameInvalidException firstName is invalid.
     */
    public void setFirstName(String firstName) {
        if (!validateName(firstName))
            throw new NameInvalidException(firstName);

        this.firstName = firstName;
    }

    /**
     * Returns the lastName of the user.
     * 
     * @return The lastName of the user.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name of the user with the lastName given. lastName is validated
     * according to {@link User#validateName(String)} before applying it.
     * 
     * @param lastName The lastName to validate and apply.
     * 
     * @throws NameInvalidException lastName is invalid.
     */
    public void setLastName(String lastName) {
        if (!validateName(lastName))
            throw new NameInvalidException(lastName);

        this.lastName = lastName;
    }

    /**
     * Returns the journal of the user.
     * 
     * @return The journal of the user.
     */
    public Journal getJournal() {
        return journal;
    }

    /**
     * Creates and returns a ReturnUserDTO object representing the fields userId,
     * userName, firstName and lastName of the user.
     * 
     * @return A ReturnUserDTO object that includes the fields userId, userName,
     *         firstName and lastName of the user.
     */
    public ReturnUserDTO toReturnUserDTO() {
        return new ReturnUserDTO(this.getUserId(), this.getUserName(), this.getFirstName(), this.getLastName());
    }

    /**
     * Compares two users based on their primary keys and names.
     * 
     * @param o The object to compare the user to.
     * @return true if objects are equal, false otherwise.
     */
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

    /**
     * Generates the hash code of a user based on its primary key.
     * 
     * @return The hash code of this object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.userId);
    }

    /**
     * Provides a basic String representation of this object.
     * 
     * @return String representation of this object.
     */
    @Override
    public String toString() {
        return "user{" + "userId=" + this.userId + ", userName='" + this.userName + '\'' + ", firstName='"
                + this.firstName + '\'' + ", lastName='" + this.lastName + '}';
    }
}
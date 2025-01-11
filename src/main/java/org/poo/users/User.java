package org.poo.users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.poo.accounts.ClassicAccount;
import org.poo.fileio.UserInput;
import org.poo.transactions.TransactionStrategy;

import java.util.ArrayList;

public class User {
    private String firstName;
    private String lastName;
    private String email;
    private ArrayList<ClassicAccount> accounts;

    @JsonIgnore
    private ArrayList<TransactionStrategy> transactions;

    /**
     * Constructs a new {@code User} based on the provided {@code UserInput} object.
     *
     * @param other the {@code UserInput} object containing the user's data
     */
    public User(final UserInput other) {
        this.firstName = other.getFirstName();
        this.lastName = other.getLastName();
        this.email = other.getEmail();
        accounts = new ArrayList<>();
        transactions = new ArrayList<>();
    }

    /**
     * Returns the first name of the user.
     *
     * @return the first name of the user
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name of the user.
     *
     * @param firstName the first name to set for the user
     */
    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    /**
     * Returns the last name of the user.
     *
     * @return the last name of the user
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name of the user.
     *
     * @param lastName the last name to set for the user
     */
    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    /**
     * Returns the email address of the user.
     *
     * @return the email address of the user
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address of the user.
     *
     * @param email the email address to set for the user
     */
    public void setEmail(final String email) {
        this.email = email;
    }

    /**
     * Returns the list of {@code ClassicAccount} objects associated with the user.
     *
     * @return the list of accounts associated with the user
     */
    public ArrayList<ClassicAccount> getAccounts() {
        return accounts;
    }

    /**
     * Sets the list of {@code ClassicAccount} objects for the user.
     *
     * @param accounts the list of accounts to set for the user
     */
    public void setAccounts(final ArrayList<ClassicAccount> accounts) {
        this.accounts = accounts;
    }

    /**
     * Returns the list of transactions performed by the user.
     *
     * @return the list of transactions performed by the user
     */
    public ArrayList<TransactionStrategy> getTransactions() {
        return transactions;
    }

    /**
     * Sets the list of transactions performed by the user.
     *
     * @param transactions the list of transactions to set for the user
     */
    public void setTransactions(final ArrayList<TransactionStrategy> transactions) {
        this.transactions = transactions;
    }
}

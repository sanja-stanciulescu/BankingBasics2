package org.poo.users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.poo.accounts.ClassicAccount;
import org.poo.fileio.UserInput;
import org.poo.servicePlan.Plan;
import org.poo.servicePlan.StandardPlan;
import org.poo.servicePlan.StudentPlan;
import org.poo.transactions.TransactionStrategy;
import org.poo.transactions.split_payment.SplitPaymentTransaction;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.TreeSet;

public class User {
    private String firstName;
    private String lastName;
    private String email;
    private ArrayList<ClassicAccount> accounts;

    @JsonIgnore
    private ArrayList<TransactionStrategy> transactions;
    @JsonIgnore
    private String birthDate;
    @JsonIgnore
    private String occupation;
    @JsonIgnore
    private int numberOfSavingsAccounts;
    @JsonIgnore
    private Plan servicePlan;
    @JsonIgnore
    private LinkedList<SplitPaymentTransaction> activeTransactions;

    /**
     * Constructs a new {@code User} based on the provided {@code UserInput} object.
     *
     * @param other the {@code UserInput} object containing the user's data
     */
    public User(final UserInput other) {
        this.firstName = other.getFirstName();
        this.lastName = other.getLastName();
        this.email = other.getEmail();
        this.birthDate = other.getBirthDate();
        this.occupation = other.getOccupation();
        accounts = new ArrayList<>();
        transactions = new ArrayList<>();
        activeTransactions = new LinkedList<>();

        if (other.getOccupation().equals("student")) {
            servicePlan = new StudentPlan();
        } else {
            servicePlan = new StandardPlan();
        }
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

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public int getNumberOfSavingsAccounts() {
        return numberOfSavingsAccounts;
    }

    public void setNumberOfSavingsAccounts(int numberOfSavingsAccounts) {
        this.numberOfSavingsAccounts = numberOfSavingsAccounts;
    }

    public Plan getServicePlan() {
        return servicePlan;
    }

    public void setServicePlan(Plan servicePlan) {
        this.servicePlan = servicePlan;
    }

    public LinkedList<SplitPaymentTransaction> getActiveTransactions() {
        return activeTransactions;
    }

    public void setActiveTransactions(LinkedList<SplitPaymentTransaction> activeTransactions) {
        this.activeTransactions = activeTransactions;
    }
}

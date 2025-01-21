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
    @JsonIgnore
    private int bigTransactions;

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
        bigTransactions = 0;

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

    /**
     * Returns the birth date of the user.
     *
     * @return the birth date as a string
     */
    public String getBirthDate() {
        return birthDate;
    }

    /**
     * Sets the birth date for the user.
     *
     * @param birthDate the birth date to set for the user, represented as a String
     */
    public void setBirthDate(final String birthDate) {
        this.birthDate = birthDate;
    }

    /**
     * Returns the occupation of the user.
     *
     * @return the occupation of the user
     */
    public String getOccupation() {
        return occupation;
    }

    /**
     * Sets the occupation of the user.
     *
     * @param occupation the occupation to set for the user
     */
    public void setOccupation(final String occupation) {
        this.occupation = occupation;
    }

    /**
     * Returns the total number of savings accounts associated with the user.
     *
     * @return the number of savings accounts
     */
    public int getNumberOfSavingsAccounts() {
        return numberOfSavingsAccounts;
    }

    /**
     * Sets the number of savings accounts associated with the user.
     *
     * @param numberOfSavingsAccounts the number of savings accounts to set
     */
    public void setNumberOfSavingsAccounts(final int numberOfSavingsAccounts) {
        this.numberOfSavingsAccounts = numberOfSavingsAccounts;
    }

    /**
     * Retrieves the service plan associated with the user.
     *
     * @return the service plan of the user, which defines the plan's features, commission rates, and other specifics.
     */
    public Plan getServicePlan() {
        return servicePlan;
    }

    /**
     * Sets the service plan for the user.
     *
     * @param servicePlan the {@code Plan} object representing the service plan to be assigned to the user
     */
    public void setServicePlan(final Plan servicePlan) {
        this.servicePlan = servicePlan;
    }

    /**
     * Retrieves the list of active split-payment transactions associated with the user.
     *
     * @return a {@code LinkedList} containing {@code SplitPaymentTransaction} objects that represent the active transactions.
     */
    public LinkedList<SplitPaymentTransaction> getActiveTransactions() {
        return activeTransactions;
    }

    /**
     * Returns the number of big transactions associated with the user.
     *
     * @return the number of big transactions
     */
    public int getBigTransactions() {
        return bigTransactions;
    }

    /**
     * Sets the number of big transactions for the user.
     *
     * @param bigTransactions the number of big transactions to set for the user
     */
    public void setBigTransactions(final int bigTransactions) {
        this.bigTransactions = bigTransactions;
    }
}

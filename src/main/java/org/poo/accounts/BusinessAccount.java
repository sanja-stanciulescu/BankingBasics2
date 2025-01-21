package org.poo.accounts;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.poo.business.BusinessCommerciant;
import org.poo.business.Employee;
import org.poo.business.Manager;
import org.poo.business.Owner;
import org.poo.exchangeRates.Bnr;
import org.poo.users.User;

import java.util.HashMap;
import java.util.Map;

public class BusinessAccount extends ClassicAccount {
    @JsonIgnore
    private static final int INITIAL_LIMIT = 500;
    @JsonIgnore
    private double depositLimit;
    @JsonIgnore
    private Map<String, Employee> employees;
    @JsonIgnore
    private Map<String, Manager> managers;
    @JsonIgnore
    private Map<String, BusinessCommerciant> businessCommerciants;
    @JsonIgnore
    private double spendingLimit;
    @JsonIgnore
    private double totalDeposited;
    @JsonIgnore
    private double totalSpent;

    @JsonIgnore
    private Owner owner;
    @JsonIgnore
    private Bnr bank;

    /**
     * Constructs a new {@code BusinessAccount} instance.
     *
     * @param iban the International Bank Account Number for the account
     * @param currency the currency of the account
     * @param type the type of the account
     * @param user the user who owns the account
     * @param bank the bank associated with the account
     */
    public BusinessAccount(final String iban,
                           final String currency,
                           final String type,
                           final User user,
                           final Bnr bank
    ) {
        super(iban, currency, type);
        this.employees = new HashMap<>();
        this.managers = new HashMap<>();
        this.businessCommerciants = new HashMap<>();
        this.owner = new Owner(user, this);
        this.bank = bank;

        if (!currency.equals("RON")) {
            double exchangeRate = bank.getExchangeRate("RON", currency);
            spendingLimit = exchangeRate * INITIAL_LIMIT;
            depositLimit = exchangeRate * INITIAL_LIMIT;
        } else {
            spendingLimit = INITIAL_LIMIT;
            depositLimit = INITIAL_LIMIT;
        }
    }

    /**
     * Retrieves the deposit limit of the business account.
     *
     * @return the maximum amount that can be deposited into the account.
     */
    public double getDepositLimit() {
        return depositLimit;
    }

    /**
     * Updates the deposit limit for the business account.
     *
     * @param depositLimit the maximum amount that can be deposited into the account.
     */
    public void setDepositLimit(final double depositLimit) {
        this.depositLimit = depositLimit;
    }

    /**
     * Retrieves the mapping of employees associated with the business account.
     *
     * @return a map where the key is the employee's identifier (e.g., email or ID)
     *         and the value is the corresponding {@code Employee} object.
     */
    public Map<String, Employee> getEmployees() {
        return employees;
    }

    /**
     * Updates the mapping of employees associated with the business account.
     *
     * @param employees a map where the key represents the unique identifier for an employee
     *                  (e.g., email or ID) and the value is the corresponding
     *                  {@code Employee} object.
     */
    public void setEmployees(final Map<String, Employee> employees) {
        this.employees = employees;
    }

    /**
     * Retrieves the mapping of managers associated with the business account.
     *
     * @return a map where the key is the manager's unique identifier (e.g., email or ID)
     *         and the value is the corresponding {@code Manager} object.
     */
    public Map<String, Manager> getManagers() {
        return managers;
    }

    /**
     * Updates the mapping of managers associated with the business account.
     *
     * @param managers a map where the key represents the unique identifier for a manager
     *                 (e.g., email or ID) and the value is the corresponding
     *                 {@code Manager} object.
     */
    public void setManagers(final Map<String, Manager> managers) {
        this.managers = managers;
    }

    /**
     * Retrieves the spending limit for the business account.
     *
     * @return the maximum amount that can be spent from the account.
     */
    public double getSpendingLimit() {
        return spendingLimit;
    }

    /**
     * Updates the spending limit for the business account.
     *
     * @param spendingLimit the maximum amount that can be spent from the account.
     */
    public void setSpendingLimit(final double spendingLimit) {
        this.spendingLimit = spendingLimit;
    }

    /**
     * Retrieves the total amount deposited into the business account.
     *
     * @return the total deposited amount as a double.
     */
    public double getTotalDeposited() {
        return totalDeposited;
    }

    /**
     * Updates the total amount that has been deposited into the business account.
     *
     * @param totalDeposited the new total amount of deposited funds, specified as a double.
     */
    public void setTotalDeposited(final double totalDeposited) {
        this.totalDeposited = totalDeposited;
    }

    /**
     * Retrieves the total amount spent from the business account.
     *
     * @return the total spent amount as a double.
     */
    public double getTotalSpent() {
        return this.totalSpent;
    }

    /**
     * Updates the total amount spent from the business account.
     *
     * @param totalSpent the new total amount of funds spent, specified as a double.
     */
    public void setTotalSpent(final double totalSpent) {
        this.totalSpent = totalSpent;
    }

    /**
     * Retrieves the owner of the business account.
     *
     * @return the {@code Owner} associated with the business account.
     */
    public Owner getOwner() {
        return owner;
    }

    /**
     * Updates the owner of the business account.
     *
     * @param owner the {@code Owner} object to be associated with the business account.
     */
    public void setOwner(final Owner owner) {
        this.owner = owner;
    }

    /**
     * Retrieves the mapping of business commerciants associated with the business account.
     *
     * @return a map where the key represents the unique identifier of a commerciant,
     *         and the value is the corresponding {@code BusinessCommerciant} object.
     */
    public Map<String, BusinessCommerciant> getBusinessCommerciants() {
        return businessCommerciants;
    }
}

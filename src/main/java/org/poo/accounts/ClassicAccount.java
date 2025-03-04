package org.poo.accounts;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.poo.cards.Card;
import org.poo.commerciants.Commerciant;
import org.poo.transactions.TransactionStrategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ClassicAccount {
    protected double balance;
    protected String currency;
    protected String type;
    protected ArrayList<Card> cards;

    @JsonIgnore
    private ArrayList<TransactionStrategy> transactions;
    @JsonIgnore
    private Commerciant commerciants;
    @JsonIgnore
    private Map<String, Double> coupons;
    @JsonIgnore
    private double totalSpentPerCommerciant;

    @JsonProperty("IBAN")
    protected String iban;

    @JsonIgnore
    protected double minBalance;

    /**
     * Constructs a new {@code ClassicAccount} instance with the specified IBAN,
     * currency, and account type.
     *
     * @param iban      the International Bank Account Number (IBAN) of the account;
     *                  cannot be null or empty.
     * @param currency  the currency of the account; cannot be null or empty.
     * @param type      the type of the account (e.g., "Savings", "Classic").
     */
    public ClassicAccount(final String iban, final String currency, final String type) {
        this.iban = iban;
        this.currency = currency;
        this.type = type;
        balance = 0;
        cards = new ArrayList<>();
        transactions = new ArrayList<>();
        commerciants = new Commerciant();
        createCoupons();
        totalSpentPerCommerciant = 0.0;
    }

    /**
     * Initializes the coupons map for the account and populates it with default
     * categories and their respective discount rates.
     * This is typically called during the construction of a {@code ClassicAccount}
     * instance to ensure the account has a predefined set of coupon categories
     * with initial discount values of 0.0.
     */
    private void createCoupons() {
        coupons = new HashMap<>();
        coupons.put("Food", 0.0);
        coupons.put("Clothes", 0.0);
        coupons.put("Tech", 0.0);
    }

    /**
     * Changes the interest rate for the account.
     *
     * @param interest the new interest rate to be set for the account.
     */
    public void changeInterest(final double interest) {
    }

    /**
     * Adds the current interest rate to the account balance.
     */
    public void addInterest() {
    }

    /**
     * Retrieves the International Bank Account Number (IBAN) of this account.
     *
     * @return the IBAN of the account.
     */
    public String getIban() {
        return iban;
    }

    /**
     * Sets the International Bank Account Number (IBAN) for this account.
     *
     * @param iban the new IBAN to be set for the account.
     */
    public void setIban(final String iban) {
        this.iban = iban;
    }

    /**
     * Retrieves the current balance of the account.
     *
     * @return the current account balance.
     */
    public double getBalance() {
        return balance;
    }

    /**
     * Updates the current balance of the account.
     *
     * @param currentBalance the new balance to be set for the account.
     */
    public void setBalance(final double currentBalance) {
        this.balance = currentBalance;
    }

    /**
     * Retrieves the currency of the account.
     *
     * @return the currency of the account.
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * Sets the currency of the account.
     *
     * @param currency the new currency to be set for the account (e.g., "USD", "EUR").
     */
    public void setCurrency(final String currency) {
        this.currency = currency;
    }

    /**
     * Retrieves the type of the account.
     *
     * @return the account type (e.g., "Savings", "Current").
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the type of the account.
     *
     * @param type the new type to be set for the account.
     */
    public void setType(final String type) {
        this.type = type;
    }

    /**
     * Retrieves the list of cards associated with the account.
     *
     * @return an {@code ArrayList} of {@code Card} objects associated with the account.
     */
    public ArrayList<Card> getCards() {
        return cards;
    }

    /**
     * Sets the list of cards associated with the account.
     *
     * @param cards an {@code ArrayList} of {@code Card} objects to associate with the account.
     */
    public void setCards(final ArrayList<Card> cards) {
        this.cards = cards;
    }

    /**
     * Retrieves the minimum balance required for the account.
     *
     * @return the minimum balance of the account.
     */
    public double getMinBalance() {
        return minBalance;
    }

    /**
     * Sets the minimum balance required for the account.
     *
     * @param minBalance the new minimum balance to be set for the account.
     */
    public void setMinBalance(final double minBalance) {
        this.minBalance = minBalance;
    }

    /**
     * Retrieves the list of transactions associated with the account.
     *
     * @return an {@code ArrayList} of {@code TransactionStrategy} objects representing
     * the account transactions.
     */
    public ArrayList<TransactionStrategy> getTransactions() {
        return transactions;
    }

    /**
     * Sets the list of transactions associated with the account.
     *
     * @param transactions an {@code ArrayList} of {@code TransactionStrategy}
     *                     objects to associate with the account.
     */
    public void setTransactions(final ArrayList<TransactionStrategy> transactions) {
        this.transactions = transactions;
    }

    /**
     * Retrieves the {@code Commerciant} object associated with the account.
     *
     * @return the {@code Commerciant} object associated with the account.
     */
    public Commerciant getCommerciants() {
        return commerciants;
    }

    /**
     * Sets the {@code Commerciant} object associated with the account.
     *
     * @param commerciants the new {@code Commerciant} object to associate with the account.
     */
    public void setCommerciants(final Commerciant commerciants) {
        this.commerciants = commerciants;
    }

    /**
     * Retrieves the current coupons and their respective discount rates associated with the account
     * The map contains categories as keys and their corresponding discount rates as values.
     *
     * @return a {@code Map<String, Double>} where the keys are coupon categories and the values
     * are the associated discount rates.
     */
    public Map<String, Double> getCoupons() {
        return coupons;
    }

    /**
     * Retrieves the total amount spent per commerciant associated with the account.
     *
     * @return the total amount spent per commerciant as a {@code double}.
     */
    public double getTotalSpentPerCommerciant() {
        return totalSpentPerCommerciant;
    }

    /**
     * Sets the total amount spent per commerciant associated with the account.
     *
     * @param totalSpentPerCommerciant the total amount spent per commerciant
     *                                 as a {@code double}.
     */
    public void setTotalSpentPerCommerciant(final double totalSpentPerCommerciant) {
        this.totalSpentPerCommerciant = totalSpentPerCommerciant;
    }
}

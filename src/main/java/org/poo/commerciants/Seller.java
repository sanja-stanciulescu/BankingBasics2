package org.poo.commerciants;

import org.poo.accounts.ClassicAccount;
import org.poo.fileio.CommerciantInput;

import java.util.HashMap;
import java.util.Map;

public class Seller {
    private String commerciant;
    private String type;
    private int id;
    private String iban;

    private Map<ClassicAccount, Integer> numberOfTransactions;
    private CashbackStrategy cashbackStrategy;
    private String cashbackType;

    /**
     * Constructs a new Seller object with the given parameters.
     *
     * @param commerciant the name of the seller or business
     * @param type the type of the seller or business
     * @param cashbackType the type of cashback strategy associated with the seller
     * @param cashbackStrategy the cashback strategy implementation to be used
     */
    public Seller(
            final String commerciant,
            final String type,
            final String cashbackType,
            final CashbackStrategy cashbackStrategy
    ) {
        this.commerciant = commerciant;
        this.type = type;
        this.cashbackType = cashbackType;
        this.cashbackStrategy = cashbackStrategy;
        numberOfTransactions = new HashMap<>();
    }

    /**
     * Constructs a Seller instance based on the provided CommerciantInput data.
     *
     * @param commerciantInput the input data containing seller information including the
     *                         commerciant name, type, id, account, and cashback strategy
     */
    public Seller(final CommerciantInput commerciantInput) {
        this.commerciant = commerciantInput.getCommerciant();
        this.type = commerciantInput.getType();
        this.cashbackType = commerciantInput.getCashbackStrategy();
        this.id = commerciantInput.getId();
        this.iban = commerciantInput.getAccount();

        if (commerciantInput.getCashbackStrategy().equals("spendingThreshold")) {
            this.cashbackStrategy = new AmountThresholdCashback();
        } else if (commerciantInput.getCashbackStrategy().equals("nrOfTransactions")) {
            this.cashbackStrategy = new NumberOfTransactionsCashback();
        }

        numberOfTransactions = new HashMap<>();
    }

    /**
     * Retrieves the name or identifier of the commerciant associated with the seller.
     *
     * @return the commerciant as a string
     */
    public String getCommerciant() {
        return commerciant;
    }

    /**
     * Sets the commerciant name for the seller.
     *
     * @param commerciant the name of the commerciant to set
     */
    public void setCommerciant(final String commerciant) {
        this.commerciant = commerciant;
    }

    /**
     * Retrieves the type of the seller.
     *
     * @return the type of the seller as a String
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the type of the seller.
     *
     * @param type the type to be set
     */
    public void setType(final String type) {
        this.type = type;
    }

    /**
     * Retrieves the cashback strategy associated with the seller.
     *
     * @return the cashback strategy currently set for the seller
     */
    public CashbackStrategy getCashbackStrategy() {
        return cashbackStrategy;
    }

    /**
     * Sets the cashback strategy for the seller.
     *
     * @param cashbackStrategy the implementation of CashbackStrategy to be assigned to the seller
     */
    public void setCashbackStrategy(final CashbackStrategy cashbackStrategy) {
        this.cashbackStrategy = cashbackStrategy;
    }

    /**
     * Retrieves the unique identifier of the seller.
     *
     * @return the ID of the seller
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the identifier for the seller.
     *
     * @param id the identifier to be set
     */
    public void setId(final int id) {
        this.id = id;
    }

    /**
     * Retrieves the IBAN (International Bank Account Number) associated with the seller.
     *
     * @return a {@code String} representing the IBAN of the seller.
     */
    public String getIban() {
        return iban;
    }

    /**
     * Sets the IBAN (International Bank Account Number) for the seller.
     *
     * @param iban the IBAN to be set for the seller
     */
    public void setIban(final String iban) {
        this.iban = iban;
    }

    /**
     * Retrieves the mapping of ClassicAccount objects to the number of transactions
     * associated with each account.
     *
     * @return a Map where the key is a ClassicAccount and the value is an Integer
     * representing the number of transactions for that account.
     */
    public Map<ClassicAccount, Integer> getNumberOfTransactions() {
        return numberOfTransactions;
    }

    /**
     * Retrieves the type of cashback associated with the seller.
     *
     * @return the cashback type as a string
     */
    public String getCashbackType() {
        return cashbackType;
    }
}

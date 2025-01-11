package org.poo.accounts;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class SavingsAccount extends ClassicAccount {
    @JsonIgnore
    private double interest;

    /**
     * Constructs a new {@code SavingsAccount} instance with the specified IBAN,
     * currency, account type, and interest rate.
     *
     * @param iban      the International Bank Account Number (IBAN) of the account.
     * @param currency  the currency of the account.
     * @param type      the type of the account (e.g., "Savings", "Classic").
     * @param interest  the interest rate associated with the account.
     */
    public SavingsAccount(
            final String iban,
            final String currency,
            final String type,
            final double interest
    ) {
        super(iban, currency, type);
        this.interest = interest;
    }

    /**
     * Updates the interest rate of the savings account.
     *
     * @param interestRate the new interest rate to be set for the account.
     */
    @Override
    public void changeInterest(final double interestRate) {
        interest = interestRate;
    }

    /**
     * Adds the accrued interest to the account balance.
     * The interest is calculated based on the current balance and the interest rate.
     */
    @Override
    public void addInterest() {
        double initialBalance = balance;
        balance = initialBalance + initialBalance * interest;
    }

    /**
     * Retrieves the interest rate of the savings account.
     *
     * @return the interest rate of the account.
     */
    public double getInterest() {
        return interest;
    }

    /**
     * Updates the interest rate of the savings account.
     *
     * @param interest the new interest rate to be set.
     */
    public void setInterest(final double interest) {
        this.interest = interest;
    }
}

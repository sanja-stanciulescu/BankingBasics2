package org.poo.transactions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.poo.app.Finder;
import org.poo.exchangeRates.Bnr;
import org.poo.fileio.CommandInput;

import java.util.ArrayList;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SplitPaymentTransaction implements TransactionStrategy {
    private String description;
    private int timestamp;
    private double amount;
    private String currency;
    private ArrayList<String> involvedAccounts;
    private String error;

    @JsonIgnore
    private CommandInput command;
    @JsonIgnore
    private ArrayList<Finder> finders;
    @JsonIgnore
    private Bnr bank;
    @JsonIgnore
    private boolean everythingOk;

    /**
     * Constructs a new {@code SplitPaymentTransaction} with the given command input,
     * list of finders, and bank for exchange rate lookup.
     *
     * @param command the command input containing the transaction details.
     * @param finders the list of finders representing accounts and users involved in the split.
     * @param bank the bank used for exchange rates.
     */
    public SplitPaymentTransaction(
            final CommandInput command,
            final ArrayList<Finder> finders,
            final Bnr bank
    ) {
        this.command = command;
        this.finders = finders;
        this.bank = bank;
        error = null;
        this.timestamp = command.getTimestamp();
        this.involvedAccounts = new ArrayList<>();
    }

    /**
     * Executes the transaction by splitting the amount among the involved accounts.
     * It checks if all accounts have sufficient funds and converts the amount to the appropriate
     * currency before deducting the amounts from the accounts.
     */
    public void makeTransaction() {
        amount = command.getAmount() / finders.size();
        currency = command.getCurrency();
        description = "Split payment of " + String.format("%.2f",
                                                          command.getAmount()) + " " + currency;
        involvedAccounts.addAll(command.getAccounts());
        checkAccounts(finders);

        if (everythingOk) {
            for (Finder finder : finders) {
               double actualAmount = convertAmount(finders, finder);
               finder.getAccount().setBalance(finder.getAccount().getBalance() - actualAmount);
            }
        }
    }

    /**
     * Checks whether all accounts involved in the split payment are valid and have sufficient
     * balance.
     * If any account is invalid or lacks sufficient funds, an error is set.
     *
     * @param finders the list of finders representing the involved accounts.
     */
    private void checkAccounts(final ArrayList<Finder> finders) {
        everythingOk = true;
        for (Finder finder : finders) {
            if (finder.getUser() == null || finder.getAccount() == null) {
                System.out.println("Lipseste cnv");
                everythingOk = false;
            }
            double amount = convertAmount(finders, finder);
            if (Double.compare(finder.getAccount().getBalance(), amount) < 0) {
                error = "Account " + finder.getAccount().getIban()
                        + " has insufficient funds for a split payment.";
            }
            finder.getUser().getTransactions().add(this);
            finder.getAccount().getTransactions().add(this);
        }

        if (error != null) {
            everythingOk = false;
        }
    }

    /**
     * Converts the amount to the appropriate currency based on the account's currency.
     *
     * @param finders the list of finders involved in the transaction.
     * @param finder the specific finder for which the amount is being converted.
     * @return the converted amount for the given finder.
     */
    private double convertAmount(final ArrayList<Finder> finders, final Finder finder) {
        double amount;
        if (!command.getCurrency().equals(finder.getAccount().getCurrency())) {
            double exchangeRate = bank.getExchangeRate(command.getCurrency(),
                                                        finder.getAccount().getCurrency());
            amount = command.getAmount() / finders.size() * exchangeRate;
        } else {
            amount = command.getAmount() / finders.size();
        }
        return amount;
    }

    /**
     * Gets the description of the transaction.
     *
     * @return the description of the transaction.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description for the transaction.
     *
     * @param description the description to set.
     */
    public void setDescription(final String description) {
        this.description = description;
    }

    /**
     * Gets the timestamp of the transaction.
     *
     * @return the timestamp of the transaction.
     */
    public int getTimestamp() {
        return timestamp;
    }

    /**
     * Sets the timestamp for the transaction.
     *
     * @param timestamp the timestamp to set.
     */
    public void setTimestamp(final int timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Gets the amount involved in the transaction.
     *
     * @return the amount of the transaction.
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Sets the amount involved in the transaction.
     *
     * @param amount the amount to set.
     */
    public void setAmount(final double amount) {
        this.amount = amount;
    }

    /**
     * Gets the currency of the transaction.
     *
     * @return the currency of the transaction.
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * Sets the currency for the transaction.
     *
     * @param currency the currency to set.
     */
    public void setCurrency(final String currency) {
        this.currency = currency;
    }

    /**
     * Gets the list of involved account IBANs.
     *
     * @return the list of involved accounts.
     */
    public ArrayList<String> getInvolvedAccounts() {
        return involvedAccounts;
    }

    /**
     * Sets the list of involved accounts.
     *
     * @param involvedAccounts the list of involved accounts to set.
     */
    public void setInvolvedAccounts(final ArrayList<String> involvedAccounts) {
        this.involvedAccounts = involvedAccounts;
    }

    /**
     * Gets the error message related to the transaction, if any.
     *
     * @return the error message or null if no error occurred.
     */
    public String getError() {
        return error;
    }

    /**
     * Sets the error message for the transaction.
     *
     * @param error the error message to set.
     */
    public void setError(final String error) {
        this.error = error;
    }
}

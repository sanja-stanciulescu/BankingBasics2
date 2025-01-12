package org.poo.transactions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.poo.accounts.ClassicAccount;
import org.poo.accounts.SavingsAccount;
import org.poo.app.IBANRegistry;
import org.poo.fileio.CommandInput;
import org.poo.users.User;
import org.poo.utils.Utils;

public class AddAccountTransaction implements TransactionStrategy {
    private int timestamp;
    private String description;

    @JsonIgnore
    private CommandInput command;
    @JsonIgnore
    private User currentUser;
    @JsonIgnore
    private IBANRegistry registry;

    /**
     * Constructs a new {@code AddAccountTransaction} based on the given command input,
     * IBAN registry, and user.
     *
     * @param command the command input containing details about the account type and currency.
     * @param registry the IBAN registry used to register the new account's IBAN.
     * @param currentUser the user who will receive the new account.
     */
    public AddAccountTransaction(
            final CommandInput command,
            final IBANRegistry registry,
            final User currentUser
    ) {
        this.timestamp = command.getTimestamp();
        this.command = command;
        this.registry = registry;
        this.currentUser = currentUser;
    }

    /**
     * Executes the transaction, which includes creating a new account
     * (either a {@link ClassicAccount} or {@link SavingsAccount}), registering the IBAN
     * and adding the account to the user's list of accounts.
     */
    @Override
    public void makeTransaction() {
        String iban = "";
        if (command.getAccountType().equals("classic")) {
            iban = Utils.generateIBAN();
            String currency = command.getCurrency();
            currentUser.getAccounts().add(new ClassicAccount(iban, currency, "classic"));
            currentUser.getAccounts().getLast().getTransactions().add(this);
        } else if (command.getAccountType().equals("savings")) {
            iban = Utils.generateIBAN();
            String currency = command.getCurrency();
            double interest = command.getInterestRate();
            currentUser.getAccounts().add(new SavingsAccount(iban, currency, "savings", interest));
            currentUser.setNumberOfSavingsAccounts(currentUser.getNumberOfSavingsAccounts() + 1);
        }
        description = "New account created";
        registry.registerIBAN(iban, iban);
        currentUser.getTransactions().add(this);
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
     * Gets the command input that initiated the transaction.
     *
     * @return the command input.
     */
    public CommandInput getCommand() {
        return command;
    }

    /**
     * Sets the command input that initiated the transaction.
     *
     * @param command the command input to set.
     */
    public void setCommand(final CommandInput command) {
        this.command = command;
    }
}

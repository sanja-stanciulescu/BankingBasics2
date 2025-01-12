package org.poo.transactions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.poo.accounts.ClassicAccount;
import org.poo.app.IBANRegistry;
import org.poo.fileio.CommandInput;

public class SetAliasTransaction implements TransactionStrategy {
    private String description;
    private int timestamp;

    @JsonIgnore
    private CommandInput command;
    private ClassicAccount account;
    private IBANRegistry registry;

    /**
     * Constructs a new {@code SetAliasTransaction} with the given command input,
     * alias registry, and the account to update.
     *
     * @param command the command input containing transaction details.
     * @param registry the registry to update the alias in.
     * @param account the account for which the alias will be set.
     */
    public SetAliasTransaction(
            final CommandInput command,
            final IBANRegistry registry,
            final ClassicAccount account) {
        this.command = command;
        this.registry = registry;
        this.account = account;
        this.description = command.getDescription();
        this.timestamp = command.getTimestamp();
    }

    /**
     * Executes the transaction by updating the alias for the specified account.
     * If the alias update fails, an error message is printed to the console.
     */
    public void makeTransaction() {
        if (account == null || registry == null) {
            System.out.println("Account or IBAN registry is null!");
            return;
        }
       if (!registry.updateAlias(account.getIban(), command.getAlias())) {
           System.out.println("Could not update the alias");
       }
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
     * Gets the command input containing transaction details.
     *
     * @return the command input for the transaction.
     */
    public CommandInput getCommand() {
        return command;
    }

    /**
     * Sets the command input for the transaction.
     *
     * @param command the command input to set.
     */
    public void setCommand(final CommandInput command) {
        this.command = command;
    }

    /**
     * Gets the account for which the alias is being set.
     *
     * @return the account for the alias transaction.
     */
    public ClassicAccount getAccount() {
        return account;
    }

    /**
     * Sets the account for the alias transaction.
     *
     * @param account the account to set.
     */
    public void setAccount(final ClassicAccount account) {
        this.account = account;
    }
}

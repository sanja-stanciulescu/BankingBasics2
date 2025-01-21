package org.poo.transactions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.accounts.ClassicAccount;
import org.poo.fileio.CommandInput;
import org.poo.users.User;

public class AddInterestTransaction implements TransactionStrategy {
    private double amount;
    private String currency;
    private String description;
    private int timestamp;

    @JsonIgnore
    private CommandInput command;
    @JsonIgnore
    private ClassicAccount account;
    @JsonIgnore
    private User user;
    @JsonIgnore
    private ArrayNode output;

    /**
     * Constructs a new {@code AddInterestTransaction} based on the given command,
     * output, and account.
     *
     * @param command the command input containing the details of the transaction,
     *                including the interest to add.
     * @param output the output array to store the transaction results.
     * @param account the account to which the interest will be added.
     */
    public AddInterestTransaction(
            final CommandInput command,
            final ArrayNode output,
            final User user,
            final ClassicAccount account
    ) {
        this.command = command;
        this.output = output;
        this.account = account;
        this.user = user;
        this.timestamp = command.getTimestamp();
    }

    /**
     * Executes the transaction by adding interest to the account if it is a "savings" account.
     * If the account is not a savings account, an error message is added to the output.
     */
    public void makeTransaction() {
        if (account == null) {
            System.out.println("Could not change interest rate");
            return;
        }
        if (account.getType().equals("classic")) {
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode node = mapper.createObjectNode();
            node.put("command", command.getCommand());
            node.put("timestamp", timestamp);

            ObjectNode errorNode = mapper.createObjectNode();
            errorNode.put("description", "This is not a savings account");
            errorNode.put("timestamp", timestamp);

            node.set("output", errorNode);
            output.add(node);

            return;
        }

        double initialBalance = account.getBalance();
        account.addInterest();
        amount = account.getBalance() - initialBalance;
        currency = account.getCurrency();
        description = "Interest rate income";
        user.getTransactions().add(this);
        account.getTransactions().add(this);
    }

    /**
     * Gets the timestamp of the transaction.
     *
     * @return the timestamp of the transaction.
     */
    @Override
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
     * Retrieves the amount associated with the transaction.
     *
     * @return the amount of the transaction as a double.
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Sets the transaction amount for this operation.
     *
     * @param amount the amount to set for the transaction.
     */
    public void setAmount(final double amount) {
        this.amount = amount;
    }

    /**
     * Retrieves the currency associated with the transaction.
     *
     * @return the currency as a string.
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * Sets the currency for the transaction.
     *
     * @param currency the currency to set, represented as a string (e.g., "USD", "EUR").
     */
    public void setCurrency(final String currency) {
        this.currency = currency;
    }

    /**
     * Retrieves the description of the transaction.
     *
     * @return the description of the transaction as a string.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description for the transaction.
     *
     * @param description the description of the transaction to set.
     */
    public void setDescription(final String description) {
        this.description = description;
    }
}

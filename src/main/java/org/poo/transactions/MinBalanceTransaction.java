package org.poo.transactions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.accounts.ClassicAccount;
import org.poo.fileio.CommandInput;
import org.poo.users.User;

public class MinBalanceTransaction implements TransactionStrategy {
    private String description;
    private int timestamp;

    @JsonIgnore
    private CommandInput command;
    private User currentUser;
    private ClassicAccount account;
    private ArrayNode output;

    /**
     * Constructs a new {@code MinBalanceTransaction} with the given command, output,
     * user, and account.
     *
     * @param command the command input containing the transaction details.
     * @param output the output to store the results of the transaction.
     * @param currentUser the user performing the transaction.
     * @param account the account for which the minimum balance will be set.
     */
    public MinBalanceTransaction(
            final CommandInput command,
            final ArrayNode output,
            final User currentUser,
            final ClassicAccount account
    ) {
        this.command = command;
        this.output = output;
        this.currentUser = currentUser;
        this.account = account;
        this.description = command.getDescription();
        this.timestamp = command.getTimestamp();
    }

    /**
     * Executes the transaction by setting the minimum balance of the account.
     * If the user or account is invalid, an error is added to the output.
     */
    public void makeTransaction() {
        if (currentUser == null || account == null) {
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode balanceNode = mapper.createObjectNode();
            balanceNode.put("command", command.getCommand());
            balanceNode.put("timestamp", timestamp);
            balanceNode.put("error", "Invalid user");
        } else {
            account.setMinBalance(command.getAmount());
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
     * Sets the description of the transaction.
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
     * Gets the command input associated with the transaction.
     *
     * @return the command input.
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
     * Gets the output associated with the transaction.
     *
     * @return the output.
     */
    public ArrayNode getOutput() {
        return output;
    }

    /**
     * Sets the output for the transaction.
     *
     * @param output the output to set.
     */
    public void setOutput(final ArrayNode output) {
        this.output = output;
    }
}

package org.poo.transactions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.accounts.ClassicAccount;
import org.poo.fileio.CommandInput;
import org.poo.users.User;

public class ChangeInterestTransaction implements TransactionStrategy {
    private String description;
    private int timestamp;

    @JsonIgnore
    private CommandInput command;
    @JsonIgnore
    private User user;
    @JsonIgnore
    private ClassicAccount account;
    @JsonIgnore
    private ArrayNode output;

    /**
     * Constructs a new {@code ChangeInterestTransaction} with the given command,
     * output, user, account, and timestamp.
     *
     * @param command the command input containing the details of the transaction,
     *                including the new interest rate.
     * @param output the output array where the transaction result will be stored.
     * @param user the user who owns the account.
     * @param account the account whose interest rate will be changed.
     */
    public ChangeInterestTransaction(
            final CommandInput command,
            final ArrayNode output,
            final User user,
            final ClassicAccount account
    ) {
        this.command = command;
        this.output = output;
        this.user = user;
        this.account = account;
        this.timestamp = command.getTimestamp();
        description = "Interest rate of the account changed to " + command.getInterestRate();
    }

    /**
     * Executes the transaction by changing the interest rate of the account
     * if it is not a "classic" account.
     * If the account is a "classic" account, an error message is added to the output.
     */
    public void makeTransaction() {
        if (user == null || account == null) {
            System.out.println("Could not change interest rate");
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
        account.changeInterest(command.getInterestRate());
        account.getTransactions().add(this);
        user.getTransactions().add(this);
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
}

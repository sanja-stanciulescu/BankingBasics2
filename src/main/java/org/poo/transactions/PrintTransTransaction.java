package org.poo.transactions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.CommandInput;
import org.poo.users.User;

public class PrintTransTransaction implements TransactionStrategy {
    private CommandInput command;
    private int timestamp;
    private User user;
    private ArrayNode output;

    /**
     * Constructs a new {@code PrintTransTransaction} with the given command, output, and user.
     *
     * @param command the command input containing transaction details.
     * @param output the output to store the result of the transaction.
     * @param user the user whose transactions will be printed.
     */
    public PrintTransTransaction(
            final CommandInput command,
            final ArrayNode output,
            final User user
    ) {
        this.command = command;
        this.user = user;
        this.output = output;
        this.timestamp = command.getTimestamp();
    }

    /**
     * Executes the transaction by gathering all transactions from the user and adding them
     * to the output in a structured format.
     */
    public void makeTransaction() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode transactionNode = mapper.createObjectNode();
        transactionNode.put("command", command.getCommand());
        transactionNode.put("timestamp", timestamp);

        ArrayNode printNode = mapper.createArrayNode();
        for (TransactionStrategy transaction : user.getTransactions()) {
            ObjectNode node = mapper.convertValue(transaction, ObjectNode.class);
            printNode.add(node);
        }
        transactionNode.set("output", printNode);
        output.add(transactionNode);
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
     * Gets the user associated with the transaction.
     *
     * @return the user associated with the transaction.
     */
    public User getUser() {
        return user;
    }

    /**
     * Sets the user associated with the transaction.
     *
     * @param user the user to set.
     */
    public void setUser(final User user) {
        this.user = user;
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

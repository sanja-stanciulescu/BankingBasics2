package org.poo.transactions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.accounts.ClassicAccount;
import org.poo.fileio.CommandInput;

public class AddInterestTransaction implements TransactionStrategy {
    private CommandInput command;
    private ClassicAccount account;
    private ArrayNode output;
    private int timestamp;

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
            final ClassicAccount account
    ) {
        this.command = command;
        this.output = output;
        this.account = account;
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
        account.addInterest();
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
}

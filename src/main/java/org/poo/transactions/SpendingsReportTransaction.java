package org.poo.transactions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.accounts.ClassicAccount;
import org.poo.fileio.CommandInput;

public class SpendingsReportTransaction implements TransactionStrategy {
    private CommandInput command;
    private ClassicAccount account;
    private ArrayNode output;
    private int timestamp;

    /**
     * Constructs a new {@code SpendingsReportTransaction} with the given command input,
     * output node, and account.
     *
     * @param command the command input containing transaction details.
     * @param output the output node where the result is added.
     * @param account the account for which the spending report will be generated.
     */
    public SpendingsReportTransaction(
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
     * Executes the transaction by generating the spending report for the account.
     * If the account is invalid or not a classic account, an error message is returned.
     */
    public void makeTransaction() {
        if (account == null) {
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode node = mapper.createObjectNode();
            node.put("command", command.getCommand());
            node.put("timestamp", timestamp);

            ObjectNode errorNode = mapper.createObjectNode();
            errorNode.put("description", "Account not found");
            errorNode.put("timestamp", timestamp);

            node.set("output", errorNode);
            output.add(node);

            return;
        }
        if (account.getType().equals("savings")) {
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode node = mapper.createObjectNode();
            node.put("command", command.getCommand());
            node.put("timestamp", timestamp);

            ObjectNode errorNode = mapper.createObjectNode();
            errorNode.put("error", "This kind of report is not supported for a saving account");

            node.set("output", errorNode);
            output.add(node);

            return;
        }
        ObjectNode node = ReportTransaction.gatherData(command, account);
        output.add(node);
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

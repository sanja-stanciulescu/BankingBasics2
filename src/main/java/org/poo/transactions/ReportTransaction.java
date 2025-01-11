package org.poo.transactions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.accounts.ClassicAccount;
import org.poo.fileio.CommandInput;

import java.util.Map;
import java.util.TreeMap;

public class ReportTransaction implements TransactionStrategy {
    private CommandInput command;
    private ClassicAccount account;
    private ArrayNode output;
    private int timestamp;

    /**
     * Constructs a new {@code ReportTransaction} with the given command, output, and account.
     *
     * @param command the command input containing transaction details.
     * @param output the output to store the result of the transaction.
     * @param account the account to generate the report for.
     */
    public ReportTransaction(
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
     * Executes the transaction by generating a report for the specified account.
     * If the account is not found, an error message is added to the output.
     * If the account is valid, the gathered data is added to the output.
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

        ObjectNode node = gatherData(command, account);
        output.add(node);
    }

    /**
     * Gathers the data for the report, including balance, currency, IBAN,
     * transactions, and optionally commerciant spending data based on the command.
     *
     * @param command the command input containing the details of the report.
     * @param account the account for which the report is generated.
     * @return an {@code ObjectNode} containing the gathered data for the report.
     */
    public static ObjectNode gatherData(
            final CommandInput command,
            final ClassicAccount account
    ) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("timestamp", command.getTimestamp());
        node.put("command", command.getCommand());

        ObjectNode outputNode = mapper.createObjectNode();
        outputNode.put("balance", account.getBalance());
        outputNode.put("currency", account.getCurrency());
        outputNode.put("IBAN", account.getIban());

        ArrayNode transactionsNode = mapper.createArrayNode();
        TreeMap<String, Double> sortedCommerciants = new TreeMap<>();

        // Gather transaction data based on the command type (report or spendingsReport)
        if (command.getCommand().equals("report")) {
            for (TransactionStrategy transaction : account.getTransactions()) {
                if (transaction.getTimestamp() >= command.getStartTimestamp()
                        && transaction.getTimestamp() <= command.getEndTimestamp()) {
                    ObjectNode transNode = mapper.convertValue(transaction, ObjectNode.class);
                    transactionsNode.add(transNode);
                }
            }
        } else {
            for (PayOnlineTransaction transaction : account.getCommerciants().getPayments()) {
                if (transaction.getTimestamp() >= command.getStartTimestamp()
                        && transaction.getTimestamp() <= command.getEndTimestamp()) {
                    ObjectNode transNode = mapper.convertValue(transaction, ObjectNode.class);
                    transactionsNode.add(transNode);

                    sortedCommerciants.put(transaction.getCommerciant(),
                            sortedCommerciants.getOrDefault(transaction.getCommerciant(), 0.0)
                                                            + transaction.getAmount());
                }
            }
        }
        outputNode.set("transactions", transactionsNode);

        // If the command is "spendingsReport", gather spending data for each commerciant
        if (command.getCommand().equals("spendingsReport")) {
            ArrayNode commerciantsNode = mapper.createArrayNode();
            for (Map.Entry<String, Double> entry : sortedCommerciants.entrySet()) {
                ObjectNode payNode = mapper.createObjectNode();
                payNode.put("commerciant", entry.getKey());
                payNode.put("total", entry.getValue());
                commerciantsNode.add(payNode);
            }
            outputNode.set("commerciants", commerciantsNode);
        }

        node.set("output", outputNode);

        return node;
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

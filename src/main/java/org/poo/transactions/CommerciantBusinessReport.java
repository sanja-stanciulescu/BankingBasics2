package org.poo.transactions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.accounts.BusinessAccount;
import org.poo.business.BusinessCommerciant;
import org.poo.fileio.CommandInput;

import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class CommerciantBusinessReport implements TransactionStrategy {
    private CommandInput command;
    private BusinessAccount account;
    private ArrayNode output;
    private int timestamp;

    /**
     * Constructs a new {@code CommerciantBusinessReport} instance.
     *
     * @param command the input command containing details such as transaction type,
     *                user information, target account, and other relevant attributes.
     * @param account the business account associated with the transaction or report generation.
     * @param output  the output data structure (e.g., JSON array) to store the result of
     *                the report generation or transaction operations.
     */
    public CommerciantBusinessReport(
            final CommandInput command,
            final BusinessAccount account,
            final ArrayNode output
    ) {
        this.command = command;
        this.account = account;
        this.output = output;
    }

    /**
     * Processes a transaction based on the current state of the business account
     * and generates an appropriate output. This method involves handling cases
     * where the account is null, extracting account details, and creating a comprehensive
     * JSON output for the transaction, including business commerciant details.
     *
     * When the account is null, it creates an error entry with the description
     * "Account not found" and includes the current timestamp.
     *
     * If the account exists, the method retrieves detailed attributes of the account
     * (such as balance, currency, IBAN, deposit limit, spending limit) and adds
     * statistics specific to the commerciant associated with the account.
     * The list of business commerciants is sorted alphabetically by their name and
     * embedded into the output JSON.
     *
     * The output is stored in the {@code output} field in the form of nested
     * JSON structures, adhering to a specific format based on the transaction command.
     */
    @Override
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

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("timestamp", command.getTimestamp());
        node.put("command", command.getCommand());

        ObjectNode outputNode = mapper.createObjectNode();
        outputNode.put("balance", account.getBalance());
        outputNode.put("currency", account.getCurrency());
        outputNode.put("IBAN", account.getIban());
        outputNode.put("deposit limit", account.getDepositLimit());
        outputNode.put("spending limit", account.getSpendingLimit());
        outputNode.put("statistics type", "commerciant");

        ArrayNode commerciantsNode = mapper.createArrayNode();
        List<BusinessCommerciant> sortedEmployees = account.getBusinessCommerciants().values()
                .stream()
                .collect(Collectors
                        .toCollection(() ->
                                new TreeSet<BusinessCommerciant>(
                                        Comparator.comparing(
                                                BusinessCommerciant::getCommerciant
                                        )
                                )
                        )
                ).stream().toList();

        for (BusinessCommerciant comm : sortedEmployees) {
            ObjectNode employeeNode = mapper.convertValue(comm, ObjectNode.class);
            commerciantsNode.add(employeeNode);
        }

        outputNode.set("commerciants", commerciantsNode);
        node.set("output", outputNode);

        output.add(node);
    }

    /**
     * Retrieves the command associated with this business report.
     *
     * @return the {@code CommandInput} object containing the details of the command.
     */
    public CommandInput getCommand() {
        return command;
    }

    /**
     * Sets the command input for the current instance.
     * This method updates the command associated with the instance by
     * assigning the provided {@code command} parameter to the internal field.
     *
     * @param command the {@code CommandInput} object containing the details
     *                of the command to be set.
     */
    public void setCommand(final CommandInput command) {
        this.command = command;
    }

    /**
     * Retrieves the associated business account.
     *
     * @return the {@code BusinessAccount} instance linked to the report.
     */
    public BusinessAccount getAccount() {
        return account;
    }

    /**
     * Updates the business account associated with the CommerciantBusinessReport.
     *
     * @param account the {@code BusinessAccount} to be set as the current account.
     */
    public void setAccount(final BusinessAccount account) {
        this.account = account;
    }

    /**
     * Retrieves the output data represented as an {@code ArrayNode}.
     *
     * @return the output data contained within this instance.
     */
    public ArrayNode getOutput() {
        return output;
    }

    /**
     * Sets the output data for a transaction or operation.
     *
     * @param output the {@code ArrayNode} containing the output data to be associated
     *               with the current transaction or operation.
     */
    public void setOutput(final ArrayNode output) {
        this.output = output;
    }

    /**
     * Retrieves the timestamp of the transaction.
     *
     * @return the timestamp indicating when the transaction occurred.
     */
    @Override
    public int getTimestamp() {
        return timestamp;
    }

    /**
     * Updates the timestamp for the current instance.
     * The timestamp typically represents the specific time at which a transaction
     * or event occurred. This value can be used for tracking, sorting, or other
     * time-related functionalities.
     *
     * @param timestamp the timestamp to set, represented as an integer. It is
     *                  expected to correspond to a standardized or application-specific
     *                  time format (e.g., UNIX timestamp, application-defined epoch).
     */
    public void setTimestamp(final int timestamp) {
        this.timestamp = timestamp;
    }
}

package org.poo.transactions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.accounts.BusinessAccount;
import org.poo.business.Employee;
import org.poo.business.Manager;
import org.poo.fileio.CommandInput;

import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class TransactionBusinessReport implements TransactionStrategy {
    private CommandInput command;
    private BusinessAccount account;
    private ArrayNode output;
    private int timestamp;

    /**
     * Constructs a TransactionBusinessReport instance that generates a business transaction
     * report based on the provided parameters.
     *
     * @param command the {@code CommandInput} object containing transaction details such
     *                as command type, account information, and transaction specifics.
     * @param account the {@code BusinessAccount} object representing the business account
     *                associated with the transaction report.
     * @param output  the {@code ArrayNode} object that will store the output result
     *                of the transaction report creation process.
     */
    public TransactionBusinessReport(
            final CommandInput command,
            final BusinessAccount account,
            final ArrayNode output
    ) {
        this.command = command;
        this.account = account;
        this.output = output;
    }

    /**
     * Processes the transaction based on the details provided by the {@code command}
     * and {@code account}.
     * Outputs the transaction details including account balance, currency, limits,
     * and associated employees and managers in a structured JSON format to the {@code output}.
     *
     * If the associated {@code account} is null, an error message with the description
     * "Account not found" is added to the {@code output}.
     *
     * The transaction details include the following:
     * - Account balance, currency, IBAN, deposit limit, and spending limit.
     * - Total deposited and total spent amounts.
     * - A hierarchical list of associated employees and managers, sorted by their order value.
     *
     * Steps involved:
     * 1. If the {@code account} is null, logs an error message.
     * 2. If the {@code account} is present, retrieves and structures the account details.
     * 3. Employees and managers are individually sorted and added to the structured JSON.
     * 4. Outputs the structured JSON into the {@code output}.
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
        outputNode.put("statistics type", "transaction");
        outputNode.put("total deposited", account.getTotalDeposited());
        outputNode.put("total spent", account.getTotalSpent());

        ArrayNode employeesNode = mapper.createArrayNode();
        List<Employee> sortedEmployees = account.getEmployees().values()
                .stream().collect(Collectors.toCollection(() -> new TreeSet<Employee>((e1, e2) -> {
                    if (e1.getOrder() > e2.getOrder()) {
                        return 1;
                    } else if (e1.getOrder() < e2.getOrder()) {
                        return -1;
                    }
                    return 0;
                })))
                .stream().toList();
        for (Employee employee : sortedEmployees) {
            ObjectNode employeeNode = mapper.convertValue(employee, ObjectNode.class);
            employeesNode.add(employeeNode);
        }

        ArrayNode managersNode = mapper.createArrayNode();
        List<Manager> sortedManagers = account.getManagers().values()
                .stream().collect(Collectors.toCollection(() -> new TreeSet<Manager>((m1, m2) -> {
                    if (m1.getOrder() > m2.getOrder()) {
                        return 1;
                    } else if (m1.getOrder() < m2.getOrder()) {
                        return -1;
                    }
                    return 0;
                })))
                .stream().toList();
        for (Manager manager : sortedManagers) {
            ObjectNode managerNode = mapper.convertValue(manager, ObjectNode.class);
            managersNode.add(managerNode);
        }

        outputNode.set("employees", employeesNode);
        outputNode.set("managers", managersNode);
        node.set("output", outputNode);

        output.add(node);
    }

    /**
     * Retrieves the command associated with the transaction.
     *
     * @return the {@code CommandInput} object representing the command details.
     */
    public CommandInput getCommand() {
        return command;
    }

    /**
     * Sets the command input for the transaction.
     *
     * @param command the {@code CommandInput} instance containing the details of the command.
     */
    public void setCommand(final CommandInput command) {
        this.command = command;
    }

    /**
     * Retrieves the business account associated with this transaction report.
     *
     * @return the {@code BusinessAccount} instance linked to this transaction report.
     */
    public BusinessAccount getAccount() {
        return account;
    }

    /**
     * Sets the business account associated with this transaction.
     *
     * @param account the {@code BusinessAccount} to associate with this transaction
     */
    public void setAccount(final BusinessAccount account) {
        this.account = account;
    }

    /**
     * Retrieves the output as an {@code ArrayNode}.
     *
     * @return the output represented as an {@code ArrayNode}.
     */
    public ArrayNode getOutput() {
        return output;
    }

    /**
     * Updates the output of the transaction business report.
     *
     * @param output the new {@code ArrayNode} to set as the output of the transaction
     *               business report.
     */
    public void setOutput(final ArrayNode output) {
        this.output = output;
    }

    /**
     * Returns the timestamp associated with the transaction.
     *
     * @return the timestamp of the transaction as an integer.
     */
    @Override
    public int getTimestamp() {
        return timestamp;
    }

    /**
     * Updates the timestamp of the transaction.
     *
     * @param timestamp the timestamp representing the time of the transaction as an
     *                  integer value.
     */
    public void setTimestamp(final int timestamp) {
        this.timestamp = timestamp;
    }
}

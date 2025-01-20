package org.poo.transactions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.accounts.BusinessAccount;
import org.poo.business.Employee;
import org.poo.business.Manager;
import org.poo.fileio.CommandInput;

import java.util.Map;

public class TransactionBusinessReport implements TransactionStrategy{
    private CommandInput command;
    private BusinessAccount account;
    private ArrayNode output;
    private int timestamp;

    public TransactionBusinessReport(CommandInput command, BusinessAccount account, ArrayNode output) {
        this.command = command;
        this.account = account;
        this.output = output;
    }

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
        for (Map.Entry<String, Employee> entry : account.getEmployees().entrySet()) {
            ObjectNode employeeNode = mapper.convertValue(entry.getValue(), ObjectNode.class);
            employeesNode.add(employeeNode);
        }

        ArrayNode managersNode = mapper.createArrayNode();
        for (Map.Entry<String, Manager> entry : account.getManagers().entrySet()) {
            ObjectNode managerNode = mapper.convertValue(entry.getValue(), ObjectNode.class);
            managersNode.add(managerNode);
        }

        outputNode.set("employees", employeesNode);
        outputNode.set("managers", managersNode);
        node.set("output", outputNode);

        output.add(node);
    }

    public CommandInput getCommand() {
        return command;
    }

    public void setCommand(CommandInput command) {
        this.command = command;
    }

    public BusinessAccount getAccount() {
        return account;
    }

    public void setAccount(BusinessAccount account) {
        this.account = account;
    }

    public ArrayNode getOutput() {
        return output;
    }

    public void setOutput(ArrayNode output) {
        this.output = output;
    }

    @Override
    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }
}

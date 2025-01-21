package org.poo.transactions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.accounts.BusinessAccount;
import org.poo.business.BusinessCommerciant;
import org.poo.business.Employee;
import org.poo.business.Manager;
import org.poo.fileio.CommandInput;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class CommerciantBusinessReport implements TransactionStrategy {
    private CommandInput command;
    private BusinessAccount account;
    private ArrayNode output;
    private int timestamp;

    public CommerciantBusinessReport(CommandInput command, BusinessAccount account, ArrayNode output) {
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
        outputNode.put("statistics type", "commerciant");

        ArrayNode commerciantsNode = mapper.createArrayNode();
        List<BusinessCommerciant> sortedEmployees = account.getBusinessCommerciants().values()
                .stream().collect(Collectors.toCollection(() -> new TreeSet<BusinessCommerciant>(Comparator.comparing(BusinessCommerciant::getCommerciant))))
                .stream().toList();
        for (BusinessCommerciant comm : sortedEmployees) {
            ObjectNode employeeNode = mapper.convertValue(comm, ObjectNode.class);
            commerciantsNode.add(employeeNode);
        }

        outputNode.set("commerciants", commerciantsNode);
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

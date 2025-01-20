package org.poo.transactions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.accounts.BusinessAccount;
import org.poo.fileio.CommandInput;
import org.poo.users.User;

public class ChangeSpendingLimitTransaction implements TransactionStrategy {
    private int timestamp;
    private String error;
    private ArrayNode output;

    @JsonIgnore
    private User user;
    @JsonIgnore
    private BusinessAccount account;
    @JsonIgnore
    private CommandInput command;

    public ChangeSpendingLimitTransaction(final CommandInput command, final User user, final BusinessAccount account, final ArrayNode output) {
        this.command = command;
        this.user = user;
        this.account = account;
        this.output = output;
        this.timestamp = command.getTimestamp();
    }

    @Override
    public void makeTransaction() {
        if (user != account.getOwner().getUser()) {
            CheckCardStatusTransaction.printError(command, "You must be owner in order to change spending limit.",
                    timestamp, output);
            return;
        }
        if (command.getCommand().equals("changeSpendingLimit"))
            account.setSpendingLimit(command.getAmount());
        else if (command.getCommand().equals("changeDepositLimit"))
            account.setDepositLimit(command.getAmount());
    }

    @Override
    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}

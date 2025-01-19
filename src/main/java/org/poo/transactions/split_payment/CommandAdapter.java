package org.poo.transactions.split_payment;

import org.poo.transactions.TransactionStrategy;

public class CommandAdapter implements TransactionStrategy {
    private Command command;

    public CommandAdapter(Command command) {
        this.command = command;
    }

    @Override
    public void makeTransaction() {
        command.execute();
    }

    @Override
    public int getTimestamp() {
        return command.getTimestamp();
    }
}

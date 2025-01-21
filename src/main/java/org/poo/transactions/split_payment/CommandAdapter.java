package org.poo.transactions.split_payment;

import org.poo.transactions.TransactionStrategy;

public class CommandAdapter implements TransactionStrategy {
    private Command command;

    /**
     * Constructs a new {@code CommandAdapter} object by wrapping the given {@code Command}.
     *
     * @param command the {@code Command} object to be adapted and executed using this adapter.
     */
    public CommandAdapter(final Command command) {
        this.command = command;
    }

    /**
     * Executes the transaction logic by delegating it to the encapsulated command's
     * {@code execute} method. The specific behavior of the transaction is determined
     * by the command implementation. This method is designed to comply with the
     * {@code TransactionStrategy} contract.
     */
    @Override
    public void makeTransaction() {
        command.execute();
    }

    /**
     * Retrieves the timestamp of the transaction by delegating to the associated command.
     * The timestamp typically represents when the transaction occurred or was created.
     *
     * @return the timestamp value as an integer.
     */
    @Override
    public int getTimestamp() {
        return command.getTimestamp();
    }
}

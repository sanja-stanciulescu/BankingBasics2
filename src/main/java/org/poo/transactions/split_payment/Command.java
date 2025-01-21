package org.poo.transactions.split_payment;

public interface Command {
    /**
     * Executes the operation defined by the implementing class.
     * The specific behavior of this method is determined by the implementation
     * and represents the core functionality or action of the command.
     */
    void execute();
    /**
     * Retrieves the timestamp associated with this command.
     * The timestamp typically represents the time at which the command is executed or created.
     *
     * @return the timestamp value as an integer.
     */
    int getTimestamp();
}

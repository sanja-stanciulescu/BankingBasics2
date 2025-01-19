package org.poo.transactions.split_payment;

public interface Command {
    void execute();
    int getTimestamp();
}

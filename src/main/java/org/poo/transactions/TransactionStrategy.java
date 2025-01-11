package org.poo.transactions;

/**
 * The {@code TransactionStrategy} interface defines the contract for different types of
 * transactions in a banking system.
 * It requires the implementation of methods to perform a transaction and retrieve
 * the timestamp of the transaction.
 */
public interface TransactionStrategy {
    /**
     * Executes the transaction logic. This method should contain the steps to
     * process the transaction based on the specific implementation.
     */
    void makeTransaction();

    /**
     * Retrieves the timestamp of the transaction.
     * The timestamp represents when the transaction occurred
     * and can be used to track or sort transactions.
     *
     * @return the timestamp of the transaction.
     */
    int getTimestamp();
}

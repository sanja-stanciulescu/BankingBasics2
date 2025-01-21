package org.poo.transactions.split_payment;

public class AcceptSplitPayment implements Command {
    private String email;
    private int timestamp;
    private SplitPaymentTransaction transaction;

    /**
     * Constructor for the AcceptSplitPayment command, initializing the user's email
     * and the associated split payment transaction.
     *
     * @param email the email address of the user approving the split payment
     * @param transaction the split payment transaction to be approved
     */
    public AcceptSplitPayment(
            final String email,
            final SplitPaymentTransaction transaction
    ) {
        this.email = email;
        this.transaction = transaction;
    }

    /**
     * Executes the command to approve the user associated with the email
     * in the context of the provided split payment transaction.
     *
     * If the transaction object is null, this method logs a message and
     * exits without performing further actions. Otherwise, it retrieves
     * the transaction's timestamp and approves the user for the transaction.
     *
     * The method interacts with the associated {@code SplitPaymentTransaction}
     * by invoking the {@code getTimestamp} method to fetch the transaction
     * timestamp, and the {@code approveUser} method to approve the user
     * identified by the email address stored in this command.
     */
    @Override
    public void execute() {
        if (transaction == null) {
            return;
        }
        timestamp = transaction.getTimestamp();
        transaction.approveUser(email);
    }

    /**
     * Retrieves the email associated with this object.
     *
     * @return the email address as a String
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address associated with this command.
     *
     * @param email the email address to be set
     */
    public void setEmail(final String email) {
        this.email = email;
    }

    /**
     * Retrieves the timestamp associated with this instance.
     *
     * @return the timestamp as an integer value
     */
    public int getTimestamp() {
        return timestamp;
    }

    /**
     * Sets the timestamp for the command.
     *
     * @param timestamp the timestamp value to be set
     */
    public void setTimestamp(final int timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Retrieves the current SplitPaymentTransaction associated with this command.
     *
     * @return the current SplitPaymentTransaction instance, or null if no transaction is set
     */
    public SplitPaymentTransaction getTransaction() {
        return transaction;
    }

    /**
     * Updates the transaction associated with the split payment.
     *
     * @param transaction the SplitPaymentTransaction to set
     */
    public void setTransaction(final SplitPaymentTransaction transaction) {
        this.transaction = transaction;
    }
}

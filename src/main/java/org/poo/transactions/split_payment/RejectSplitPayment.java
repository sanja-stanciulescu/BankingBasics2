package org.poo.transactions.split_payment;

public class RejectSplitPayment implements Command {
    private String email;
    private int timestamp;
    private SplitPaymentTransaction transaction;

    /**
     * Constructs a new instance of RejectSplitPayment to reject a specified split payment
     * transaction for a given user identified by their email address.
     *
     * @param email the email address of the user whose payment participation is to be rejected
     * @param transaction the split payment transaction to be rejected
     */
    public RejectSplitPayment(
            final String email,
            final SplitPaymentTransaction transaction
    ) {
        this.email = email;
        this.transaction = transaction;
    }

    /**
     * Executes the rejection of a user associated with a split payment transaction.
     * If the transaction is null, the method will log an informative message and terminate.
     * Otherwise, it retrieves the timestamp from the transaction and invokes the rejection
     * logic for the specified email within the transaction.
     */
    @Override
    public void execute() {
        if (transaction == null) {
            return;
        }
        timestamp = transaction.getTimestamp();
        transaction.rejectUser(email);
    }

    /**
     * Retrieves the email associated with this RejectSplitPayment command.
     *
     * @return the email as a String.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Updates the email associated with the RejectSplitPayment command.
     *
     * @param email the new email address to be set
     */
    public void setEmail(final String email) {
        this.email = email;
    }

    /**
     * Retrieves the timestamp associated with this instance.
     * The timestamp typically represents the time of a specific event or action.
     *
     * @return the timestamp value as an integer.
     */
    public int getTimestamp() {
        return timestamp;
    }

    /**
     * Sets the timestamp for this command.
     *
     * @param timestamp the timestamp value to set, typically representing the time
     *                  at which the command is executed or related to the associated transaction.
     */
    public void setTimestamp(final int timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Retrieves the current SplitPaymentTransaction associated with this command.
     *
     * @return the SplitPaymentTransaction object representing the transaction.
     */
    public SplitPaymentTransaction getTransaction() {
        return transaction;
    }

    /**
     * Sets the split payment transaction associated with this command.
     *
     * @param transaction the SplitPaymentTransaction object to be associated
     *                    with this command
     */
    public void setTransaction(final SplitPaymentTransaction transaction) {
        this.transaction = transaction;
    }
}

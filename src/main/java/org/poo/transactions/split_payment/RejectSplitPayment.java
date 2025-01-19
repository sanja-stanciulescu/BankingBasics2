package org.poo.transactions.split_payment;

public class RejectSplitPayment implements Command {
    private String email;
    private int timestamp;
    private SplitPaymentTransaction transaction;

    public RejectSplitPayment(String email, SplitPaymentTransaction transaction) {
        this.email = email;
        this.transaction = transaction;
    }

    @Override
    public void execute() {
        if (transaction == null) {
            System.out.println("Transaction is null");
            return;
        }
        timestamp = transaction.getTimestamp();
        transaction.rejectUser(email);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public SplitPaymentTransaction getTransaction() {
        return transaction;
    }

    public void setTransaction(SplitPaymentTransaction transaction) {
        this.transaction = transaction;
    }
}

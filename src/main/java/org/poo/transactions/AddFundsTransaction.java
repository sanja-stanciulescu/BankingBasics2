package org.poo.transactions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.poo.accounts.BusinessAccount;
import org.poo.accounts.ClassicAccount;
import org.poo.fileio.CommandInput;

public class AddFundsTransaction implements TransactionStrategy {
    private int timestamp;

    @JsonIgnore
    private ClassicAccount currentAccount;
    @JsonIgnore
    private String email;
    private CommandInput command;

    /**
     * Constructs a new {@code AddFundsTransaction} based on the given command and account.
     *
     * @param command the command input containing the details of the transaction,
     *                such as the amount to add.
     * @param currentAccount the account to which the funds will be added.
     */
    public AddFundsTransaction(final CommandInput command, final ClassicAccount currentAccount) {
        this.currentAccount = currentAccount;
        this.command = command;
        timestamp = command.getTimestamp();
        email = command.getEmail();
    }

    /**
     * Executes the transaction by adding the specified amount to the current account's balance.
     */
    public void makeTransaction() {
        if (currentAccount == null) {
            System.out.println("User not found.");
            return;
        }

        if (currentAccount.getType().equals("business")) {
            BusinessAccount business = (BusinessAccount) currentAccount;
            if (business.getEmployees().containsKey(email)) {
                if (command.getAmount() > business.getDepositLimit()) {
                    return;
                }
                business.getEmployees().get(email).setDeposited(business.getEmployees().get(email).getDeposited() + command.getAmount());
                business.setTotalDeposited(business.getTotalDeposited() + command.getAmount());
            } else if (business.getManagers().containsKey(email)) {
                business.getManagers().get(email).setDeposited(business.getManagers().get(email).getDeposited() + command.getAmount());
                business.setTotalDeposited(business.getTotalDeposited() + command.getAmount());
            }
        }

        currentAccount.setBalance(currentAccount.getBalance() + command.getAmount());
    }

    /**
     * Gets the timestamp of the transaction.
     *
     * @return the timestamp of the transaction.
     */
    public int getTimestamp() {
        return timestamp;
    }

    /**
     * Sets the timestamp for the transaction.
     *
     * @param timestamp the timestamp to set.
     */
    public void setTimestamp(final int timestamp) {
        this.timestamp = timestamp;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

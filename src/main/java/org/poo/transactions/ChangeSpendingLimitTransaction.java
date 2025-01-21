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

    /**
     * Constructs a new {@code ChangeSpendingLimitTransaction} object used to handle a transaction
     * to change the spending or deposit limit of a business account.
     *
     * @param command the {@code CommandInput} containing the details of the transaction, such as
     *                the type of command, timestamp, and associated limits or amounts
     * @param user the {@code User} performing the transaction, typically the account owner
     * @param account the {@code BusinessAccount} for which the spending or deposit limit is
     *                being changed
     * @param output an {@code ArrayNode} used to store the result or any error messages generated
     *               during the transaction
     */
    public ChangeSpendingLimitTransaction(
            final CommandInput command,
            final User user,
            final BusinessAccount account,
            final ArrayNode output
    ) {
        this.command = command;
        this.user = user;
        this.account = account;
        this.output = output;
        this.timestamp = command.getTimestamp();
    }

    /**
     * Executes a transaction to either change the spending limit or deposit limit of the account.
     *
     * This method performs the following operations:
     * 1. Validates whether the user attempting to perform the transaction is the owner of
     * the account.
     *    - If not, an error message is logged to the output, and the transaction terminates.
     *    - Specific error messages are provided based on the operation being performed.
     * 2. If the user is the owner, modifies the corresponding account limit based on the command:
     *    - Updates the spending limit when the command is "changeSpendingLimit".
     *    - Updates the deposit limit when the command is "changeDepositLimit".
     *
     * If the command provided in the transaction is unsupported, no action is taken.
     *
     * Preconditions:
     * - The `command`, `user`, `account`, and `output` fields must be properly initialized in the
     *   containing object before invoking this method.
     * - The `command` must contain a valid timestamp and supported command string
     * ("changeSpendingLimit" or "changeDepositLimit").
     */
    @Override
    public void makeTransaction() {
        if (user != account.getOwner().getUser()) {
            if (command.getCommand().equals("changeSpendingLimit")) {
                CheckCardStatusTransaction.printError(command,
                        "You must be owner in order to change spending limit.", timestamp, output);
            } else if (command.getCommand().equals("changeDepositLimit")) {
                CheckCardStatusTransaction.printError(command,
                        "You must be owner in order to change deposit limit.", timestamp, output);
            }
            return;
        }

        if (command.getCommand().equals("changeSpendingLimit")) {
            account.setSpendingLimit(command.getAmount());
        } else if (command.getCommand().equals("changeDepositLimit")) {
            account.setDepositLimit(command.getAmount());
        }
    }

    /**
     * Retrieves the timestamp of the transaction.
     *
     * @return the timestamp of the transaction.
     */
    @Override
    public int getTimestamp() {
        return timestamp;
    }

    /**
     * Sets the timestamp for the transaction.
     *
     * @param timestamp the timestamp to set, representing the point in time
     *                  when the transaction occurred
     */
    public void setTimestamp(final int timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Retrieves the error message associated with the transaction.
     *
     * @return the error message as a String, or null if no error is present.
     */
    public String getError() {
        return error;
    }

    /**
     * Sets the error message associated with the transaction.
     *
     * @param error the error message to be set for this transaction
     */
    public void setError(final String error) {
        this.error = error;
    }
}

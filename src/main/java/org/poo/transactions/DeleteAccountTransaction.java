package org.poo.transactions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.accounts.ClassicAccount;
import org.poo.fileio.CommandInput;
import org.poo.users.User;

public class DeleteAccountTransaction implements TransactionStrategy {
    private String description;
    private int timestamp;

    @JsonIgnore
    private CommandInput command;
    @JsonIgnore
    private User currentUser;
    @JsonIgnore
    private ClassicAccount account;
    @JsonIgnore
    private ArrayNode output;
    @JsonIgnore
    private String email;

    /**
     * Constructs a new {@code DeleteAccountTransaction} with the given command, output, and user.
     *
     * @param command the command input containing the transaction details.
     * @param output the output array where the transaction result will be stored.
     * @param currentUser the user who owns the account to be deleted.
     */
    public DeleteAccountTransaction(
            final CommandInput command,
            final ArrayNode output,
            final User currentUser
    ) {
        this.command = command;
        this.currentUser = currentUser;
        this.output = output;
        this.description = command.getDescription();
        this.timestamp = command.getTimestamp();
    }

    /**
     * Executes the transaction by attempting to delete the specified account.
     * If the account has a balance, it cannot be deleted, and an error message
     * is added to the output.
     * If the account balance is zero, the account and its associated cards are removed,
     * and a success message is added.
     */
    public void makeTransaction() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode cardNode = mapper.createObjectNode();
        cardNode.put("command", command.getCommand());
        cardNode.put("timestamp", timestamp);
        ObjectNode outputNode = mapper.createObjectNode();

        int idx = searchAccount(currentUser, command.getAccount());
        if (idx == -1) {
            outputNode.put("error", "Account couldn't be deleted -"
                    + " see org.poo.transactions for details");
        } else {
            ClassicAccount wantedAccount = currentUser.getAccounts().get(idx);
            if (wantedAccount.getBalance() == 0) {
                wantedAccount.getCards().clear();
                currentUser.getAccounts().remove(idx);
                outputNode.put("success", "Account deleted");
                outputNode.put("timestamp", timestamp);
            } else {
                outputNode.put("error", "Account couldn't be deleted -"
                        + " see org.poo.transactions for details");
                outputNode.put("timestamp", timestamp);
                description = "Account couldn't be deleted - there are funds remaining";
                account.getTransactions().add(this);
                currentUser.getTransactions().add(this);
            }
        }
        cardNode.set("output", outputNode);
        output.add(cardNode);
    }

    /**
     * Searches for an account with the specified IBAN in the given user.
     *
     * @param user the user to search for the account.
     * @param iban the IBAN of the account to search for.
     * @return the index of the account if found, or -1 if not found.
     */
    private int searchAccount(final User user, final String iban) {
        if (user == null) {
            return -1;
        }

        for (int i = 0; i < user.getAccounts().size(); i++) {
            if (user.getAccounts().get(i).getIban().equals(iban)) {
                account = user.getAccounts().get(i);
                return i;
            }
        }
        return -1;
    }

    /**
     * Gets the description of the transaction.
     *
     * @return the description of the transaction.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the transaction.
     *
     * @param description the description to set.
     */
    public void setDescription(final String description) {
        this.description = description;
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

    /**
     * Gets the command input associated with the transaction.
     *
     * @return the command input.
     */
    public CommandInput getCommand() {
        return command;
    }

    /**
     * Sets the command input for the transaction.
     *
     * @param command the command input to set.
     */
    public void setCommand(final CommandInput command) {
        this.command = command;
    }

    /**
     * Gets the output array where the results of the transaction are stored.
     *
     * @return the output array.
     */
    public ArrayNode getOutput() {
        return output;
    }

    /**
     * Sets the output array for the transaction.
     *
     * @param output the output array to set.
     */
    public void setOutput(final ArrayNode output) {
        this.output = output;
    }

    /**
     * Gets the account associated with the transaction.
     *
     * @return the account.
     */
    public ClassicAccount getAccount() {
        return account;
    }

    /**
     * Sets the account for the transaction.
     *
     * @param account the account to set.
     */
    public void setAccount(final ClassicAccount account) {
        this.account = account;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

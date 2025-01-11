package org.poo.transactions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.accounts.ClassicAccount;
import org.poo.cards.Card;
import org.poo.fileio.CommandInput;
import org.poo.users.User;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CheckCardStatusTransaction implements TransactionStrategy {
    private String description;
    private Integer timestamp;

    @JsonIgnore
    private CommandInput command;
    @JsonIgnore
    private User user;
    @JsonIgnore
    private ClassicAccount currentAccount;
    @JsonIgnore
    private Card card;
    @JsonIgnore
    private ArrayNode output;

    /**
     * Constructs a new {@code CheckCardStatusTransaction} with the given command,
     * output, user, account, and card.
     *
     * @param command the command input containing the transaction details.
     * @param output the output array where the transaction result will be stored.
     * @param user the user who owns the account and card.
     * @param currentAccount the account associated with the card.
     * @param card the card whose status will be checked and updated.
     */
    public CheckCardStatusTransaction(
            final CommandInput command,
            final ArrayNode output,
            final User user,
            final ClassicAccount currentAccount,
            final Card card
    ) {
        this.command = command;
        this.output = output;
        this.user = user;
        this.currentAccount = currentAccount;
        this.card = card;
    }

    /**
     * Executes the transaction by checking the account balance and updating the card status.
     * If the balance is below the minimum required, the card status is set to "frozen".
     * If the balance is low but above the minimum threshold, the card status is set to "warning".
     * If the account or card is null, an error message is added to the output.
     */
    public void makeTransaction() {
        if (currentAccount == null || card == null) {
            printError(command, command.getTimestamp(), output);
        } else {
         if (currentAccount.getBalance() <= currentAccount.getMinBalance()) {
             description = "You have reached the minimum amount of funds, the card will be frozen";
             timestamp = command.getTimestamp();
             card.setStatus("frozen");
             user.getTransactions().add(this);
         } else if (currentAccount.getBalance() - currentAccount.getMinBalance() <= 30) {
             description = "The card is in a warning stage";
             timestamp = command.getTimestamp();
             card.setStatus("warning");
             user.getTransactions().add(this);
         }
        }
    }

    /**
     * Adds an error message to the output if the card is not found.
     *
     * @param command the command input containing the details of the transaction.
     * @param timestamp the timestamp of the transaction.
     * @param output the output array where the error message will be added.
     */
    static void printError(
            final CommandInput command,
            final int timestamp,
            final ArrayNode output
    ) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode cardNode = mapper.createObjectNode();
        cardNode.put("command", command.getCommand());
        cardNode.put("timestamp", timestamp);

        ObjectNode errorNode = mapper.createObjectNode();
        errorNode.put("description", "Card not found");
        errorNode.put("timestamp", command.getTimestamp());
        cardNode.set("output", errorNode);
        output.add(cardNode);
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
    @Override
    public int getTimestamp() {
        if (timestamp != null) {
            return timestamp;
        }
        return -1;
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
     * Gets the card associated with the transaction.
     *
     * @return the card.
     */
    public Card getCard() {
        return card;
    }

    /**
     * Sets the card for the transaction.
     *
     * @param card the card to set.
     */
    public void setCard(final Card card) {
        this.card = card;
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
     * Gets the user associated with the transaction.
     *
     * @return the user.
     */
    public User getUser() {
        return user;
    }

    /**
     * Sets the user for the transaction.
     *
     * @param user the user to set.
     */
    public void setUser(final User user) {
        this.user = user;
    }
}

package org.poo.transactions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.accounts.BusinessAccount;
import org.poo.accounts.ClassicAccount;
import org.poo.cards.Card;
import org.poo.fileio.CommandInput;
import org.poo.users.User;

import java.util.ArrayList;

public class PrintUserTransaction implements TransactionStrategy {
    private String command;
    private int timestamp;
    private ArrayList<User> allUsers;
    private ArrayNode output;

    /**
     * Constructs a new {@code PrintUserTransaction} with the given command, output,
     * and list of all users.
     *
     * @param command the command input containing transaction details.
     * @param output the output to store the result of the transaction.
     * @param allUsers the list of all users whose details will be printed.
     */
    public PrintUserTransaction(
            final CommandInput command,
            final ArrayNode output,
            final ArrayList<User> allUsers
    ) {
        this.command = command.getCommand();
        this.timestamp = command.getTimestamp();
        this.output = output;
        this.allUsers = allUsers;
    }

    /**
     * Executes the transaction by gathering details of all users and adding them
     * to the output in a structured format.
     */
    @Override
    public void makeTransaction() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode printUsersNode = mapper.createObjectNode();
        printUsersNode.put("command", command);
        printUsersNode.put("timestamp", timestamp);

        ArrayNode usersArray = mapper.createArrayNode();
        for (User user : allUsers) {
            ObjectNode userNode = mapper.createObjectNode();

            userNode.put("firstName", user.getFirstName());
            userNode.put("lastName", user.getLastName());
            userNode.put("email", user.getEmail());

            ArrayNode accountsArray = mapper.createArrayNode();
            for (ClassicAccount account : user.getAccounts()) {
                if (account.getType().equals("business")) {
                    BusinessAccount business = (BusinessAccount) account;
                    if (business.getOwner().getUser() != user) {
                        continue;
                    }
                }
                ObjectNode accountNode = mapper.createObjectNode();
                accountNode.put("IBAN", account.getIban());
                accountNode.put("balance", account.getBalance());
                accountNode.put("currency", account.getCurrency());
                accountNode.put("type", account.getType());

                ArrayNode cardsArray = mapper.createArrayNode();
                for (Card card : account.getCards()) {
                    ObjectNode cardNode = mapper.convertValue(card, ObjectNode.class);
                    cardsArray.add(cardNode);
                }
                accountNode.set("cards", cardsArray);
                accountsArray.add(accountNode);
            }
            userNode.set("accounts", accountsArray);

            usersArray.add(userNode);
        }
        printUsersNode.set("output", usersArray);
        output.add(printUsersNode);
    }

    /**
     * Gets the command input associated with the transaction.
     *
     * @return the command input.
     */
    public String getCommand() {
        return command;
    }

    /**
     * Sets the command input for the transaction.
     *
     * @param command the command input to set.
     */
    public void setCommand(final String command) {
        this.command = command;
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
     * Gets the output associated with the transaction.
     *
     * @return the output.
     */
    public ArrayNode getOutput() {
        return output;
    }

    /**
     * Sets the output for the transaction.
     *
     * @param output the output to set.
     */
    public void setOutput(final ArrayNode output) {
        this.output = output;
    }
}

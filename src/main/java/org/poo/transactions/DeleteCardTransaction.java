package org.poo.transactions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.poo.accounts.ClassicAccount;
import org.poo.fileio.CommandInput;
import org.poo.users.User;

public class DeleteCardTransaction implements TransactionStrategy {
    private String description;
    private int timestamp;
    private String account;
    private String card;
    private String cardHolder;


    @JsonIgnore
    private CommandInput command;
    @JsonIgnore
    private ClassicAccount currentAccount;
    @JsonIgnore
    private User currentUser;
    @JsonIgnore
    private String email;

    /**
     * Constructs a new {@code DeleteCardTransaction} with the given command, account, and user.
     *
     * @param command the command input containing the transaction details.
     * @param currentAccount the account from which the card will be deleted.
     * @param currentUser the user who owns the account.
     */
    public DeleteCardTransaction(
            final CommandInput command,
            final ClassicAccount currentAccount,
            final User currentUser
    ) {
        this.command = command;
        this.currentAccount = currentAccount;
        this.currentUser = currentUser;
        this.timestamp = command.getTimestamp();
    }

    /**
     * Constructs a new {@code DeleteCardTransaction} with the given description, timestamp,
     * account, card, and cardholder details.
     *
     * @param description the description of the transaction.
     * @param timestamp the timestamp of the transaction.
     * @param account the account associated with the transaction.
     * @param card the card to be deleted.
     * @param cardHolder the cardholder's email.
     */
    public DeleteCardTransaction(
            final String description,
            final int timestamp,
            final String account,
            final String card,
            final String cardHolder
    ) {
        this.description = description;
        this.timestamp = timestamp;
        this.account = account;
        this.card = card;
        this.cardHolder = cardHolder;
    }

    /**
     * Executes the transaction by attempting to delete the specified card from the account.
     * If the card is found, it is removed from the account. A description is added to the
     * transaction.
     */
    public void makeTransaction() {
        if (currentAccount != null) {
            int idx = pickCard(command.getCardNumber());
            if (idx == -1) {
                System.out.println("Card not found");
            } else {
                currentAccount.getCards().remove(idx);
                if (currentUser != null) {
                    currentUser.getTransactions().add(this);
                    description = "The card has been destroyed";
                    account = currentAccount.getIban();
                    card = command.getCardNumber();
                    cardHolder = currentUser.getEmail();
                }
            }
        }
    }

    /**
     * Searches for a card in the account's list of cards by the card number.
     *
     * @param cardNumber the card number to search for.
     * @return the index of the card if found, or -1 if not found.
     */
    private int pickCard(final String cardNumber) {
        for (int i = 0; i < currentAccount.getCards().size(); i++) {
            if (currentAccount.getCards().get(i).getCardNumber().equals(cardNumber)) {
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
     * Gets the account associated with the transaction.
     *
     * @return the account.
     */
    public String getAccount() {
        return account;
    }

    /**
     * Sets the account for the transaction.
     *
     * @param account the account to set.
     */
    public void setAccount(final String account) {
        this.account = account;
    }

    /**
     * Gets the card to be deleted.
     *
     * @return the card.
     */
    public String getCard() {
        return card;
    }

    /**
     * Sets the card for the transaction.
     *
     * @param card the card to set.
     */
    public void setCard(final String card) {
        this.card = card;
    }

    /**
     * Gets the card holder's email.
     *
     * @return the card holder's email.
     */
    public String getCardHolder() {
        return cardHolder;
    }

    /**
     * Sets the card holder's email.
     *
     * @param cardHolder the card holder's email to set.
     */
    public void setCardHolder(final String cardHolder) {
        this.cardHolder = cardHolder;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

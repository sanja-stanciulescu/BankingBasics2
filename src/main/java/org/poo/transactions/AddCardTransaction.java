package org.poo.transactions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.poo.accounts.ClassicAccount;
import org.poo.cards.Card;
import org.poo.cards.OneTimeCard;
import org.poo.fileio.CommandInput;
import org.poo.users.User;
import org.poo.utils.Utils;

public class AddCardTransaction implements TransactionStrategy {
    private int timestamp;
    private String description;
    private String cardHolder;
    private String card;
    private String account;

    @JsonIgnore
    private CommandInput command;
    @JsonIgnore
    private String email;
    @JsonIgnore
    private User currentUser;

    /**
     * Constructs a new {@code AddCardTransaction} based on the given command input and user.
     * This constructor is used when adding a card based on a command.
     *
     * @param command the command input containing details about the card and the account.
     * @param currentUser the user to whom the card will be added.
     */
    public AddCardTransaction(final CommandInput command, final User currentUser) {
        this.command = command;
        this.currentUser = currentUser;
        timestamp = command.getTimestamp();
        email = command.getEmail();
    }

    /**
     * Constructs a new {@code AddCardTransaction} with explicit details for adding a card.
     *
     * @param account the account to which the card is being added.
     * @param card the card number to be associated with the account.
     * @param cardHolder the holder of the card.
     * @param description a description of the transaction.
     * @param timestamp the timestamp of the transaction.
     */
    public AddCardTransaction(
            final String account,
            final String card,
            final String cardHolder,
            final String description,
            final int timestamp
    ) {
        this.account = account;
        this.card = card;
        this.cardHolder = cardHolder;
        this.description = description;
        this.timestamp = timestamp;
    }

    /**
     * Executes the transaction, which includes creating a new card and adding
     * it to the specified account.
     * If the user is incorrect or the account is not found, an appropriate description is set.
     */
    @Override
    public void makeTransaction() {
        int ret = searchIban(currentUser, command.getAccount());
        if (ret == 1) {
            account = command.getAccount();
            cardHolder = command.getEmail();
            description = "New card created";
            currentUser.getTransactions().add(this);
        } else {
            account = command.getAccount();
            cardHolder = command.getEmail();
            description = "Wrong user";
        }
    }

    /**
     * Searches for the given IBAN in the user's accounts.
     * If found, a new card is generated and added to the account.
     *
     * @param user the user whose accounts are being searched.
     * @param iban the IBAN of the account to search for.
     * @return 1 if the IBAN is found and the card is created, 0 otherwise.
     */
    private int searchIban(final User user, final String iban) {
        if (user == null) {
            return 0;
        }

        for (ClassicAccount currAccount : user.getAccounts()) {
            if (currAccount.getIban().equals(iban)) {
                String cardNumber = Utils.generateCardNumber();
                card = cardNumber;
                if (command.getCommand().equals("createCard")) {
                    currAccount.getCards().add(new Card(cardNumber, command.getEmail(), "active"));
                } else if (command.getCommand().equals("createOneTimeCard")) {
                    currAccount.getCards().add(new OneTimeCard(cardNumber, command.getEmail()));
                }
                    if (currAccount.getType().equals("classic")) {
                    currAccount.getTransactions().add(this);
                }

                return 1;
            }
        }

        return 0;
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
     * Gets the description of the transaction.
     *
     * @return the description of the transaction.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description for the transaction.
     *
     * @param description the description to set.
     */
    public void setDescription(final String description) {
        this.description = description;
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
     * @param cardHolder the email of the card holder.
     */
    public void setCardHolder(final String cardHolder) {
        this.cardHolder = cardHolder;
    }

    /**
     * Gets the card number associated with the transaction.
     *
     * @return the card number.
     */
    public String getCard() {
        return card;
    }

    /**
     * Sets the card number for the transaction.
     *
     * @param card the card number to set.
     */
    public void setCard(final String card) {
        this.card = card;
    }

    /**
     * Gets the account associated with the card creation.
     *
     * @return the account number.
     */
    public String getAccount() {
        return account;
    }

    /**
     * Sets the account number for the transaction.
     *
     * @param account the account number to set.
     */
    public void setAccount(final String account) {
        this.account = account;
    }

    /**
     * Retrieves the email associated with the transaction.
     *
     * @return the email of the user involved in the transaction.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email for the transaction.
     *
     * @param email the email address associated with the transaction
     */
    public void setEmail(final String email) {
        this.email = email;
    }
}

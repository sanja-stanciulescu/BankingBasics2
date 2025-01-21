package org.poo.cards;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.poo.users.User;

public class Card {
    protected String cardNumber;
    protected String status;

    @JsonIgnore
    private int checkedStatus;
    @JsonIgnore
    protected String creatorEmail;

    /**
     * Constructs a new {@code Card} with the specified card number and status.
     *
     * @param cardNumber the unique card number, must not be null or empty.
     * @param status     the status of the card (e.g., "active", "inactive").
     */
    public Card(final String cardNumber, final String email, final String status) {
        this.cardNumber = cardNumber;
        this.creatorEmail = email;
        this.status = status;
        checkedStatus = 0;
    }

    /**
     * Simulates the usage of the card for a transaction.
     *
     * @param account    the account associated with the card, must not be null or empty.
     * @param user       the user who owns the card, must not be null.
     * @param cardHolder the name of the cardholder, must not be null or empty.
     * @param timestamp  the timestamp of the transaction.
     * @return an integer representing the result of the card usage (currently always {@code 0}
     * because we do not wish to change the card).
     */
    public int useCard(
            final String account,
            final User user,
            final String cardHolder,
            final int timestamp
    ) {
        return 0;
    }

    /**
     * Returns the card number.
     *
     * @return the unique card number.
     */
    public String getCardNumber() {
        return cardNumber;
    }

    /**
     * Sets the card number.
     *
     * @param cardNumber the unique card number to set, must not be null or empty.
     */
    public void setCardNumber(final String cardNumber) {
        this.cardNumber = cardNumber;
    }

    /**
     * Returns the current status of the card.
     *
     * @return the status of the card (e.g., "active", "inactive").
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the status of the card.
     *
     * @param status the status to set (e.g., "active", "inactive"), must not be null or empty.
     */
    public void setStatus(final String status) {
        this.status = status;
    }

    /**
     * Retrieves the email address of the creator associated with this card.
     *
     * @return the email address of the card's creator.
     */
    public String getCreatorEmail() {
        return creatorEmail;
    }
}

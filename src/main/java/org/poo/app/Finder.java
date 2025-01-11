package org.poo.app;

import org.poo.accounts.ClassicAccount;
import org.poo.cards.Card;
import org.poo.users.User;

public class Finder {
    private User user;
    private ClassicAccount account;
    private Card card;

    /**
     * Retrieves the user associated with the current operation.
     *
     * @return the {@code User} object.
     */
    public User getUser() {
        return user;
    }

    /**
     * Sets the user associated with the current operation.
     *
     * @param user the {@code User} object to set.
     */
    public void setUser(final User user) {
        this.user = user;
    }

    /**
     * Retrieves the account associated with the current operation.
     *
     * @return the {@code ClassicAccount} object.
     */
    public ClassicAccount getAccount() {
        return account;
    }

    /**
     * Sets the account associated with the current operation.
     *
     * @param account the {@code ClassicAccount} object to set.
     */
    public void setAccount(final ClassicAccount account) {
        this.account = account;
    }

    /**
     * Retrieves the card associated with the current operation.
     *
     * @return the {@code Card} object.
     */
    public Card getCard() {
        return card;
    }

    /**
     * Sets the card associated with the current operation.
     *
     * @param card the {@code Card} object to set.
     */
    public void setCard(final Card card) {
        this.card = card;
    }
}

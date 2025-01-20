package org.poo.cards;

import org.poo.transactions.AddCardTransaction;
import org.poo.transactions.DeleteCardTransaction;
import org.poo.transactions.TransactionStrategy;
import org.poo.users.User;
import org.poo.utils.Utils;

public class OneTimeCard extends Card {
    /**
     * Constructs a new {@code OneTimeCard} with the specified card number.
     * The card is initialized with an "active" status.
     *
     * @param cardNumber the unique card number, must not be null or empty.
     */
    public OneTimeCard(final String cardNumber, final String email) {
        super(cardNumber, email, "active");
    }

    /**
     * Simulates the usage of the card for a transaction.
     * After being used, the card is destroyed and replaced with a new card.
     *
     * <p>This process involves:
     * <ul>
     *   <li>Recording a transaction indicating the card has been destroyed.</li>
     *   <li>Generating a new card number and assigning it to the card.</li>
     *   <li>Recording a transaction for the creation of the new card.</li>
     *   <li>Resetting the card's status to "active".</li>
     * </ul>
     *
     * @param account    the account associated with the card, must not be null or empty.
     * @param user       the user who owns the card, must not be null.
     * @param cardHolder the name of the cardholder, must not be null or empty.
     * @param timestamp  the timestamp of the transaction.
     * @return an integer indicating the result of the card usage (always {@code 1}).
     */
    @Override
    public int useCard(
            final String account,
            final User user,
            final String cardHolder,
            final int timestamp
    ) {
        TransactionStrategy trans = new DeleteCardTransaction(
                "The card has been destroyed",
                timestamp, account, cardNumber, cardHolder);
        user.getTransactions().add(trans);
        cardNumber = Utils.generateCardNumber();
        trans = new AddCardTransaction(
                account, cardNumber, cardHolder,
                "New card created", timestamp);
        user.getTransactions().add(trans);
        status = "active";

        return 1;
    }
}

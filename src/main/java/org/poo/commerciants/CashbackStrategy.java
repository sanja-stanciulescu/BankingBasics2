package org.poo.commerciants;

import org.poo.accounts.ClassicAccount;
import org.poo.users.User;

public interface CashbackStrategy {
    /**
     * Calculates the cashback amount for a given transaction based on the seller's strategy,
     * account details, user, and transaction amount.
     *
     * @param seller the seller associated with the transaction
     * @param account the account used for the transaction
     * @param user the user performing the transaction
     * @param transactionAmount the amount of the transaction
     * @return the calculated cashback amount
     */
    double calculateCashback(
            Seller seller,
            ClassicAccount account,
            User user,
            double transactionAmount);
}

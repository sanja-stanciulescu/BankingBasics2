package org.poo.commerciants;

import org.poo.accounts.ClassicAccount;
import org.poo.users.User;

public class NumberOfTransactionsCashback implements CashbackStrategy {

    /**
     * Calculates the cashback for a given transaction based on the seller's cashback strategy
     * and updates the seller's transaction count and account coupons accordingly.
     *
     * @param seller the seller performing the transaction, whose cashback strategy
     *               and transaction records are used
     * @param account the buyer's account involved in the transaction,
     *                which may be updated with coupons
     * @param user the user performing the transaction,
     *             whose details may influence the cashback calculation
     * @param transactionAmount the monetary amount of the transaction
     * @return the cashback amount for the transaction,
     * which is always 0.0 in the current implementation
     */
    public double calculateCashback(
            final Seller seller,
            final ClassicAccount account,
            final User user,
            final double transactionAmount
    ) {
        Integer transactions = seller.getNumberOfTransactions().get(account);

        if (seller.getCashbackType().equals("nrOfTransactions")) {
            if (transactions == null) {
                transactions = 1;
            } else {
                transactions = transactions + 1;
            }
        }
        seller.getNumberOfTransactions().put(account, transactions);

        if (transactions == null) {
            return 0;
        }
        if (transactions == 2) {
            if (account.getCoupons().get("Food") != -1) {
                account.getCoupons().put("Food", 0.02);
            }
        } else if (transactions == 5) {
            if (account.getCoupons().get("Clothes") != -1) {
                account.getCoupons().put("Clothes", 0.05);
            }
        } else if (transactions == 10) {
            if (account.getCoupons().get("Tech") != -1) {
                account.getCoupons().put("Tech", 0.1);
            }
        }
        return 0.0;
    }
}

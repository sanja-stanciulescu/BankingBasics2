package org.poo.commerciants;

import org.poo.accounts.ClassicAccount;
import org.poo.users.User;

public class AmountThresholdCashback implements CashbackStrategy {
    private static final int MIN_SPENT_AMOUNT = 100;
    private static final int MAX_SPENT_AMOUNT = 500;
    private static final int MID_SPENT_AMOUNT = 300;

    /**
     * Calculates the cashback amount for a given transaction based on the seller's cashback
     * strategy and the user's service plan.
     *
     * @param seller the seller involved in the transaction, containing cashback type
     *               and strategy information
     * @param account the user's account, used to calculate the total spending threshold
     * @param user the user making the transaction, whose service plan determines the cashback rate
     * @param transactionAmount the amount of the current transaction
     * @return the calculated cashback amount for the transaction
     */
    public double calculateCashback(
            final Seller seller,
            final ClassicAccount account,
            final User user,
            final double transactionAmount
    ) {
        double totalSpent = account.getTotalSpentPerCommerciant() + transactionAmount;
        double cashback = 0.0;
        if (totalSpent >= MAX_SPENT_AMOUNT) {
            if (user.getServicePlan().getPlan().equals("standard")
                    || user.getServicePlan().getPlan().equals("student")) {
                cashback = 0.0025 * transactionAmount;
            } else if (user.getServicePlan().getPlan().equals("silver")) {
                cashback = 0.005 * transactionAmount;
            } else if (user.getServicePlan().getPlan().equals("gold")) {
                cashback = 0.007 * transactionAmount;
            }
        } else if (totalSpent >= MID_SPENT_AMOUNT) {
            if (user.getServicePlan().getPlan().equals("standard")
                    || user.getServicePlan().getPlan().equals("student")) {
                cashback = 0.002 * transactionAmount;
            } else if (user.getServicePlan().getPlan().equals("silver")) {
                cashback = 0.004 * transactionAmount;
            } else if (user.getServicePlan().getPlan().equals("gold")) {
                cashback = 0.0055 * transactionAmount;
            }
        } else if (totalSpent >= MIN_SPENT_AMOUNT) {
            if (user.getServicePlan().getPlan().equals("standard")
                    || user.getServicePlan().getPlan().equals("student")) {
                cashback = 0.001 * transactionAmount;
            } else if (user.getServicePlan().getPlan().equals("silver")) {
                cashback = 0.003 * transactionAmount;
            } else if (user.getServicePlan().getPlan().equals("gold")) {
                cashback = 0.005 * transactionAmount;
            }
        }
        if (seller.getCashbackType().equals("spendingThreshold")) {
            account.setTotalSpentPerCommerciant(totalSpent);
        }
        return cashback;
    }
}

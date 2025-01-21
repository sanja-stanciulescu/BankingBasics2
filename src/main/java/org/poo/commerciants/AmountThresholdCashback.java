package org.poo.commerciants;

import org.poo.accounts.ClassicAccount;
import org.poo.users.User;

public class AmountThresholdCashback implements CashbackStrategy{
    @Override
    public double calculateCashback(Seller seller, ClassicAccount account, User user, double transactionAmount) {
        double totalSpent = account.getTotalSpentPerCommerciant() + transactionAmount;
        System.out.println("Total spent is " + totalSpent);
        double cashback = 0.0;
        if (totalSpent >= 500) {
            if (user.getServicePlan().getPlan().equals("standard") || user.getServicePlan().getPlan().equals("student")) {
                cashback = 0.25 * transactionAmount / 100;
            } else if (user.getServicePlan().getPlan().equals("silver")) {
                cashback = 0.5 * transactionAmount / 100;
            } else if (user.getServicePlan().getPlan().equals("gold")) {
                cashback = 0.7 * transactionAmount / 100;
            }
        } else if (totalSpent >= 300) {
            if (user.getServicePlan().getPlan().equals("standard") || user.getServicePlan().getPlan().equals("student")) {
                cashback = 0.002 * transactionAmount;
            } else if (user.getServicePlan().getPlan().equals("silver")) {
                cashback = 0.004 * transactionAmount;
            } else if (user.getServicePlan().getPlan().equals("gold")) {
                cashback = 0.0055 * transactionAmount;
            }
        } else if (totalSpent >= 100) {
            if (user.getServicePlan().getPlan().equals("standard") || user.getServicePlan().getPlan().equals("student")) {
                cashback = 0.1 * transactionAmount / 100;
            } else if (user.getServicePlan().getPlan().equals("silver")) {
                cashback = 0.3 * transactionAmount / 100;
            } else if (user.getServicePlan().getPlan().equals("gold")) {
                cashback = 0.5 * transactionAmount / 100;
            }
        }
        if (seller.getCashbackType().equals("spendingThreshold"))
            account.setTotalSpentPerCommerciant(totalSpent);
        return cashback;
    }
}

package org.poo.commerciants;

import org.poo.accounts.ClassicAccount;
import org.poo.users.User;

public class AmountThresholdCashback implements CashbackStrategy{
    @Override
    public double calculateCashback(Seller seller, ClassicAccount account, User user, double transactionAmount) {
        double totalSpent = account.getTotalSpent().get(seller.getType()) + transactionAmount;
        double cashback = 0.0;
        if (totalSpent > 500) {
            if (user.getServicePlan().getPlan().equals("standard") || user.getServicePlan().getPlan().equals("student")) {
                cashback = 0.25 * transactionAmount / 100;
            } else if (user.getServicePlan().getPlan().equals("silver")) {
                cashback = 0.5 * transactionAmount / 100;
            } else if (user.getServicePlan().getPlan().equals("gold")) {
                cashback = 0.7 * transactionAmount / 100;
            }
        } else if (totalSpent > 300) {
            if (user.getServicePlan().getPlan().equals("standard") || user.getServicePlan().getPlan().equals("student")) {
                cashback = 0.2 * transactionAmount / 100;
            } else if (user.getServicePlan().getPlan().equals("silver")) {
                cashback = 0.4 * transactionAmount / 100;
            } else if (user.getServicePlan().getPlan().equals("gold")) {
                cashback = 0.55 * transactionAmount / 100;
            }
        } else if (totalSpent > 100) {
            if (user.getServicePlan().getPlan().equals("standard") || user.getServicePlan().getPlan().equals("student")) {
                cashback = 0.1 * transactionAmount / 100;
            } else if (user.getServicePlan().getPlan().equals("silver")) {
                cashback = 0.3 * transactionAmount / 100;
            } else if (user.getServicePlan().getPlan().equals("gold")) {
                cashback = 0.5 * transactionAmount / 100;
            }
        }
        account.getTotalSpent().put(seller.getType(), totalSpent - cashback);
        return cashback;
    }
}

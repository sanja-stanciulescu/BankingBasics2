package org.poo.commerciants;

import org.poo.accounts.ClassicAccount;
import org.poo.users.User;

public interface CashbackStrategy {
    double calculateCashback(Seller seller, ClassicAccount account, User user, double transactionAmount);
}

package org.poo.commerciants;

import org.poo.accounts.ClassicAccount;
import org.poo.users.User;

public class NumberOfTransactionsCashback implements CashbackStrategy {

    @Override
    public double calculateCashback(Seller seller, ClassicAccount account, User user, double transactionAmount) {
        Integer transactions = seller.getNumberOfTransactions().get(account);

        if (transactions == null) {
            return 0;
        }
        if (transactions == 2 ) {
            if (account.getCoupons().get("Food") != -1)
                account.getCoupons().put("Food", 0.02);
        } else if (transactions == 5 ) {
            if (account.getCoupons().get("Clothes") != -1)
                account.getCoupons().put("Clothes", 0.05);
        } else if (transactions == 10 ) {
            if (account.getCoupons().get("Tech") != -1)
                account.getCoupons().put("Tech", 0.1);
        }
        return 0.0;
    }
}

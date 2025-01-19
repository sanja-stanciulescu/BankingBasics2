package org.poo.business;

import org.poo.accounts.BusinessAccount;
import org.poo.users.User;

public class Owner {
    private User user;
    private BusinessAccount account;

    public Owner(User user, BusinessAccount account) {
        this.user = user;
        this.account = account;
    }
}

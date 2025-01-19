package org.poo.business;

import org.poo.accounts.ClassicAccount;
import org.poo.users.User;

public class Manager {
    private User user;
    private ClassicAccount account;

    public Manager(final User user, final ClassicAccount account) {
        this.user = user;
        this.account = account;
    }
}

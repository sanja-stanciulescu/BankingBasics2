package org.poo.business;

import org.poo.accounts.ClassicAccount;
import org.poo.users.User;

public class Employee {
    public User user;
    public ClassicAccount account;

    public Employee(final User user, final ClassicAccount account) {
        this.user = user;
        this.account = account;
    }
}

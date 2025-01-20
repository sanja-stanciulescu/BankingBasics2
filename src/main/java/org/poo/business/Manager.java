package org.poo.business;

import org.poo.users.User;

public class Manager extends Employee {

    public Manager(final User user) {
        super(user);
        permissions = 1;
    }
}

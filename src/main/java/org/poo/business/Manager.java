package org.poo.business;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.poo.users.User;

public class Manager extends Employee {

    public Manager(final User user, final int order) {
        super(user, order);
        permissions = 1;
    }
}

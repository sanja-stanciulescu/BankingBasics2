package org.poo.business;

import org.poo.users.User;

public class Manager extends Employee {

    /**
     * Constructs a new {@code Manager} object with the specified {@code User} instance
     * and order value. Initializes the manager with a default permission level.
     *
     * @param user the {@code User} object associated with the manager
     * @param order the order or position of the manager within the hierarchy
     */
    public Manager(final User user, final int order) {
        super(user, order);
        permissions = 1;
    }
}

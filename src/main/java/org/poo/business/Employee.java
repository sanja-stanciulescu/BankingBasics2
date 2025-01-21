package org.poo.business;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.poo.users.User;

public class Employee {
    protected double deposited;
    protected double spent;
    protected String username;

    @JsonIgnore
    protected User user;
    @JsonIgnore
    protected int permissions;
    @JsonIgnore
    private int order;

    /**
     * Constructs a new {@code Employee} object with the specified {@code User} instance
     * and order value. Initializes the associated user's full name, and sets default
     * values for deposited amount, spent amount, and permissions.
     *
     * @param user the {@code User} object associated with the employee
     * @param order the order or position of the employee within the hierarchy
     */
    public Employee(final User user, final int order) {
        this.user = user;
        this.order = order;
        deposited = 0.0;
        spent = 0.0;
        username = user.getLastName() + " " + user.getFirstName();
        permissions = 0;
    }

    /**
     * Retrieves the {@code User} associated with this instance.
     *
     * @return the {@code User} object linked to this instance
     */
    public User getUser() {
        return user;
    }

    /**
     * Sets the user associated with this instance.
     *
     * @param user the {@code User} object to associate with this instance
     */
    public void setUser(final User user) {
        this.user = user;
    }

    /**
     * Retrieves the total amount deposited by this employee.
     *
     * @return the total deposited amount as a double.
     */
    public double getDeposited() {
        return deposited;
    }

    /**
     * Sets the total deposited amount for an employee.
     *
     * @param deposited the amount to set as the employee's total deposited value
     */
    public void setDeposited(final double deposited) {
        this.deposited = deposited;
    }

    /**
     * Retrieves the total amount spent by the employee.
     *
     * @return the total spent amount as a double.
     */
    public double getSpent() {
        return spent;
    }

    /**
     * Sets the total amount of money that has been spent.
     *
     * @param spent the amount to set as the total spent value
     */
    public void setSpent(final double spent) {
        this.spent = spent;
    }

    /**
     * Retrieves the username associated with this instance.
     *
     * @return the username of the employee as a String
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username for the employee.
     *
     * @param username the username to set for this employee
     */
    public void setUsername(final String username) {
        this.username = username;
    }

    /**
     * Retrieves the order value associated with this instance.
     *
     * @return an integer representing the order or position within a hierarchy.
     */
    public int getOrder() {
        return order;
    }

    /**
     * Sets the order or position for this instance.
     *
     * @param order the order value to assign
     */
    public void setOrder(final int order) {
        this.order = order;
    }
}

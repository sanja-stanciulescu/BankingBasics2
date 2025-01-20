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

    public Employee(final User user) {
        this.user = user;
        deposited = 0.0;
        spent = 0.0;
        username = user.getLastName() + " " + user.getFirstName();
        permissions = 0;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public double getDeposited() {
        return deposited;
    }

    public void setDeposited(double deposited) {
        this.deposited = deposited;
    }

    public double getSpent() {
        return spent;
    }

    public void setSpent(double spent) {
        this.spent = spent;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getPermissions() {
        return permissions;
    }

    public void setPermissions(int permissions) {
        this.permissions = permissions;
    }
}

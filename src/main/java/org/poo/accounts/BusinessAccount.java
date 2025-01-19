package org.poo.accounts;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.poo.business.Employee;
import org.poo.business.Manager;
import org.poo.business.Owner;
import org.poo.users.User;

import java.util.ArrayList;

public class BusinessAccount extends ClassicAccount {
    private double depositLimit;
    private ArrayList<Employee> employees;
    private ArrayList<Manager> managers;
    private double spendingLimit;
    private double totalDeposited;
    private double totalSpent;

    @JsonIgnore
    private Owner owner;

    public BusinessAccount(final String iban,
                           final String currency,
                           final String type,
                           final User user
    ) {
        super(iban, currency, type);
        this.employees = new ArrayList<Employee>();
        this.managers = new ArrayList<Manager>();
        this.owner = new Owner(user, this);
    }

    public double getDepositLimit() {
        return depositLimit;
    }

    public void setDepositLimit(double depositLimit) {
        this.depositLimit = depositLimit;
    }

    public ArrayList<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(ArrayList<Employee> employees) {
        this.employees = employees;
    }

    public ArrayList<Manager> getManagers() {
        return managers;
    }

    public void setManagers(ArrayList<Manager> managers) {
        this.managers = managers;
    }

    public double getSpendingLimit() {
        return spendingLimit;
    }

    public void setSpendingLimit(double spendingLimit) {
        this.spendingLimit = spendingLimit;
    }

    public double getTotalDeposited() {
        return totalDeposited;
    }

    public void setTotalDeposited(double totalDeposited) {
        this.totalDeposited = totalDeposited;
    }

    public double getTotalSpent() {
        return this.totalSpent;
    }

    public void setTotalSpent(double totalSpent) {
        this.totalSpent = totalSpent;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }
}

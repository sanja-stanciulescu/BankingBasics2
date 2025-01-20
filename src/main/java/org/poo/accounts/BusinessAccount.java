package org.poo.accounts;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.poo.business.Employee;
import org.poo.business.Manager;
import org.poo.business.Owner;
import org.poo.exchangeRates.Bnr;
import org.poo.users.User;

import java.util.HashMap;
import java.util.Map;

public class BusinessAccount extends ClassicAccount {
    @JsonProperty("deposit limit")
    private double depositLimit;
    private Map<String,Employee> employees;
    private Map<String, Manager> managers;
    @JsonProperty("spending limit")
    private double spendingLimit;
    @JsonProperty("total deposited")
    private double totalDeposited;
    @JsonProperty("total spent")
    private double totalSpent;

    @JsonIgnore
    private Owner owner;
    @JsonIgnore
    private Bnr bank;

    public BusinessAccount(final String iban,
                           final String currency,
                           final String type,
                           final User user,
                           final Bnr bank
    ) {
        super(iban, currency, type);
        this.employees = new HashMap<>();
        this.managers = new HashMap<>();
        this.owner = new Owner(user, this);
        this.bank = bank;

        if (!currency.equals("RON")) {
            double exchangeRate = bank.getExchangeRate("RON", currency);
            spendingLimit = exchangeRate * 500;
            depositLimit = exchangeRate * 500;
        }
    }

    public double getDepositLimit() {
        return depositLimit;
    }

    public void setDepositLimit(double depositLimit) {
        this.depositLimit = depositLimit;
    }

    public Map<String,Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(Map<String,Employee> employees) {
        this.employees = employees;
    }

    public Map<String, Manager> getManagers() {
        return managers;
    }

    public void setManagers(Map<String, Manager> managers) {
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

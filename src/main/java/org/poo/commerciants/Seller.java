package org.poo.commerciants;

import org.poo.accounts.ClassicAccount;
import org.poo.fileio.CommerciantInput;

import java.util.HashMap;
import java.util.Map;

public class Seller {
    private String commerciant;
    private String type;
    private int id;
    private String iban;

    private Map<ClassicAccount, Integer> numberOfTransactions;
    private CashbackStrategy cashbackStrategy;

    public Seller(String commerciant, String type, CashbackStrategy cashbackStrategy) {
        this.commerciant = commerciant;
        this.type = type;
        this.cashbackStrategy = cashbackStrategy;
        numberOfTransactions = new HashMap<>();
    }

    public Seller(CommerciantInput commerciantInput) {
        this.commerciant = commerciantInput.getCommerciant();
        this.type = commerciantInput.getType();
        this.id = commerciantInput.getId();
        this.iban = commerciantInput.getAccount();

        if (commerciantInput.getCashbackStrategy().equals("spendingThreshold")) {
            this.cashbackStrategy = new AmountThresholdCashback();
        } else if (commerciantInput.getCashbackStrategy().equals("nrOfTransactions")) {
            this.cashbackStrategy = new NumberOfTransactionsCashback();
        }

        numberOfTransactions = new HashMap<>();
    }

    public String getCommerciant() {
        return commerciant;
    }

    public void setCommerciant(String commerciant) {
        this.commerciant = commerciant;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public CashbackStrategy getCashbackStrategy() {
        return cashbackStrategy;
    }

    public void setCashbackStrategy(CashbackStrategy cashbackStrategy) {
        this.cashbackStrategy = cashbackStrategy;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public Map<ClassicAccount, Integer> getNumberOfTransactions() {
        return numberOfTransactions;
    }

    public void setNumberOfTransactions(Map<ClassicAccount, Integer> numberOfTransactions) {
        this.numberOfTransactions = numberOfTransactions;
    }
}

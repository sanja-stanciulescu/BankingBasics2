package org.poo.transactions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.accounts.ClassicAccount;
import org.poo.exchangeRates.Bnr;
import org.poo.fileio.CommandInput;
import org.poo.servicePlan.Plan;
import org.poo.servicePlan.PlanFactory;
import org.poo.users.User;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpgradePlanTransaction implements TransactionStrategy {
    private String accountIBAN;
    private String description;
    private String newPlanType;
    private int timestamp;

    @JsonIgnore
    private CommandInput command;
    @JsonIgnore
    private User user;
    @JsonIgnore
    private ClassicAccount account;
    @JsonIgnore
    private Plan newPlan;
    @JsonIgnore
    private Bnr bank;
    @JsonIgnore
    private ArrayNode output;

    public UpgradePlanTransaction(CommandInput command, User user, ClassicAccount account, Bnr bank, ArrayNode output) {
        this.command = command;
        this.user = user;
        this.account = account;
        this.bank = bank;
        this.output = output;
        this.accountIBAN = command.getAccount();
        this.timestamp = command.getTimestamp();
        this.newPlanType = command.getNewPlanType();
        newPlan = PlanFactory.createPlan(newPlanType);
        description = null;
    }

    @Override
    public void makeTransaction() {
        if (user == null || account == null) {
            CheckCardStatusTransaction.printError(command, "Account not found", timestamp, output);
            return;
        }

        if (user.getServicePlan().getPlan().equals(newPlanType)) {
            description = "The user already has the " + newPlanType + " plan.";
            accountIBAN = null;
            newPlanType = null;
            user.getTransactions().add(this);
            account.getTransactions().add(this);
            return;
        }

        if (user.getServicePlan().getId() > newPlan.getId()) {
            description = "You cannot downgrade your plan.";
            user.getTransactions().add(this);
            account.getTransactions().add(this);
            return;
        }

        int fee = 0;
        switch (user.getServicePlan().getPlan()) {
            case "standard", "student":
                if (newPlanType.equals("silver"))
                    fee = 100;
                else if (newPlanType.equals("gold"))
                    fee = 350;
                break;
            case "silver":
                if (newPlanType.equals("gold"))
                    fee = 250;
                break;
            default:
                break;
        }

        double amount;
        if (!account.getCurrency().equals("RON")) {
            double exchangeRate = bank.getExchangeRate("RON", account.getCurrency());
            amount = fee * exchangeRate;
        } else {
            amount = fee;
        }

        if (account.getBalance() - amount < account.getMinBalance()) {
            description = "Insufficient funds";
            accountIBAN = null;
            newPlanType = null;
        } else {
            account.setBalance(account.getBalance() - amount);
            user.setServicePlan(newPlan);
            description = "Upgrade plan";
        }

        user.getTransactions().add(this);
        account.getTransactions().add(this);
    }

    public String getAccountIBAN() {
        return accountIBAN;
    }

    public void setAccountIBAN(String accountIBAN) {
        this.accountIBAN = accountIBAN;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNewPlanType() {
        return newPlanType;
    }

    public void setNewPlanType(String newPlanType) {
        this.newPlanType = newPlanType;
    }

    @Override
    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }
}

package org.poo.transactions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.poo.accounts.ClassicAccount;
import org.poo.exchangeRates.Bnr;
import org.poo.fileio.CommandInput;
import org.poo.users.User;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class WithdrawSavingsTransaction implements TransactionStrategy{
    private int timestamp;
    private String description;
    private String classicAccountIBAN;
    private String savingsAccountIBAN;
    private Double amount;
    private static final int CURRENT_YEAR = 2025;

    @JsonIgnore
    private CommandInput command;
    @JsonIgnore
    private User currentUser;
    @JsonIgnore
    private ClassicAccount account;
    @JsonIgnore
    private Bnr bank;

    public WithdrawSavingsTransaction(final CommandInput command, final Bnr bank, final User currentUser, final ClassicAccount account) {
        this.command = command;
        this.bank = bank;
        this.currentUser = currentUser;
        this.account = account;
        this.timestamp = command.getTimestamp();
        savingsAccountIBAN = null;
        classicAccountIBAN = null;
        amount = null;
    }

    @Override
    public void makeTransaction() {
        if (currentUser == null) {
            return;
        }

        if (account == null) {
            description = "Account not found";
            currentUser.getTransactions().add(this);
            return;
        }

        if (!account.getType().equals("savings")) {
            description = "Account is not of type savings.";
            currentUser.getTransactions().add(this);
            return;
        }

        if (currentUser.getAccounts().size() - currentUser.getNumberOfSavingsAccounts() < 1) {
            description = "You do not have a classic account.";
            account.getTransactions().add(this);
            currentUser.getTransactions().add(this);
            return;
        }

        if (CURRENT_YEAR - Integer.parseInt(currentUser.getBirthDate().substring(0, 4)) < 21) {
            description = "You don't have the minimum age required.";
            currentUser.getTransactions().add(this);
            return;
        }

        ClassicAccount classicAccount = currentUser.getAccounts()
                .stream()
                .filter(acc -> acc.getCurrency().equals(command.getCurrency()) && acc.getType().equals("classic"))
                .findFirst().orElse(null);

        if (classicAccount == null) {
            description = "You do not have a classic account.";
            currentUser.getTransactions().add(this);
            return;
        }

        double amount;

        if (!command.getCurrency().equals(account.getCurrency())) {
            double exchangeRate = bank.getExchangeRate(command.getCurrency(),
                    account.getCurrency());
            amount = command.getAmount() * exchangeRate;
        } else {
            amount = command.getAmount();
        }

        account.setBalance(account.getBalance() - amount);
        classicAccount.setBalance(classicAccount.getBalance() + command.getAmount());
        description = "Savings withdrawal";
        this.amount = amount;
        classicAccountIBAN = classicAccount.getIban();
        savingsAccountIBAN = account.getIban();
        currentUser.getTransactions().add(this);
        currentUser.getTransactions().add(this);
    }

    @Override
    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CommandInput getCommand() {
        return command;
    }

    public void setCommand(CommandInput command) {
        this.command = command;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public ClassicAccount getAccount() {
        return account;
    }

    public void setAccount(ClassicAccount account) {
        this.account = account;
    }

    public Bnr getBank() {
        return bank;
    }

    public void setBank(Bnr bank) {
        this.bank = bank;
    }

    public String getClassicAccountIBAN() {
        return classicAccountIBAN;
    }

    public void setClassicAccountIBAN(String classicAccountIBAN) {
        this.classicAccountIBAN = classicAccountIBAN;
    }

    public String getSavingsAccountIBAN() {
        return savingsAccountIBAN;
    }

    public void setSavingsAccountIBAN(String savingsAccountIBAN) {
        this.savingsAccountIBAN = savingsAccountIBAN;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}

package org.poo.transactions.split_payment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.poo.accounts.ClassicAccount;
import org.poo.app.Finder;
import org.poo.exchangeRates.Bnr;
import org.poo.fileio.CommandInput;
import org.poo.transactions.TransactionStrategy;
import org.poo.users.User;

import java.util.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SplitPaymentTransaction implements TransactionStrategy {
    private String description;
    private int timestamp;
    private String currency;
    private ArrayList<String> involvedAccounts;
    private String error;
    private String splitPaymentType;
    private List<Double> amountForUsers;
    private Double amount;

    @JsonIgnore
    private CommandInput command;
    @JsonIgnore
    private ArrayList<Finder> finders;
    @JsonIgnore
    private Bnr bank;
    @JsonIgnore
    private boolean everythingOk;
    @JsonIgnore
    private Map<String, Boolean> approvals;
    @JsonIgnore
    private int waiting;

    /**
     * Constructs a new {@code SplitPaymentTransaction} with the given command input,
     * list of finders, and bank for exchange rate lookup.
     *
     * @param command the command input containing the transaction details.
     * @param finders the list of finders representing accounts and users involved in the split.
     * @param bank the bank used for exchange rates.
     */
    public SplitPaymentTransaction(
            final CommandInput command,
            final ArrayList<Finder> finders,
            final Bnr bank
    ) {
        this.command = command;
        this.finders = finders;
        this.bank = bank;
        error = null;
        this.timestamp = command.getTimestamp();
        this.splitPaymentType = command.getSplitPaymentType();
        this.amountForUsers = new ArrayList<>();
        this.involvedAccounts = new ArrayList<>();
        this.approvals = new HashMap<>();
        for (int i = 0; i < command.getAccounts().size(); i++) {
            approvals.put(finders.get(i).getUser().getEmail(), false);
        }
        waiting = 1;
        everythingOk = true;
    }

    /**
     * Executes the transaction by splitting the amount among the involved accounts.
     * It checks if all accounts have sufficient funds and converts the amount to the appropriate
     * currency before deducting the amounts from the accounts.
     */
    public void makeTransaction() {
        if (waiting == 1) {
            for (Finder finder : finders) {
                finder.getUser().getActiveTransactions().addLast(this);
            }
        } else {
            // It means that somebody rejected the SplitPayment
            if (!everythingOk) {
                for (Finder finder : finders) {
                    error = "One user rejected the payment.";
                    finder.getUser().getTransactions().add(this);
                    finder.getUser().getActiveTransactions().remove(this);
                    finder.getAccount().getTransactions().add(this);
                }
            }

            currency = command.getCurrency();
            description = "Split payment of " + String.format("%.2f",
                    command.getAmount()) + " " + currency;
            involvedAccounts.addAll(command.getAccounts());

            if (splitPaymentType.equals("equal")) {
                for (int i = 0; i < finders.size(); i++) {
                    amountForUsers.add(command.getAmount() / finders.size());
                }
            } else {
                amountForUsers = command.getAmountForUsers();
            }

            checkAccounts(finders, amountForUsers);

            if (everythingOk) {
                for (int i = 0; i < finders.size(); i++) {
                    double actualAmount = convertAmount(amountForUsers.get(i), finders.get(i));
                    finders.get(i).getAccount().setBalance(finders.get(i).getAccount().getBalance() - actualAmount);

                    finders.get(i).getUser().getTransactions().add(this);
                    finders.get(i).getUser().getTransactions().sort(Comparator.comparingInt(TransactionStrategy::getTimestamp));

                    finders.get(i).getAccount().getTransactions().add(this);
                    finders.get(i).getAccount().getTransactions().sort(Comparator.comparingInt(TransactionStrategy::getTimestamp));
                }
                if (splitPaymentType.equals("equal")) {
                    amount = command.getAmount() / finders.size();
                    amountForUsers = null;
                } else {
                    amount = null;
                }
            }
        }
    }

    /**
     * Checks whether all accounts involved in the split payment are valid and have sufficient
     * balance.
     * If any account is invalid or lacks sufficient funds, an error is set.
     *
     * @param finders the list of finders representing the involved accounts.
     */
    private void checkAccounts(final ArrayList<Finder> finders, List<Double> amountForUsers) {
        everythingOk = true;
        for (int i = 0; i < finders.size(); i++) {
            if (finders.get(i).getUser() == null || finders.get(i).getAccount() == null) {
                everythingOk = false;
            }
            double amount = convertAmount(amountForUsers.get(i), finders.get(i));
            if (Double.compare(finders.get(i).getAccount().getBalance(), amount) < 0) {
                error = "Account " + finders.get(i).getAccount().getIban()
                        + " has insufficient funds for a split payment.";
                addToAll(finders);
                if (splitPaymentType.equals("equal")) {
                    this.amount = command.getAmount() / finders.size();
                    this.amountForUsers = null;
                } else {
                    this.amount = null;
                }
                break;
            }
        }

        if (error != null) {
            everythingOk = false;
        }
    }

    private void addToAll(final ArrayList<Finder> finders) {
        for (int i = 0; i < finders.size(); i++) {
            finders.get(i).getUser().getTransactions().add(this);
            finders.get(i).getUser().getTransactions().sort(Comparator.comparingInt(TransactionStrategy::getTimestamp));

            finders.get(i).getAccount().getTransactions().add(this);
            finders.get(i).getAccount().getTransactions().sort(Comparator.comparingInt(TransactionStrategy::getTimestamp));
        }
    }

    /**
     * Converts the amount to the appropriate currency based on the account's currency.
     *
     * @param givenAmount the initial amount the user needed to pay.
     * @param finder the specific finder for which the amount is being converted.
     * @return the converted amount for the given finder.
     */
    private double convertAmount(final Double givenAmount, final Finder finder) {
        double amount;
        if (!command.getCurrency().equals(finder.getAccount().getCurrency())) {
            double exchangeRate = bank.getExchangeRate(command.getCurrency(),
                                                        finder.getAccount().getCurrency());
            amount = givenAmount * exchangeRate;
        } else {
            amount = givenAmount;
        }
        return amount;
    }

    private boolean allUsersApproved() {
        if (approvals.values().stream().allMatch(t -> t)) {
            for (Finder finder : finders) {
                finder.getUser().getActiveTransactions().remove(this);
            }
            return true;
        }
        return false;
    }

    public void approveUser(String email) {
        if (approvals.containsKey(email)) {
            approvals.put(email, true);
            int i;
            for (i = 0; i < finders.size(); i++) {
                if (finders.get(i).getUser().getEmail().equals(email)) {
                    break;
                }
            }
            finders.get(i).getUser().getActiveTransactions().remove(this);
        }

        if (allUsersApproved()) {
            waiting = 0;
            this.makeTransaction();
        }
    }

    public void rejectUser(String email) {
        if (approvals.containsKey(email)) {
            approvals.put(email, false);
            everythingOk = false;
            waiting = 0;
            this.makeTransaction();
        }
    }

    /**
     * Gets the description of the transaction.
     *
     * @return the description of the transaction.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description for the transaction.
     *
     * @param description the description to set.
     */
    public void setDescription(final String description) {
        this.description = description;
    }

    /**
     * Gets the timestamp of the transaction.
     *
     * @return the timestamp of the transaction.
     */
    public int getTimestamp() {
        return timestamp;
    }

    /**
     * Sets the timestamp for the transaction.
     *
     * @param timestamp the timestamp to set.
     */
    public void setTimestamp(final int timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Gets the amount involved in the transaction.
     *
     * @return the amount of the transaction.
     */
    public Double getAmount() {
        return amount;
    }

    /**
     * Sets the amount involved in the transaction.
     *
     * @param amount the amount to set.
     */
    public void setAmount(final Double amount) {
        this.amount = amount;
    }

    /**
     * Gets the currency of the transaction.
     *
     * @return the currency of the transaction.
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * Sets the currency for the transaction.
     *
     * @param currency the currency to set.
     */
    public void setCurrency(final String currency) {
        this.currency = currency;
    }

    /**
     * Gets the list of involved account IBANs.
     *
     * @return the list of involved accounts.
     */
    public ArrayList<String> getInvolvedAccounts() {
        return involvedAccounts;
    }

    /**
     * Sets the list of involved accounts.
     *
     * @param involvedAccounts the list of involved accounts to set.
     */
    public void setInvolvedAccounts(final ArrayList<String> involvedAccounts) {
        this.involvedAccounts = involvedAccounts;
    }

    /**
     * Gets the error message related to the transaction, if any.
     *
     * @return the error message or null if no error occurred.
     */
    public String getError() {
        return error;
    }

    /**
     * Sets the error message for the transaction.
     *
     * @param error the error message to set.
     */
    public void setError(final String error) {
        this.error = error;
    }

    public String getSplitPaymentType() {
        return splitPaymentType;
    }

    public void setSplitPaymentType(String splitPaymentType) {
        this.splitPaymentType = splitPaymentType;
    }

    public List<Double> getAmountForUsers() {
        return amountForUsers;
    }

    public void setAmountForUsers(List<Double> amountForUsers) {
        this.amountForUsers = amountForUsers;
    }
}

package org.poo.transactions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.poo.accounts.ClassicAccount;
import org.poo.exchangeRates.Bnr;
import org.poo.fileio.CommandInput;
import org.poo.users.User;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class WithdrawSavingsTransaction implements TransactionStrategy {
    private int timestamp;
    private String description;
    private String classicAccountIBAN;
    private String savingsAccountIBAN;
    private Double amount;
    private static final int CURRENT_YEAR = 2025;
    private static final int MINIMUM_AGE = 21;
    private static final int DIGITS_IN_YEAR = 4;

    @JsonIgnore
    private CommandInput command;
    @JsonIgnore
    private User currentUser;
    @JsonIgnore
    private ClassicAccount account;
    @JsonIgnore
    private Bnr bank;

    /**
     * Constructs a new WithdrawSavingsTransaction.
     *
     * This constructor initializes a withdrawal transaction from a savings account.
     * It sets the required parameters, including the initial command details,
     * the bank instance, the user performing the transaction, and the associated
     * account.
     *
     * @param command the {@code CommandInput} object containing the details of the
     *                transaction command
     * @param bank the {@code Bnr} instance representing the bank service facilitating
     *             the transaction
     * @param currentUser the {@code User} instance representing the individual
     *                    performing the transaction
     * @param account the {@code ClassicAccount} instance associated with the transaction
     */
    public WithdrawSavingsTransaction(
            final CommandInput command,
            final Bnr bank,
            final User currentUser,
            final ClassicAccount account
    ) {
        this.command = command;
        this.bank = bank;
        this.currentUser = currentUser;
        this.account = account;
        this.timestamp = command.getTimestamp();
        savingsAccountIBAN = null;
        classicAccountIBAN = null;
        amount = null;
    }

    /**
     * Executes a savings account withdrawal transaction.
     *
     * The method validates multiple conditions to determine if the transaction can be
     * performed, modifies account balances accordingly, and updates transaction logs.
     *
     * The checks performed include the following:
     * - Ensures the `currentUser` is not null.
     * - Ensures the target `account` is not null and is of type "savings".
     * - Ensures the user has at least one "classic" account.
     * - Validates that the user meets the minimum age requirement of 21 years.
     * - Ensures currency compatibility between accounts, and calculates the amount
     *   using appropriate exchange rates if currencies differ.
     * - Ensures sufficient balance in the savings account to complete the transaction.
     *
     * On successful withdrawal:
     * - Deducts the specified amount from the savings account.
     * - Credits the corresponding amount to a classic account.
     * - Updates relevant fields such as `amount`, `description`, `classicAccountIBAN`,
     *   and `savingsAccountIBAN`.
     * - Logs the transaction in both the user's and the affected account's transaction lists.
     *
     * If any validation fails, an appropriate `description` is set describing the issue,
     * and the transaction is recorded with no changes to account balances.
     */
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

        if (CURRENT_YEAR - Integer.parseInt(currentUser.getBirthDate()
                .substring(0, DIGITS_IN_YEAR)) < MINIMUM_AGE) {
            description = "You don't have the minimum age required.";
            currentUser.getTransactions().add(this);
            return;
        }

        ClassicAccount classicAccount = currentUser.getAccounts()
                .stream()
                .filter(acc -> acc.getCurrency().equals(command.getCurrency())
                        && acc.getType().equals("classic"))
                .findFirst().orElse(null);

        if (classicAccount == null) {
            description = "You do not have a classic account.";
            currentUser.getTransactions().add(this);
            account.getTransactions().add(this);
            return;
        }

        double withdrawalAmount;

        if (!command.getCurrency().equals(account.getCurrency())) {
            double exchangeRate = bank.getExchangeRate(command.getCurrency(),
                    account.getCurrency());
            withdrawalAmount = command.getAmount() * exchangeRate;
        } else {
            withdrawalAmount = command.getAmount();
        }

        if (account.getBalance() - withdrawalAmount < 0) {
            return;
        }
        account.setBalance(account.getBalance() - withdrawalAmount);
        classicAccount.setBalance(classicAccount.getBalance() + command.getAmount());
        description = "Savings withdrawal";
        this.amount = withdrawalAmount;
        classicAccountIBAN = classicAccount.getIban();
        savingsAccountIBAN = account.getIban();
        currentUser.getTransactions().add(this);
        currentUser.getTransactions().add(this);
    }

    /**
     * Retrieves the timestamp representing when the transaction occurred.
     *
     * @return the timestamp of the transaction as an integer
     */
    @Override
    public int getTimestamp() {
        return timestamp;
    }

    /**
     * Sets the timestamp for the transaction.
     *
     * @param timestamp the timestamp of the transaction, represented as an integer
     */
    public void setTimestamp(final int timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Returns the description of the transaction.
     *
     * @return the description of the transaction as a string
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description for the transaction.
     *
     * @param description the description of the transaction
     */
    public void setDescription(final String description) {
        this.description = description;
    }

    /**
     * Retrieves the {@code CommandInput} object associated with this transaction.
     *
     * @return the {@code CommandInput} object containing the details of the command
     */
    public CommandInput getCommand() {
        return command;
    }

    /**
     * Sets the command input for the transaction.
     *
     * @param command the {@code CommandInput} object containing the details
     *                of the command to be processed for the transaction.
     */
    public void setCommand(final CommandInput command) {
        this.command = command;
    }

    /**
     * Retrieves the associated {@code ClassicAccount} for this transaction.
     *
     * @return the {@code ClassicAccount} instance related to this transaction.
     */
    public ClassicAccount getAccount() {
        return account;
    }

    /**
     * Updates the account associated with the WithdrawSavingsTransaction instance.
     *
     * @param account the ClassicAccount instance to be associated with this transaction.
     */
    public void setAccount(final ClassicAccount account) {
        this.account = account;
    }

    /**
     * Retrieves the current instance of the {@code Bnr} object associated with the transaction.
     *
     * @return the {@code Bnr} instance representing the bank service used in the transaction.
     */
    public Bnr getBank() {
        return bank;
    }

    /**
     * Sets the bank instance for this transaction.
     *
     * @param bank the bank object to be associated with the transaction
     */
    public void setBank(final Bnr bank) {
        this.bank = bank;
    }

    /**
     * Retrieves the IBAN (International Bank Account Number) associated with the classic account
     * involved in this transaction.
     *
     * @return the classic account IBAN as a String
     */
    public String getClassicAccountIBAN() {
        return classicAccountIBAN;
    }

    /**
     * Sets the IBAN of the classic account associated with the transaction.
     *
     * @param classicAccountIBAN the IBAN of the classic account to be set
     */
    public void setClassicAccountIBAN(final String classicAccountIBAN) {
        this.classicAccountIBAN = classicAccountIBAN;
    }

    /**
     * Retrieves the IBAN (International Bank Account Number) for the savings account
     * associated with the transaction.
     *
     * @return the IBAN of the savings account as a string
     */
    public String getSavingsAccountIBAN() {
        return savingsAccountIBAN;
    }

    /**
     * Sets the IBAN of the savings account associated with this transaction.
     *
     * @param savingsAccountIBAN the IBAN of the savings account to be set
     */
    public void setSavingsAccountIBAN(final String savingsAccountIBAN) {
        this.savingsAccountIBAN = savingsAccountIBAN;
    }

    /**
     * Retrieves the amount associated with the transaction.
     *
     * @return the amount of the transaction as a Double
     */
    public Double getAmount() {
        return amount;
    }

    /**
     * Sets the amount for the transaction.
     *
     * @param amount the amount to be set for this transaction
     */
    public void setAmount(final Double amount) {
        this.amount = amount;
    }
}

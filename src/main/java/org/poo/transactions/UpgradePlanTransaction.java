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
    private static final int STANDARD_FEE = 100;
    private static final int SILVER_FEE = 250;
    private static final int GOLD_FEE = 350;

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
    @JsonIgnore
    private int automatic;

    /**
     * Constructs an {@code UpgradePlanTransaction} object which handles the logic
     * for upgrading a user's service plan. It initializes the transaction details,
     * including the related account, user, bank, transaction output, and whether
     * the transaction should be automatic or manual.
     *
     * @param command the {@code CommandInput} object containing details such as the
     *                account and new plan type for the upgrade
     * @param user the {@code User} object representing the user performing the transaction
     * @param account the {@code ClassicAccount} representing the user's account related to
     *                this transaction
     * @param bank the {@code Bnr} object representing the bank involved in the transaction
     * @param output the {@code ArrayNode} for processing and storing transaction output information
     * @param automatic an integer flag to indicate whether the upgrade transaction is automatic
     *                  (non-zero) or manual (zero)
     */
    public UpgradePlanTransaction(
            final CommandInput command,
            final User user,
            final ClassicAccount account,
            final Bnr bank,
            final ArrayNode output,
            final int automatic
    ) {
        this.command = command;
        this.user = user;
        this.account = account;
        this.bank = bank;
        this.output = output;
        this.automatic = automatic;
        this.accountIBAN = command.getAccount();
        this.timestamp = command.getTimestamp();
        this.newPlanType = command.getNewPlanType();
        newPlan = PlanFactory.createPlan(newPlanType);
        if (automatic == 0) {
            description = null;
        } else {
            user.setServicePlan(newPlan);
            description = "Upgrade plan";
        }
    }

    /**
     * Executes the transaction for upgrading a user's service plan.
     * This method performs several validations and operations:
     *
     * - Checks if the user and account are valid, otherwise logs an error.
     * - Verifies if the user already has the requested plan type and finalizes
     * the transaction if true.
     * - Prevents downgrading to a lower-tier plan and logs a corresponding message.
     * - Calculates the upgrade fee based on the user's current plan and the target plan type.
     * - Checks for sufficient funds in the user's account to cover the upgrade fee,
     * considering currency exchange rates if needed.
     * - Deducts the upgrade fee (when applicable) and updates the user's service plan.
     *
     * The method updates the transaction logs for both the user and the account regardless
     * of the outcome.
     */
    @Override
    public void makeTransaction() {
        if (user == null || account == null) {
            CheckCardStatusTransaction.printError(command,
                    "Account not found", timestamp, output);
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
                if (newPlanType.equals("silver")) {
                    fee = STANDARD_FEE;
                } else if (newPlanType.equals("gold")) {
                    fee = GOLD_FEE;
                }
                break;
            case "silver":
                if (newPlanType.equals("gold")) {
                    fee = SILVER_FEE;
                }
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
            if (automatic == 0) {
                account.setBalance(account.getBalance() - amount);
            }
            user.setServicePlan(newPlan);
            description = "Upgrade plan";
        }

        user.getTransactions().add(this);
        account.getTransactions().add(this);
    }

    /**
     * Retrieves the International Bank Account Number (IBAN) associated with the account.
     *
     * @return the IBAN of the account as a String
     */
    public String getAccountIBAN() {
        return accountIBAN;
    }

    /**
     * Sets the IBAN (International Bank Account Number) for the account
     * associated with this transaction.
     *
     * @param accountIBAN the IBAN to set for the account
     */
    public void setAccountIBAN(final String accountIBAN) {
        this.accountIBAN = accountIBAN;
    }

    /**
     * Retrieves the description of the transaction.
     *
     * @return the description of the transaction
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description for the upgrade plan transaction.
     *
     * @param description the description of the transaction to set
     */
    public void setDescription(final String description) {
        this.description = description;
    }

    /**
     * Retrieves the new plan type associated with the transaction.
     *
     * @return the new plan type as a string
     */
    public String getNewPlanType() {
        return newPlanType;
    }

    /**
     * Updates the current plan type associated with the transaction.
     *
     * @param newPlanType the new plan type to be set
     */
    public void setNewPlanType(final String newPlanType) {
        this.newPlanType = newPlanType;
    }

    /**
     * Retrieves the timestamp of the transaction.
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
     * @param timestamp the timestamp to be set, representing the specific time of the transaction
     */
    public void setTimestamp(final int timestamp) {
        this.timestamp = timestamp;
    }
}

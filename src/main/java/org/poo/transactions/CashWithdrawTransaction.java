package org.poo.transactions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.accounts.ClassicAccount;
import org.poo.cards.Card;
import org.poo.exchangeRates.Bnr;
import org.poo.fileio.CommandInput;
import org.poo.users.User;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CashWithdrawTransaction implements TransactionStrategy {
    private String description;
    private int timestamp;
    private Double amount;

    @JsonIgnore
    private CommandInput commandInput;
    @JsonIgnore
    private User user;
    @JsonIgnore
    private ClassicAccount account;
    @JsonIgnore
    private Card card;
    @JsonIgnore
    private ArrayNode output;
    @JsonIgnore
    private Bnr bank;

    /**
     * Constructs a new {@code CashWithdrawTransaction} with the specified parameters.
     *
     * @param commandInput the {@code CommandInput} object containing the details of the command
     * @param user the {@code User} initiating the cash withdrawal transaction
     * @param account the {@code ClassicAccount} from which the cash is being withdrawn
     * @param card the {@code Card} associated with the account being used for the withdrawal
     * @param output the {@code ArrayNode} used to store output details for the transaction
     * @param bank the {@code Bnr} bank instance processing the withdrawal transaction
     */
    public CashWithdrawTransaction(
            final CommandInput commandInput,
            final User user,
            final ClassicAccount account,
            final Card card,
            final ArrayNode output,
            final Bnr bank
    ) {
        this.commandInput = commandInput;
        this.user = user;
        this.account = account;
        this.card = card;
        this.output = output;
        this.bank = bank;
        timestamp = commandInput.getTimestamp();
        description = null;
        amount = null;
    }

    /**
     * Executes a cash withdrawal transaction for a given user and their associated account and card
     * This method ensures that all necessary validation checks are performed before processing
     * the transaction. It supports handling user verification, account and card validation,
     * transaction, currency conversion, and balance adjustments.
     *
     * If the card or account associated with the transaction is invalid, or if the email of
     * the user is not provided or mismatches the card owner (for non-business accounts), an error
     * message will be generated.
     *
     * If the card's status is "frozen", the transaction will not proceed, and a relevant
     * description is logged. Additionally, when the account's currency is not "RON",
     * currency conversion is applied using the exchange rate from the associated bank.
     *
     * During balance verification, the method ensures there are sufficient funds available in the
     * account. If the account balance, after considering the transaction amount and applicable
     * commissions, falls below the account's minimum balance, the transaction will be marked as
     * unsuccessful with an "Insufficient funds" message logged. Otherwise, the account is debited,
     * and the transaction is recorded with the withdrawal amount and description.
     */
    @Override
    public void makeTransaction() {
        if (user == null || account == null || card == null) {
            CheckCardStatusTransaction.printError(commandInput, "Card not found",
                    timestamp, output);
        } else if (commandInput.getEmail() == null) {
            CheckCardStatusTransaction.printError(commandInput, "User not found",
                    timestamp, output);
        } else {
            if (!account.getType().equals("business")
                    && !card.getCreatorEmail().equals(commandInput.getEmail())) {
                CheckCardStatusTransaction.printError(commandInput, "Card not found",
                        timestamp, output);
                return;
            }

            if (card.getStatus().equals("frozen")) {
                description = "The card is frozen";
                user.getTransactions().add(this);
                return;
            }

            double tempAmount;
            if (!account.getCurrency().equals("RON")) {
                double exchangeRate = bank.getExchangeRate("RON", account.getCurrency());
                tempAmount = commandInput.getAmount() * exchangeRate;
            } else {
                tempAmount = commandInput.getAmount();
            }

            double commission = user.getServicePlan().getComissionRate(commandInput.getAmount());

            if (account.getBalance() - tempAmount - commission * tempAmount
                    <= account.getMinBalance()) {
                description = "Insufficient funds";
            } else {
                account.setBalance(account.getBalance() - tempAmount - tempAmount * commission);
                amount = commandInput.getAmount();
                description = "Cash withdrawal of " + amount;
            }
            user.getTransactions().add(this);
        }
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
     * Sets the description for the transaction.
     *
     * @param description the description to associate with the transaction
     */
    public void setDescription(final String description) {
        this.description = description;
    }

    /**
     * Retrieves the timestamp of the transaction.
     *
     * @return the timestamp representing when the transaction occurred
     */
    @Override
    public int getTimestamp() {
        return timestamp;
    }

    /**
     * Sets the timestamp for the transaction.
     *
     * @param timestamp the given timestamp representing when the transaction occurred
     */
    public void setTimestamp(final int timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Retrieves the amount associated with the transaction.
     *
     * @return the amount of the transaction as a Double.
     */
    public Double getAmount() {
        return amount;
    }

    /**
     * Sets the amount for the transaction.
     *
     * @param amount the amount to set for the transaction
     */
    public void setAmount(final Double amount) {
        this.amount = amount;
    }
}

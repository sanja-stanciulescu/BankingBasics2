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

    public CashWithdrawTransaction(CommandInput commandInput, User user, ClassicAccount account, Card card, ArrayNode output, Bnr bank) {
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

    @Override
    public void makeTransaction() {
        if (user == null || account == null || card == null) {
            CheckCardStatusTransaction.printError(commandInput, "Card not found", timestamp, output);
        } else if (commandInput.getEmail() == null) {
            CheckCardStatusTransaction.printError(commandInput, "User not found", timestamp, output);
        } else {
            if (!account.getType().equals("business") && !card.getCreatorEmail().equals(commandInput.getEmail())) {
                CheckCardStatusTransaction.printError(commandInput, "Card not found", timestamp, output);
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

            if (account.getBalance() - tempAmount - commission * tempAmount <= account.getMinBalance()) {
                description = "Insufficient funds";
            } else {
                account.setBalance(account.getBalance() - tempAmount - tempAmount * commission);
                amount = commandInput.getAmount();
                description = "Cash withdrawal of " + amount;
            }
            user.getTransactions().add(this);
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}

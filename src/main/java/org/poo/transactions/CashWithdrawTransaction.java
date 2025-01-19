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
    }

    @Override
    public void makeTransaction() {
        if (user == null || account == null || card == null) {
            CheckCardStatusTransaction.printError(commandInput, timestamp, output);
        } else {
            if (card.getStatus().equals("frozen")) {
                description = "The card is frozen";
                user.getTransactions().add(this);
                return;
            }

            double amount;
            if (!account.getCurrency().equals("RON")) {
                double exchangeRate = bank.getExchangeRate("RON", account.getCurrency());
                amount = commandInput.getAmount() * exchangeRate;
            } else {
                amount = commandInput.getAmount();
            }

            double commission = user.getServicePlan().getComissionRate(commandInput.getAmount());

            if (account.getBalance() - amount - commission * amount <= account.getMinBalance()) {
                description = "Insufficient funds";
                user.getTransactions().add(this);
            } else {
                account.setBalance(account.getBalance() - amount - amount * commission);
            }
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
}

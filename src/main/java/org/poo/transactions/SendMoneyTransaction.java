package org.poo.transactions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.accounts.BusinessAccount;
import org.poo.accounts.ClassicAccount;
import org.poo.exchangeRates.Bnr;
import org.poo.fileio.CommandInput;
import org.poo.users.User;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SendMoneyTransaction implements TransactionStrategy {
    private String description;
    private int timestamp;
    private String senderIBAN;
    private String receiverIBAN;
    private String amount;
    private String transferType;

    @JsonIgnore
    private ClassicAccount giver;
    @JsonIgnore
    private User giverUser;
    @JsonIgnore
    private ClassicAccount receiver;
    @JsonIgnore
    private User receiverUser;
    @JsonIgnore
    private Bnr bank;
    @JsonIgnore
    private CommandInput command;
    @JsonIgnore
    private ArrayNode output;

    /**
     * Constructs a new {@code SendMoneyTransaction} with the given command input,
     * sender and receiver accounts, their respective users, and the bank for currency conversion.
     *
     * @param command the command input containing transaction details.
     * @param giver the sender's account.
     * @param giverUser the sender's user information.
     * @param receiver the receiver's account.
     * @param receiverUser the receiver's user information.
     * @param bank the bank used for currency exchange.
     */
    public SendMoneyTransaction(
            final CommandInput command,
            final ClassicAccount giver,
            final User giverUser,
            final ClassicAccount receiver,
            final User receiverUser,
            final Bnr bank,
            final ArrayNode output
    ) {
        this.command = command;
        this.giver = giver;
        this.giverUser = giverUser;
        this.receiver = receiver;
        this.receiverUser = receiverUser;
        this.bank = bank;
        this.output = output;
        this.timestamp = command.getTimestamp();
        this.description = command.getDescription();
    }

    /**
     * Constructs a new {@code SendMoneyTransaction} for a specific description, timestamp,
     * sender IBAN, receiver IBAN, amount, and transfer type (either "sent" or "received").
     *
     * @param description the description of the transaction.
     * @param timestamp the timestamp of the transaction.
     * @param senderIBAN the IBAN of the sender.
     * @param receiverIBAN the IBAN of the receiver.
     * @param amount the amount to be transferred.
     * @param transferType the type of transfer ("sent" or "received").
     */
    public SendMoneyTransaction(
            final String description,
            final int timestamp,
            final String senderIBAN,
            final String receiverIBAN,
            final String amount,
            final String transferType
    ) {
        this.description = description;
        this.timestamp = timestamp;
        this.senderIBAN = senderIBAN;
        this.receiverIBAN = receiverIBAN;
        this.amount = amount;
        this.transferType = transferType;
    }

    /**
     * Executes the transaction by transferring money from the giver's account to
     * the receiver's account.
     * Updates the balances of both accounts, records the transaction, and creates a corresponding
     * transaction for the receiver.
     */
    public void makeTransaction() {
        if (giver == null || receiver == null) {
            CheckCardStatusTransaction.printError(command, "User not found", timestamp, output);
            return;
        }

        if (giver.getBalance() - command.getAmount() <= 0) {
            description = "Insufficient funds";
        } else {
            senderIBAN = giver.getIban();
            receiverIBAN = receiver.getIban();
            amount = command.getAmount() + " " + giver.getCurrency();
            transferType = "sent";

            double transactionAmount;
            if (!giver.getCurrency().equals(receiver.getCurrency())) {
                double exchangeRate = bank.getExchangeRate(giver.getCurrency(),
                        receiver.getCurrency());
                transactionAmount = command.getAmount() * exchangeRate;
            } else {
                transactionAmount = command.getAmount();
            }

            if (giver.getType().equals("business")) {
                BusinessAccount business = (BusinessAccount) giver;
                if (business.getEmployees().containsKey(command.getEmail())) {
                    if (transactionAmount > business.getSpendingLimit()) {
                        giverUser.getTransactions().remove(this);
                        return;
                    }
                    double initialAmount = business.getEmployees()
                            .get(giverUser.getEmail()).getSpent();
                    business.getEmployees()
                            .get(giverUser.getEmail()).setSpent(initialAmount + transactionAmount);
                    business.setTotalSpent(business.getTotalSpent() + transactionAmount);
                } else if (business.getManagers().containsKey(giverUser.getEmail())) {
                    double initialAmount = business.getManagers()
                            .get(giverUser.getEmail()).getSpent();
                    business.getManagers()
                            .get(giverUser.getEmail()).setSpent(initialAmount + transactionAmount);
                    business.setTotalSpent(business.getTotalSpent() + transactionAmount);
                }
            }

            double commission;
            if (!giver.getCurrency().equals("RON")) {
                double exchangeRate = bank.getExchangeRate(giver.getCurrency(), "RON");
                commission = giverUser.getServicePlan()
                        .getComissionRate(command.getAmount() * exchangeRate);
            } else {
                commission = giverUser
                        .getServicePlan().getComissionRate(command.getAmount());
            }

            giver.setBalance(giver.getBalance() - command.getAmount()
                    - commission * command.getAmount());
            receiver.setBalance(receiver.getBalance() + transactionAmount);
            TransactionStrategy trans = new SendMoneyTransaction(description, timestamp,
                    senderIBAN, receiverIBAN,
                    transactionAmount + " " + receiver.getCurrency(),
                    "received");
            receiverUser.getTransactions().add(trans);
            receiver.getTransactions().add(trans);
        }

        if (giver.getType().equals("business")) {
            BusinessAccount business = (BusinessAccount) giver;
            if (business.getOwner().getUser() == giverUser) {
                giverUser.getTransactions().add(this);
            }
        } else if (giverUser.getEmail().equals(receiverUser.getEmail())) {
            giverUser.getTransactions().add(giverUser.getTransactions().size() - 1, this);
        } else {
            giverUser.getTransactions().add(this);
        }

        if (giver.getType().equals("classic")) {
            giver.getTransactions().add(this);
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
     * Gets the receiver's account.
     *
     * @return the receiver's account.
     */
    public ClassicAccount getReceiver() {
        return receiver;
    }

    /**
     * Sets the receiver's account.
     *
     * @param receiver the receiver's account to set.
     */
    public void setReceiver(final ClassicAccount receiver) {
        this.receiver = receiver;
    }

    /**
     * Gets the command input containing transaction details.
     *
     * @return the command input for the transaction.
     */
    public CommandInput getCommand() {
        return command;
    }

    /**
     * Sets the command input for the transaction.
     *
     * @param command the command input to set.
     */
    public void setCommand(final CommandInput command) {
        this.command = command;
    }

    /**
     * Gets the sender's IBAN.
     *
     * @return the sender's IBAN.
     */
    public String getSenderIBAN() {
        return senderIBAN;
    }

    /**
     * Sets the sender's IBAN.
     *
     * @param senderIBAN the sender's IBAN to set.
     */
    public void setSenderIBAN(final String senderIBAN) {
        this.senderIBAN = senderIBAN;
    }

    /**
     * Gets the receiver's IBAN.
     *
     * @return the receiver's IBAN.
     */
    public String getReceiverIBAN() {
        return receiverIBAN;
    }

    /**
     * Sets the receiver's IBAN.
     *
     * @param receiverIBAN the receiver's IBAN to set.
     */
    public void setReceiverIBAN(final String receiverIBAN) {
        this.receiverIBAN = receiverIBAN;
    }

    /**
     * Gets the amount being transferred.
     *
     * @return the amount to be transferred.
     */
    public String getAmount() {
        return amount;
    }

    /**
     * Sets the amount for the transaction.
     *
     * @param amount the amount to set.
     */
    public void setAmount(final String amount) {
        this.amount = amount;
    }

    /**
     * Gets the transfer type ("sent" or "received").
     *
     * @return the transfer type.
     */
    public String getTransferType() {
        return transferType;
    }

    /**
     * Sets the transfer type ("sent" or "received").
     *
     * @param transferType the transfer type to set.
     */
    public void setTransferType(final String transferType) {
        this.transferType = transferType;
    }
}

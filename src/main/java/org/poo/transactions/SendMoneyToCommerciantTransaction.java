package org.poo.transactions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.accounts.BusinessAccount;
import org.poo.accounts.ClassicAccount;
import org.poo.business.BusinessCommerciant;
import org.poo.commerciants.Seller;
import org.poo.exchangeRates.Bnr;
import org.poo.fileio.CommandInput;
import org.poo.users.User;

public class SendMoneyToCommerciantTransaction implements  TransactionStrategy {
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
    private Seller receiver;
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
     * @param bank the bank used for currency exchange.
     */
    public SendMoneyToCommerciantTransaction(
            final CommandInput command,
            final ClassicAccount giver,
            final User giverUser,
            final Seller receiver,
            final Bnr bank,
            final ArrayNode output
    ) {
        this.command = command;
        this.giver = giver;
        this.giverUser = giverUser;
        this.receiver = receiver;
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
    public SendMoneyToCommerciantTransaction(
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

            double amount = command.getAmount();

            if (giver.getType().equals("business")) {
                BusinessAccount business = (BusinessAccount) giver;
                if (business.getEmployees().containsKey(command.getEmail()) && amount > business.getSpendingLimit()) {
                    return;
                }
            }

            double coupon = 0.0;
            if (giver.getCoupons().get(receiver.getType()) != -1.0 && giver.getCoupons().get(receiver.getType()) != 0) {
                coupon = giver.getCoupons().get(receiver.getType()) * amount;
                giver.getCoupons().put(receiver.getType(), -1.0);
            }

            double commission;
            if (!giver.getCurrency().equals("RON")) {
                double exchangeRate = bank.getExchangeRate(giver.getCurrency(), "RON");
                commission = giverUser.getServicePlan().getComissionRate(command.getAmount() * exchangeRate);
            } else {
                commission = giverUser.getServicePlan().getComissionRate(command.getAmount());
            }

            if (giver.getBalance() - amount - commission <= giver.getMinBalance()) {
                description = "Insufficient funds";
                return;
            }

            double cashback;
            if (!giver.getCurrency().equals("RON")) {
                double exchangeRate = bank.getExchangeRate(giver.getCurrency(), "RON");
                cashback = receiver.getCashbackStrategy().calculateCashback(receiver, giver, giverUser, command.getAmount() * exchangeRate);
            } else {
                cashback = receiver.getCashbackStrategy().calculateCashback(receiver, giver, giverUser, command.getAmount());
            }

            double exchangeRate2 = 1;
            if (!giver.getCurrency().equals("RON")) {
                exchangeRate2 = bank.getExchangeRate("RON", giver.getCurrency());
            }

            cashback = cashback * exchangeRate2 + coupon;

            if (giver.getType().equals("business")) {
                BusinessAccount business = (BusinessAccount) giver;
                System.out.println(giverUser.getEmail());
                if (business.getEmployees().containsKey(command.getEmail())) {
                    if (amount + commission > business.getSpendingLimit())
                        return;
                    double initialAmount = business.getEmployees().get(giverUser.getEmail()).getSpent();
                    business.getEmployees().get(giverUser.getEmail()).setSpent(initialAmount + amount);
                    business.setTotalSpent(business.getTotalSpent() + amount);

                    business.getBusinessCommerciants().putIfAbsent(receiver.getCommerciant(), new BusinessCommerciant(receiver.getCommerciant()));

                    BusinessCommerciant comm = business.getBusinessCommerciants().get(receiver.getCommerciant());
                    comm.getEmployees().add(business.getEmployees().get(giverUser.getEmail()).getUsername());
                    comm.setTotalReceived(comm.getTotalReceived() + amount);
                } else if (business.getManagers().containsKey(giverUser.getEmail())) {
                    double initialAmount = business.getManagers().get(giverUser.getEmail()).getSpent();
                    business.getManagers().get(giverUser.getEmail()).setSpent(initialAmount + amount);
                    business.setTotalSpent(business.getTotalSpent() + amount);

                    business.getBusinessCommerciants().putIfAbsent(receiver.getCommerciant(), new BusinessCommerciant(receiver.getCommerciant()));

                    BusinessCommerciant comm = business.getBusinessCommerciants().get(receiver.getCommerciant());
                    comm.getManagers().add(business.getManagers().get(giverUser.getEmail()).getUsername());
                    comm.setTotalReceived(comm.getTotalReceived() + amount);
                }
            }

            giver.setBalance(giver.getBalance() - command.getAmount() - commission * command.getAmount() + cashback);

            if (amount / exchangeRate2 + amount * commission / exchangeRate2 > 300 && giverUser.getBigTransactions() < 5)
                giverUser.setBigTransactions(giverUser.getBigTransactions() + 1);
        }

        if (giver.getType().equals("business")) {
            BusinessAccount business = (BusinessAccount) giver;
            if (business.getOwner().getUser() == giverUser) {
                giverUser.getTransactions().add(this);
                if (giverUser.getBigTransactions() == 5 && giverUser.getServicePlan().getPlan().equals("silver")) {
                    giverUser.setBigTransactions(6);
                    command.setAccount(giver.getIban());
                    command.setNewPlanType("gold");
                    giverUser.getTransactions().add(new UpgradePlanTransaction(command, giverUser, giver, bank, output, 1));
                }
            }
        } else {
            giverUser.getTransactions().add(this);
            if (giverUser.getBigTransactions() == 5 && giverUser.getServicePlan().getPlan().equals("silver")) {
                giverUser.setBigTransactions(6);
                command.setAccount(giver.getIban());
                command.setNewPlanType("gold");
                giverUser.getTransactions().add(new UpgradePlanTransaction(command, giverUser, giver, bank, output, 1));
            }
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
    public Seller getReceiver() {
        return receiver;
    }

    /**
     * Sets the receiver's account.
     *
     * @param receiver the receiver's account to set.
     */
    public void setReceiver(final Seller receiver) {
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

package org.poo.transactions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.accounts.BusinessAccount;
import org.poo.accounts.ClassicAccount;
import org.poo.business.BusinessCommerciant;
import org.poo.cards.Card;
import org.poo.commerciants.Seller;
import org.poo.exchangeRates.Bnr;
import org.poo.exchangeRates.ExchangeRate;
import org.poo.fileio.CommandInput;
import org.poo.users.User;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PayOnlineTransaction implements TransactionStrategy {
    private String description;
    private int timestamp;
    private Double amount;
    private String commerciant;

    @JsonIgnore
    private CommandInput command;
    @JsonIgnore
    private User currentUser;
    @JsonIgnore
    private Seller seller;
    @JsonIgnore
    private Card card;
    @JsonIgnore
    private ArrayNode output;
    @JsonIgnore
    private Bnr bank;

    /**
     * Constructs a new {@code PayOnlineTransaction} with the given command, output, bank,
     * and user.
     *
     * @param command the command input containing transaction details.
     * @param output the output to store the results of the transaction.
     * @param bank the bank responsible for exchange rate handling.
     * @param currentUser the user performing the transaction.
     */
    public PayOnlineTransaction(
            final CommandInput command,
            final ArrayNode output,
            final Bnr bank,
            final Seller seller,
            final User currentUser
    ) {
        this.command = command;
        this.output = output;
        this.bank = bank;
        this.seller = seller;
        this.currentUser = currentUser;
        this.timestamp = command.getTimestamp();
    }

    /**
     * Executes the transaction by checking the card's status, performing currency conversion
     * if needed, and updating the account balance. It also adds the transaction to the user's
     * transaction history.
     */
    public void makeTransaction() {
        System.out.println("Pay online at " + timestamp);
        ClassicAccount account = pickCard(command.getCardNumber());
            if (account == null) {
                CheckCardStatusTransaction.printError(command, "Card not found", timestamp, output);
            } else if (seller == null) {
                System.out.println("Nu a gasit sellerul la timestamp " + timestamp);
            } else {
                if (command.getAmount() == 0.0) {
                    return;
                }
                String currency = account.getCurrency();
                double amount;

                System.out.println("User is " + command.getEmail());
                if (description != null) {
                    currentUser.getTransactions().add(this);
                    return;
                }

                double exchangeRate1;
                if (!currency.equals(command.getCurrency())) {
                    exchangeRate1 = bank.getExchangeRate(command.getCurrency(), currency);
                    bank.getExchangeRates().add(new ExchangeRate(command.getCurrency(),
                            currency, exchangeRate1));
                    amount = command.getAmount() * exchangeRate1;
                } else {
                    amount = command.getAmount();
                }

                System.out.println("Amount is " + amount);

                if (account.getType().equals("business")) {
                    BusinessAccount business = (BusinessAccount) account;
                    if (business.getEmployees().containsKey(command.getEmail()) && amount > business.getSpendingLimit()) {
                        return;
                    }
                }

                double coupon = 0.0;
                System.out.println("Valoarea in hash este de " + account.getCoupons().get(seller.getType()));
                if (account.getCoupons().get(seller.getType()) != -1.0 && account.getCoupons().get(seller.getType()) != 0) {
                    coupon = account.getCoupons().get(seller.getType()) * amount;
                    account.getCoupons().put(seller.getType(), -1.0);
                }

                System.out.println("Coupon is " + coupon);

                double commission;
                if (!account.getCurrency().equals("RON")) {
                    double exchangeRate = bank.getExchangeRate(account.getCurrency(), "RON");
                    if (account.getType().equals("business")) {
                        BusinessAccount business = (BusinessAccount) account;
                        commission = business.getOwner().getUser().getServicePlan().getComissionRate(amount * exchangeRate);
                    } else {
                        commission = currentUser.getServicePlan().getComissionRate(amount * exchangeRate);
                    }
                } else {
                    if (account.getType().equals("business")) {
                        BusinessAccount business = (BusinessAccount) account;
                        commission = business.getOwner().getUser().getServicePlan().getComissionRate(amount);
                    } else {
                        commission = currentUser.getServicePlan().getComissionRate(amount);
                    }
                }

                System.out.println("Commission is " + commission * amount + " pentru cont de tipul " + currentUser.getServicePlan().getPlan());

                int cardChanged = 0;

                if (account.getBalance() - amount - commission * amount <= account.getMinBalance()) {
                    description = "Insufficient funds";
                } else {
                    double cashback;
                    if (!account.getCurrency().equals("RON")) {
                        double exchangeRate = bank.getExchangeRate(account.getCurrency(), "RON");
                        if (account.getType().equals("business")) {
                            BusinessAccount business = (BusinessAccount) account;
                            cashback = seller.getCashbackStrategy().calculateCashback(seller, account, business.getOwner().getUser(), amount * exchangeRate);
                        } else {
                            cashback = seller.getCashbackStrategy().calculateCashback(seller, account, currentUser, amount * exchangeRate);
                        }
                    } else {
                        if (account.getType().equals("business")) {
                            BusinessAccount business = (BusinessAccount) account;
                            cashback = seller.getCashbackStrategy().calculateCashback(seller, account, business.getOwner().getUser(), amount);
                        } else {
                            cashback = seller.getCashbackStrategy().calculateCashback(seller, account, currentUser, amount);
                        }
                    }

                    double exchangeRate2 = 1;
                    if (!currency.equals("RON")) {
                        exchangeRate2 = bank.getExchangeRate("RON", account.getCurrency());
                    }


                    cashback = cashback * exchangeRate2 + coupon;
                    System.out.println("Account has currency " + currency);
                    System.out.println("Cashback is " + cashback);
                    System.out.println("Type is " + seller.getCashbackType() + "\n");

                    account.setBalance(account.getBalance() - amount - amount * commission + cashback);

                    if (amount / exchangeRate2 + amount * commission / exchangeRate2 >= 300 && currentUser.getBigTransactions() < 5)
                        currentUser.setBigTransactions(currentUser.getBigTransactions() + 1);

                    System.out.println("Userul " + currentUser.getEmail() + " are " + currentUser.getBigTransactions());

                    this.amount = amount;
                    commerciant = command.getCommerciant();

                    if (account.getType().equals("classic")) {
                        account.getCommerciants().getPayments().add(this);
                    } else if (account.getType().equals("business")) {
                        BusinessAccount business = (BusinessAccount) account;
                        if (business.getEmployees().containsKey(command.getEmail())) {
                            double initialAmount = business.getEmployees().get(currentUser.getEmail()).getSpent();
                            business.getEmployees().get(currentUser.getEmail()).setSpent(initialAmount + amount);
                            business.setTotalSpent(business.getTotalSpent() + amount);

                            business.getBusinessCommerciants().putIfAbsent(seller.getCommerciant(), new BusinessCommerciant(seller.getCommerciant()));

                            BusinessCommerciant comm = business.getBusinessCommerciants().get(seller.getCommerciant());
                            comm.getEmployees().add(business.getEmployees().get(currentUser.getEmail()).getUsername());
                            comm.setTotalReceived(comm.getTotalReceived() + amount);
                        } else if (business.getManagers().containsKey(command.getEmail())) {
                            double initialAmount = business.getManagers().get(currentUser.getEmail()).getSpent();
                            business.getManagers().get(currentUser.getEmail()).setSpent(initialAmount + amount);
                            business.setTotalSpent(business.getTotalSpent() + amount);

                            business.getBusinessCommerciants().putIfAbsent(seller.getCommerciant(), new BusinessCommerciant(seller.getCommerciant()));

                            BusinessCommerciant comm = business.getBusinessCommerciants().get(seller.getCommerciant());
                            comm.getManagers().add(business.getManagers().get(currentUser.getEmail()).getUsername());
                            comm.setTotalReceived(comm.getTotalReceived() + amount);
                        }
                    }

                    description = "Card payment";
                    cardChanged = card.useCard(account.getIban(), currentUser,
                            currentUser.getEmail(), timestamp);
                }

                if (cardChanged == 1) {
                    currentUser.getTransactions().add(currentUser.getTransactions().size() - 2,
                            this);
                    if (currentUser.getBigTransactions() == 5 && currentUser.getServicePlan().getPlan().equals("silver")) {
                        currentUser.setBigTransactions(6);
                        command.setAccount(account.getIban());
                        command.setNewPlanType("gold");
                        currentUser.getTransactions().add(currentUser.getTransactions().size() - 2,
                                new UpgradePlanTransaction(command, currentUser, account, bank, output, 1));
                    }
                } else {
                    currentUser.getTransactions().add(this);
                    if (currentUser.getBigTransactions() == 5 && currentUser.getServicePlan().getPlan().equals("silver")) {
                        currentUser.setBigTransactions(6);
                        command.setAccount(account.getIban());
                        command.setNewPlanType("gold");
                        currentUser.getTransactions().add(new UpgradePlanTransaction(command, currentUser, account, bank, output, 1));
                    }
                }
            }
    }

    /**
     * Searches for a card in the user's accounts and checks its status. If the card is found,
     * it returns the account linked to the card.
     *
     * @param cardNumber the card number to search for.
     * @return the account associated with the card, or {@code null} if the card is not found.
     */
    private ClassicAccount pickCard(final String cardNumber) {
        for (ClassicAccount account : currentUser.getAccounts()) {
            for (int i = 0; i < account.getCards().size(); i++) {
                if (account.getCards().get(i).getCardNumber().equals(cardNumber)) {
                    if (account.getCards().get(i).getStatus().equals("frozen")) {
                        description = "The card is frozen";
                    }
                    if (account.getType().equals("classic")) {
                        account.getTransactions().add(this);
                    }
                    card = account.getCards().get(i);
                    return account;
                }
            }
        }

        return null;
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
     * Sets the description of the transaction.
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
     * Gets the command input associated with the transaction.
     *
     * @return the command input.
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
     * Gets the output associated with the transaction.
     *
     * @return the output.
     */
    public ArrayNode getOutput() {
        return output;
    }

    /**
     * Sets the output for the transaction.
     *
     * @param output the output to set.
     */
    public void setOutput(final ArrayNode output) {
        this.output = output;
    }

    /**
     * Gets the amount involved in the transaction.
     *
     * @return the amount.
     */
    public Double getAmount() {
        return amount;
    }

    /**
     * Sets the amount for the transaction.
     *
     * @param amount the amount to set.
     */
    public void setAmount(final double amount) {
        this.amount = amount;
    }

    /**
     * Gets the commerciant (merchant) for the transaction.
     *
     * @return the commerciant.
     */
    public String getCommerciant() {
        return commerciant;
    }

    /**
     * Sets the commerciant for the transaction.
     *
     * @param commerciant the commerciant to set.
     */
    public void setCommerciant(final String commerciant) {
        this.commerciant = commerciant;
    }

}

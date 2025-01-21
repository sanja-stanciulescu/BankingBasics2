package org.poo.app;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.accounts.BusinessAccount;
import org.poo.accounts.ClassicAccount;
import org.poo.cards.Card;
import org.poo.commerciants.Seller;
import org.poo.exchangeRates.Bnr;
import org.poo.fileio.CommandInput;
import org.poo.fileio.ObjectInput;
import org.poo.transactions.*;
import org.poo.transactions.split_payment.*;
import org.poo.users.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AppManager {
    private ArrayList<User> allUsers;
    private Map<String, Seller> allSellers;
    private Bnr bank;
    private IBANRegistry registry;
    private Finder finder;

    /**
     * Constructs an instance of {@code AppManager} and initializes its fields.
     */
    public AppManager() {
        allUsers = new ArrayList<>();
        allSellers = new HashMap<>();
        bank = new Bnr();
        registry = new IBANRegistry();
        finder = new Finder();
    }

    /**
     * Starts the application, initializes users, sets up the bank exchange rates,
     * and processes a list of commands.
     *
     * @param output    the {@code ArrayNode} to store the output of processed commands.
     * @param inputData the {@code ObjectInput} containing user data, exchange rates, and commands.
     */
    public void start(final ArrayNode output, final ObjectInput inputData) {
        //Initialize the list of users
        for (int i = 0; i < inputData.getUsers().length; i++) {
            allUsers.add(new User(inputData.getUsers()[i]));
        }

        //Initialize the list of commerciants
        for (int i = 0; i < inputData.getCommerciants().length; i++) {
            allSellers.put(inputData.getCommerciants()[i].getCommerciant(),
                    new Seller(inputData.getCommerciants()[i]));
        }

        //Initialize the board where exchange rates are showcased
        bank.setUp(inputData);

        //Parse the commands
        CommandInput[] commands = inputData.getCommands();
        TransactionStrategy transaction;
        for (CommandInput command : commands) {
            transaction = useTransactionFactory(output, command);
            if (transaction != null) {
                transaction.makeTransaction();
            }
        }
    }

    /**
     * Creates a {@code TransactionStrategy} object based on the specified command
     * and executes the associated operation.
     *
     * @param output  the {@code ArrayNode} to store the results of certain commands.
     * @param command the {@code CommandInput} containing details about the requested operation.
     * @return a {@code TransactionStrategy} object representing the operation,
     * or {@code null} if the command is invalid.
     */
    public TransactionStrategy useTransactionFactory(
            final ArrayNode output,
            final CommandInput command
    ) {
        TransactionStrategy transaction = null;
        User currentUser;
        ClassicAccount currentAccount;
        switch (command.getCommand()) {
            case "printUsers":
                transaction = new PrintUserTransaction(command, output, allUsers);
                break;
            case "addAccount":
                searchUserByEmail(command.getEmail());
                transaction = new AddAccountTransaction(command, registry, finder.getUser(), bank);
                break;
            case "createCard", "createOneTimeCard":
                searchUserByEmail(command.getEmail());
                transaction = new AddCardTransaction(command, finder.getUser());
                break;
            case "addFunds":
                searchByIban(command.getAccount());
                transaction = new AddFundsTransaction(command, finder.getAccount());
                break;
            case "deleteAccount":
                searchUserByEmail(command.getEmail());
                transaction = new DeleteAccountTransaction(command, output, finder.getUser());
                break;
            case "deleteCard":
                searchByCard(command.getCardNumber());
                transaction = new DeleteCardTransaction(command, finder.getAccount(),
                                                        finder.getUser());
                break;
            case "setMinimumBalance":
                searchByIban(command.getAccount());
                transaction = new MinBalanceTransaction(command, output, finder.getUser(),
                                                        finder.getAccount());
                break;
            case "withdrawSavings":
                searchByIban(command.getAccount());
                transaction = new WithdrawSavingsTransaction(command, bank, finder.getUser(), finder.getAccount());
                break;
            case "payOnline":
                searchUserByEmail(command.getEmail());
                transaction = new PayOnlineTransaction(command, output, bank,
                                                        allSellers.get(command.getCommerciant()),
                                                        finder.getUser());
                break;
            case "sendMoney":
                searchByIban(registry.getIBAN(command.getAccount()));
                currentAccount = finder.getAccount();
                currentUser = finder.getUser();
                searchByIban(registry.getIBAN(command.getReceiver()));
                if (finder.getAccount() == null || finder.getUser() == null) {
                    Seller seller = searchForCommerciant(command.getReceiver());
                    transaction = new SendMoneyToCommerciantTransaction(command, currentAccount, currentUser, seller, bank, output);
                    break;
                }
                transaction = new SendMoneyTransaction(command, currentAccount, currentUser,
                                                       finder.getAccount(), finder.getUser(), bank, output);
                break;
            case "setAlias":
                searchByIban(command.getAccount());
                transaction = new SetAliasTransaction(command, registry, finder.getAccount());
                break;
            case "printTransactions":
                searchUserByEmail(command.getEmail());
                transaction = new PrintTransTransaction(command, output, finder.getUser());
                break;
            case "checkCardStatus":
                searchByCard(command.getCardNumber());
                transaction = new CheckCardStatusTransaction(command, output, finder.getUser(),
                                                             finder.getAccount(), finder.getCard());
                break;
            case "changeInterestRate":
                searchByIban(command.getAccount());
                transaction = new ChangeInterestTransaction(command, output,
                                                            finder.getUser(), finder.getAccount());
                break;
            case "addInterest":
                searchByIban(command.getAccount());
                transaction = new AddInterestTransaction(command, output, finder.getUser(), finder.getAccount());
                break;
            case "splitPayment":
                ArrayList<Finder> finders = new ArrayList<>();
                for (int i = 0; i < command.getAccounts().size(); i++) {
                    finders.add(new Finder());
                    searchByIban(command.getAccounts().get(i));
                    finders.get(i).setUser(finder.getUser());
                    finders.get(i).setAccount(finder.getAccount());
                    finders.get(i).setCard(finder.getCard());
                }
                transaction = new SplitPaymentTransaction(command, finders, bank);
                break;
            case "acceptSplitPayment":
                searchUserByEmail(command.getEmail());
                if (finder.getUser() == null) {
                    CheckCardStatusTransaction.printError(command, "User not found", command.getTimestamp(), output);
                    break;
                }
                Command acceptCommand = new AcceptSplitPayment(command.getEmail(),
                        finder.getUser().getActiveTransactions().stream()
                                .filter((tran) -> tran.getSplitPaymentType()
                                                .equals(command.getSplitPaymentType()))
                                .findFirst().orElse(null));
                transaction = new CommandAdapter(acceptCommand);
                break;
            case "rejectSplitPayment":
                searchUserByEmail(command.getEmail());
                if (finder.getUser() == null) {
                    CheckCardStatusTransaction.printError(command, "User not found", command.getTimestamp(), output);
                    break;
                }
                Command rejectCommand = new RejectSplitPayment(command.getEmail(),
                        finder.getUser().getActiveTransactions().stream()
                                .filter((tran) -> tran.getSplitPaymentType()
                                        .equals(command.getSplitPaymentType()))
                                .findFirst().orElse(null));
                transaction = new CommandAdapter(rejectCommand);
                break;
            case "report":
                searchByIban(command.getAccount());
                transaction = new ReportTransaction(command, output, finder.getAccount());
                break;
            case "spendingsReport":
                searchByIban(command.getAccount());
                transaction = new SpendingsReportTransaction(command, output, finder.getAccount());
                break;
            case "businessReport":
                searchByIban(command.getAccount());
                if (command.getType().equals("transaction")) {
                    transaction = new TransactionBusinessReport(command, (BusinessAccount) finder.getAccount(), output);
                } else {
                    transaction = new CommerciantBusinessReport(command, (BusinessAccount) finder.getAccount(), output);
                }
                break;
            case "upgradePlan":
                searchByIban(command.getAccount());
                transaction = new UpgradePlanTransaction(command, finder.getUser(), finder.getAccount(), bank, output, 0);
                break;
            case "cashWithdrawal":
                if (command.getEmail().isEmpty()) {
                    CheckCardStatusTransaction.printError(command, "User not found", command.getTimestamp(), output);
                    break;
                }
                searchByCard(command.getCardNumber());
                transaction = new CashWithdrawTransaction(command, finder.getUser(), finder.getAccount(), finder.getCard(), output, bank);
                break;
            case "addNewBusinessAssociate":
                searchByIban(command.getAccount());
                User owner = finder.getUser();
                BusinessAccount business = (BusinessAccount) finder.getAccount();
                searchUserByEmail(command.getEmail());
                transaction = new AddNewBusinessAssociateTransaction(command, owner, business, finder.getUser());
                break;
            case "changeSpendingLimit", "changeDepositLimit":
                searchByIban(command.getAccount());
                BusinessAccount account = null;
                if (finder.getAccount().getType().equals("business")) {
                    account = (BusinessAccount) finder.getAccount();
                } else {
                    CheckCardStatusTransaction.printError(command, "This is not a business account", command.getTimestamp(), output);
                    break;
                }

                searchUserByEmail(command.getEmail());
                transaction = new ChangeSpendingLimitTransaction(command, finder.getUser(), account, output);
                break;
            default:
                System.out.println("Invalid command");
        }

        return transaction;
    }

    /**
     * Searches for a user by their email address and sets the result in the {@code Finder} object.
     *
     * @param email the email address of the user to search for.
     */
    private void searchUserByEmail(final String email) {
        for (User user : allUsers) {
            if (user.getEmail().equals(email)) {
                finder.setUser(user);
                return;
            }
        }
        finder.setUser(null);
    }

    /**
     * Searches for an account by its IBAN and sets the corresponding user and
     * account in the {@code Finder} object.
     *
     * @param iban the IBAN of the account to search for.
     */
    private void searchByIban(final String iban) {
        for (User user : allUsers) {
            for (ClassicAccount account : user.getAccounts()) {
                if (account.getIban().equals(iban)) {
                    finder.setUser(user);
                    finder.setAccount(account);
                    return;
                }
            }
        }

        finder.setUser(null);
        finder.setAccount(null);
    }

    /**
     * Searches for a card by its card number and sets the corresponding user, account,
     * and card in the {@code Finder} object.
     *
     * @param cardNumber the card number to search for.
     */
    private void searchByCard(final String cardNumber) {
        for (User user : allUsers) {
            for (ClassicAccount account : user.getAccounts()) {
                for (Card card : account.getCards()) {
                    if (card.getCardNumber().equals(cardNumber)) {
                        finder.setUser(user);
                        finder.setAccount(account);
                        finder.setCard(card);
                        return;
                    }
                }
            }
        }

        finder.setUser(null);
        finder.setAccount(null);
        finder.setCard(null);
    }

    private Seller searchForCommerciant(final String iban) {
        for (Map.Entry<String, Seller> entry : allSellers.entrySet()) {
            if (entry.getValue().getIban().equals(iban)) {
                return entry.getValue();
            }
        }
        return null;
    }
}

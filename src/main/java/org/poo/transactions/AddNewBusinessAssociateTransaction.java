package org.poo.transactions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.poo.accounts.BusinessAccount;
import org.poo.business.Employee;
import org.poo.business.Manager;
import org.poo.fileio.CommandInput;
import org.poo.users.User;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddNewBusinessAssociateTransaction implements TransactionStrategy {
    private int timestamp;

    @JsonIgnore
    private BusinessAccount businessAccount;
    @JsonIgnore
    private User user;
    @JsonIgnore
    private CommandInput command;
    @JsonIgnore
    private int employeeOrder;
    @JsonIgnore
    private int managerOrder;

    /**
     * Creates an instance of {@code AddNewBusinessAssociateTransaction}, used to add a new
     * business associate (employee or manager) to a specified business account based on the
     * provided command details.
     *
     * @param command the {@code CommandInput} object that contains details about the transaction,
     *                including the role of the associate (e.g., employee or manager) and the
     *                timestamp.
     * @param account the {@code BusinessAccount} to which the new associate will be added.
     * @param user the {@code User} object representing the individual to be added as a business
     *             associate (employee or manager).
     */
    public AddNewBusinessAssociateTransaction(
            final CommandInput command,
            final BusinessAccount account,
            final User user
    ) {
        this.command = command;
        this.user = user;
        this.businessAccount = account;
        timestamp = command.getTimestamp();
        employeeOrder = 0;
        managerOrder = 0;
    }

    /**
     * Handles the logic to add a new business associate (either an employee or a manager)
     * to the specified business account based on the role defined in the command.
     *
     * This method performs the following actions:
     * - If the role is "employee" and the user is neither a manager nor the owner of the
     *   business account, the user is added to the employee list of the business account.
     *   Additionally, the business account is added to the user's account list.
     * - If the role is "manager" and the user is neither an employee nor the owner of
     *   the business account, the user is added to the manager list of the business account.
     *   Similarly, the business account is added to the user's account list.
     *
     * The method checks for existing roles and ownership to prevent duplicate or
     * invalid associations.
     *
     * Roles and respective order values are managed dynamically based on the size
     * of the respective manager or employee lists.
     */
    @Override
    public void makeTransaction() {
        if (command.getRole().equals("employee")) {
            if (businessAccount.getManagers().containsKey(user.getEmail())
                    || businessAccount.getOwner().getUser() == user) {
                return;
            } else {
                employeeOrder = businessAccount.getEmployees().size();
                businessAccount.getEmployees().put(user.getEmail(),
                        new Employee(user, employeeOrder));
                user.getAccounts().add(businessAccount);
                employeeOrder++;
            }
        } else if (command.getRole().equals("manager")) {
            if (businessAccount.getEmployees().containsKey(user.getEmail())
                    || businessAccount.getOwner().getUser() == user) {
                return;
            } else {
                managerOrder = businessAccount.getManagers().size();
                businessAccount.getManagers().put(user.getEmail(), new Manager(user, managerOrder));
                user.getAccounts().add(businessAccount);
                managerOrder++;
            }
        }
    }

    /**
     * Retrieves the timestamp of the transaction.
     *
     * @return the timestamp representing the time the transaction occurred.
     */
    @Override
    public int getTimestamp() {
        return timestamp;
    }

    /**
     * Updates the timestamp value for the transaction.
     *
     * @param timestamp the new timestamp to be set, representing when the transaction occurred
     *                  in milliseconds since the epoch.
     */
    public void setTimestamp(final int timestamp) {
        this.timestamp = timestamp;
    }
}

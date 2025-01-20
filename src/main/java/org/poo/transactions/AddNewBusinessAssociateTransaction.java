package org.poo.transactions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.poo.accounts.BusinessAccount;
import org.poo.accounts.ClassicAccount;
import org.poo.business.Employee;
import org.poo.business.Manager;
import org.poo.fileio.CommandInput;
import org.poo.users.User;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddNewBusinessAssociateTransaction implements TransactionStrategy {
    private int timestamp;
    private String error;

    @JsonIgnore
    private User businessOwner;
    @JsonIgnore
    private BusinessAccount businessAccount;
    @JsonIgnore
    private User user;
    @JsonIgnore
    private CommandInput command;

    public AddNewBusinessAssociateTransaction(final CommandInput command, final User businessOwner, final BusinessAccount account, final User user) {
        this.command = command;
        this.user = user;
        this.businessOwner = businessOwner;
        this.businessAccount = account;
        timestamp = command.getTimestamp();
        error = null;
    }

    @Override
    public void makeTransaction() {
        if (command.getRole().equals("employee")) {
            if (businessAccount.getEmployees().containsKey(user.getEmail())) {
                error = "The user is already an associate of the account.";
            } else {
                businessAccount.getEmployees().put(user.getEmail(), new Employee(user));
                user.getAccounts().add(businessAccount);
            }
        } else if (command.getRole().equals("manager")) {
            if (businessAccount.getManagers().containsKey(user.getEmail())) {
                error = "The user is already an associate of the account.";
            } else {
                businessAccount.getManagers().put(user.getEmail(), new Manager(user));
                user.getAccounts().add(businessAccount);
            }
        }
    }

    @Override
    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }
}

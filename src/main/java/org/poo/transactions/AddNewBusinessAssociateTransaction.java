package org.poo.transactions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.poo.accounts.ClassicAccount;
import org.poo.fileio.CommandInput;
import org.poo.users.User;

public class AddNewBusinessAssociateTransaction implements TransactionStrategy {
    private int timestamp;

    @JsonIgnore
    private User user;
    @JsonIgnore
    private ClassicAccount account;
    @JsonIgnore
    private CommandInput command;

    public AddNewBusinessAssociateTransaction(final CommandInput command, final User user, final ClassicAccount account) {
        this.command = command;
        this.user = user;
        this.account = account;
    }

    @Override
    public void makeTransaction() {

    }

    @Override
    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }
}

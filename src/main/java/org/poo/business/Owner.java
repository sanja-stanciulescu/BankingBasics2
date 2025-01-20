package org.poo.business;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.poo.accounts.BusinessAccount;
import org.poo.users.User;

public class Owner extends Employee {
    @JsonIgnore
    private BusinessAccount account;


    public Owner(User user, BusinessAccount account) {
        super(user);
        this.account = account;
        permissions = 2;
    }

    public BusinessAccount getAccount() {
        return account;
    }

    public void setAccount(BusinessAccount account) {
        this.account = account;
    }
}

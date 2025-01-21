package org.poo.business;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.poo.accounts.BusinessAccount;
import org.poo.users.User;

public class Owner extends Employee {
    @JsonIgnore
    private BusinessAccount account;

    /**
     * Constructs a new Owner with the specified user and business account.
     *
     * @param user the User associated with the Owner
     * @param account the BusinessAccount associated with the Owner
     */
    public Owner(final User user, final BusinessAccount account) {
        super(user, 100);
        this.account = account;
        permissions = 2;
    }

    /**
     * Retrieves the business account associated with the owner.
     *
     * @return the {@code BusinessAccount} instance associated with this owner.
     */
    public BusinessAccount getAccount() {
        return account;
    }

    /**
     * Updates the business account associated with the owner.
     *
     * @param account the {@code BusinessAccount} to be associated with the owner
     */
    public void setAccount(final BusinessAccount account) {
        this.account = account;
    }
}

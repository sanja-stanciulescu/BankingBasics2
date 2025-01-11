package org.poo.commerciants;

import org.poo.transactions.PayOnlineTransaction;

import java.util.ArrayList;

public class Commerciant {
    private ArrayList<PayOnlineTransaction> payments;

    /**
     * Constructs a new {@code Commerciant} instance with an empty list of payments.
     */
   public Commerciant() {
       payments = new ArrayList<>();
   }

    /**
     * Retrieves the list of online payment transactions for the merchant.
     *
     * @return an {@code ArrayList} of {@code PayOnlineTransaction} objects
     * representing payments to the merchant.
     */
    public ArrayList<PayOnlineTransaction> getPayments() {
        return payments;
    }
}

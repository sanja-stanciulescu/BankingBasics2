package org.poo.servicePlan;

public interface Plan {
    /**
     * Calculates the commission rate for a given transaction amount.
     *
     * @param amount the transaction amount for which the commission rate is to be determined
     * @return the commission rate as a percentage (e.g., 0.05 for 5%)
     */
    default double getComissionRate(double amount) {
        return 0;
    }

    /**
     * Retrieves the name of the current plan.
     *
     * @return a string representing the name of the plan.
     */
    String getPlan();

    /**
     * Retrieves the unique identifier of the plan.
     *
     * @return the unique identifier of the plan as an integer.
     */
    int getId();
}

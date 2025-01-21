package org.poo.servicePlan;

public class SilverPlan implements Plan {
    private static final int SILVER_ID = 3;
    private static final double COMMISSION_RATE = 0.001;
    private static final double MAX_AMOUNT = 500.0;

    /**
     * Calculates the commission rate for a given transaction amount in the Silver plan.
     *
     * @param amount the transaction amount for which the commission rate is to be determined
     * @return the commission rate as a percentage (e.g., 0.001 for 0.1%) if the amount
     * is 500.0 or greater; otherwise, returns 0
     */
    @Override
    public double getComissionRate(final double amount) {
        if (Double.compare(amount, MAX_AMOUNT) >= 0) {
            return COMMISSION_RATE;
        }
        return 0;
    }

    /**
     * Retrieves the name of the current plan.
     *
     * @return a string representing the name of the current plan.
     */
    @Override
    public String getPlan() {
        return "silver";
    }

    /**
     * Retrieves the unique identifier of the plan.
     *
     * @return the unique identifier of the plan as an integer.
     */
    @Override
    public int getId() {
        return SILVER_ID;
    }
}

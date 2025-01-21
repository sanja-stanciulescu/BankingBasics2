package org.poo.servicePlan;

public class StandardPlan implements Plan {
    private static final int STANDARD_ID = 1;
    private static final double COMMISSION_RATE = 0.002;

    /**
     * Calculates the commission rate for a given transaction amount
     * based on the conditions of the StandardPlan.
     *
     * @param amount the transaction amount for which the commission rate is to be calculated
     * @return the commission rate as a percentage (e.g., 0.002 for 0.2%)
     */
    @Override
    public double getComissionRate(final double amount) {
        return COMMISSION_RATE;
    }

    /**
     * Retrieves the name of the current plan.
     *
     * @return a string representing the name of the plan.
     */
    @Override
    public String getPlan() {
        return "standard";
    }

    /**
     * Retrieves the unique identifier of the standard plan.
     *
     * @return the unique identifier of the plan as an integer.
     */
    @Override
    public int getId() {
        return STANDARD_ID;
    }
}

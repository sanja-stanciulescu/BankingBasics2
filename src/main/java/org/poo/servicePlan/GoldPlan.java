package org.poo.servicePlan;

public class GoldPlan implements Plan {
    private static final int GOLD_ID = 4;
    /**
     * Retrieves the name of the subscription plan.
     *
     * @return the name of the plan as a string
     */
    @Override
    public String getPlan() {
        return "gold";
    }

    /**
     * Retrieves the unique identifier for the plan.
     *
     * @return the unique identifier of the plan as an integer
     */
    @Override
    public int getId() {
        return GOLD_ID;
    }
}

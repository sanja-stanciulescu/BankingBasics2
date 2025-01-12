package org.poo.servicePlan;

public class GoldPlan implements Plan {
    @Override
    public String getPlan() {
        return "gold";
    }

    @Override
    public int getId() {
        return 4;
    }
}

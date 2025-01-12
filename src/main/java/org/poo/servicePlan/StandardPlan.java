package org.poo.servicePlan;

public class StandardPlan implements Plan {

    @Override
    public double getComissionRate(double amount) {
        return 0.2 / 100;
    }

    @Override
    public String getPlan() {
        return "standard";
    }

    @Override
    public int getId() {
        return 1;
    }
}

package org.poo.servicePlan;

public class SilverPlan implements Plan {
    @Override
    public double getComissionRate(double amount) {
        if (Double.compare(amount, 500.0) >= 0)
            return 0.1 / 100;
        return 0;
    }

    @Override
    public String getPlan() {
        return "silver";
    }

    @Override
    public int getId() {
        return 3;
    }
}

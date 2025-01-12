package org.poo.servicePlan;

public interface Plan {
    default double getComissionRate(double amount) {
        return 0;
    }

    String getPlan();

    int getId();
}

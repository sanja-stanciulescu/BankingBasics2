package org.poo.servicePlan;

public class StudentPlan implements Plan{

    public String getPlan() {
        return "student";
    }

    @Override
    public int getId() {
        return 2;
    }
}

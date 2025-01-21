package org.poo.servicePlan;

public class StudentPlan implements Plan {
    private static final int STUDENT_ID = 2;

    /**
     * Retrieves the name of the current plan.
     *
     * @return a string representing the name of the plan, in this case, "student".
     */
    public String getPlan() {
        return "student";
    }

    /**
     * Retrieves the unique identifier of the plan.
     *
     * @return the unique identifier of the plan as an integer.
     */
    @Override
    public int getId() {
        return STUDENT_ID;
    }
}

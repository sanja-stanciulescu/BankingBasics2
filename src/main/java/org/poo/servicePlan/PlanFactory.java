package org.poo.servicePlan;

public class PlanFactory {
    /**
     * Creates a plan instance based on the specified type.
     *
     * @param type the type of plan to create. Valid values are "standard", "student",
     *             "silver", and "gold".
     *             If the type does not match any of these values, the method will return null.
     * @return a Plan instance corresponding to the specified type, or null if the type is invalid.
     */
    public static Plan createPlan(final String type) {
        Plan plan;
        switch (type) {
            case "standard":
                plan = new StandardPlan();
                break;
            case "student":
                plan = new StudentPlan();
                break;
            case "silver":
                plan = new SilverPlan();
                break;
            case "gold":
                plan = new GoldPlan();
                break;
            default:
                plan = null;
        }

        return plan;
    }
}

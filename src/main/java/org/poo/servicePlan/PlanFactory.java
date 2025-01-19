package org.poo.servicePlan;

public class PlanFactory {
    public static Plan createPlan(String type) {
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

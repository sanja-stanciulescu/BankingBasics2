package org.poo.business;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class BusinessCommerciant {
    private String commerciant;
    private List<String> employees;
    private List<String> managers;
    @JsonProperty("total received")
    private double totalReceived;

    /**
     * Constructs a BusinessCommerciant instance using the specified commerciant name.
     *
     * @param commerciant the name of the commerciant initializing the business
     */
    public BusinessCommerciant(final String commerciant) {
        this.commerciant = commerciant;
        this.employees = new ArrayList<>();
        this.managers = new ArrayList<>();
        this.totalReceived = 0;
    }

    /**
     * Retrieves the name of the commerciant associated with this business entity.
     *
     * @return the name of the commerciant.
     */
    public String getCommerciant() {
        return commerciant;
    }

    /**
     * Sets the name of the commerciant associated with this business entity.
     *
     * @param commerciant the name of the commerciant to set.
     */
    public void setCommerciant(final String commerciant) {
        this.commerciant = commerciant;
    }

    /**
     * Retrieves the list of employees associated with the business.
     *
     * @return a list of employee names.
     */
    public List<String> getEmployees() {
        return employees;
    }

    /**
     * Updates the list of employees associated with the business.
     *
     * @param employees the list of employee names to set
     */
    public void setEmployees(final List<String> employees) {
        this.employees = employees;
    }

    /**
     * Retrieves the list of manager names associated with the business.
     *
     * @return a list of manager names.
     */
    public List<String> getManagers() {
        return managers;
    }

    /**
     * Updates the list of managers associated with the business.
     *
     * @param managers the list of manager names to set
     */
    public void setManagers(final List<String> managers) {
        this.managers = managers;
    }

    /**
     * Retrieves the total amount received by the business.
     *
     * @return the total amount received as a double.
     */
    public double getTotalReceived() {
        return totalReceived;
    }

    /**
     * Updates the total amount received by the business commerciant.
     *
     * @param totalReceived the total amount received to set, represented as a double.
     */
    public void setTotalReceived(final double totalReceived) {
        this.totalReceived = totalReceived;
    }
}

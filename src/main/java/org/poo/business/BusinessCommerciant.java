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

    public BusinessCommerciant(final String commerciant) {
        this.commerciant = commerciant;
        this.employees = new ArrayList<>();
        this.managers = new ArrayList<>();
        this.totalReceived = 0;
    }

    public String getCommerciant() {
        return commerciant;
    }

    public void setCommerciant(String commerciant) {
        this.commerciant = commerciant;
    }

    public List<String> getEmployees() {
        return employees;
    }

    public void setEmployees(List<String> employees) {
        this.employees = employees;
    }

    public List<String> getManagers() {
        return managers;
    }

    public void setManagers(List<String> managers) {
        this.managers = managers;
    }

    public double getTotalReceived() {
        return totalReceived;
    }

    public void setTotalReceived(double totalReceived) {
        this.totalReceived = totalReceived;
    }
}

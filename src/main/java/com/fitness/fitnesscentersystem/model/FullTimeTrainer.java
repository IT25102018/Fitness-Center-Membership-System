package com.fitness.fitnesscentersystem.model;

// Inheritance: FullTimeTrainer extends Trainer
public class FullTimeTrainer extends Trainer {

    private String contractStartDate;  // format: YYYY-MM-DD
    private int    annualLeaveDays;
    private String department;         // e.g. Cardio, Strength, Wellness

    public FullTimeTrainer() {
        super();
    }

    public FullTimeTrainer(String trainerId, String name, String email,
                           String phone, String specialization,
                           String availability, double salary, String status,
                           String contractStartDate, int annualLeaveDays,
                           String department) {
        super(trainerId, name, email, phone,
                specialization, availability, salary, status);
        this.contractStartDate = contractStartDate;
        this.annualLeaveDays   = annualLeaveDays;
        this.department        = department;
    }

    public String getContractStartDate()                      { return contractStartDate; }
    public void   setContractStartDate(String contractStart)  { this.contractStartDate = contractStart; }

    public int  getAnnualLeaveDays()                          { return annualLeaveDays; }
    public void setAnnualLeaveDays(int annualLeave)           { this.annualLeaveDays = annualLeave; }

    public String getDepartment()                             { return department; }
    public void   setDepartment(String department)            { this.department = department; }

    // Polymorphism: override toString from Trainer
    @Override
    public String toString() {
        return super.toString()
                + ", department=" + department
                + ", contractStart=" + contractStartDate
                + ", leaveDays=" + annualLeaveDays;
    }

    // Abstraction: full-time benefit summary hidden from caller
    public String getBenefitsSummary() {
        return "Department: " + department
                + " | Contract From: " + contractStartDate
                + " | Annual Leave: " + annualLeaveDays + " days";
    }
}

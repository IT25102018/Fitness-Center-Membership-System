package com.fitness.fitnesscentersystem.model;

public class MembershipPlan {

    private String planId;
    private String planName;      // BASIC, PREMIUM, VIP
    private String description;
    private double price;
    private int durationMonths;
    private String features;      // comma-separated features

    public MembershipPlan() {}

    public MembershipPlan(String planId, String planName, String description,
                          double price, int durationMonths, String features) {
        this.planId = planId;
        this.planName = planName;
        this.description = description;
        this.price = price;
        this.durationMonths = durationMonths;
        this.features = features;
    }

    // Getters & Setters
    public String getPlanId()                        { return planId; }
    public void   setPlanId(String planId)           { this.planId = planId; }

    public String getPlanName()                      { return planName; }
    public void   setPlanName(String planName)       { this.planName = planName; }

    public String getDescription()                   { return description; }
    public void   setDescription(String description) { this.description = description; }

    public double getPrice()                         { return price; }
    public void   setPrice(double price)             { this.price = price; }

    public int getDurationMonths()                   { return durationMonths; }
    public void setDurationMonths(int durationMonths){ this.durationMonths = durationMonths; }

    public String getFeatures()                      { return features; }
    public void   setFeatures(String features)       { this.features = features; }

    // Save to file as CSV
    public String toFileString() {
        return planId + "|" + planName + "|" + description + "|"
                + price + "|" + durationMonths + "|" + features;
    }

    // Parse from file CSV line
    public static MembershipPlan fromFileString(String line) {
        String[] p = line.split("\\|", -1);
        MembershipPlan plan = new MembershipPlan();
        plan.setPlanId(p[0]);
        plan.setPlanName(p[1]);
        plan.setDescription(p[2]);
        plan.setPrice(Double.parseDouble(p[3]));
        plan.setDurationMonths(Integer.parseInt(p[4]));
        plan.setFeatures(p[5]);
        return plan;
    }

    @Override
    public String toString() {
        return "MembershipPlan{id=" + planId + ", name=" + planName
                + ", price=" + price + ", duration=" + durationMonths + "mo}";
    }
}

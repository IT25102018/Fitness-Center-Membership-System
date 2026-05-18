package com.fitness.fitnesscentersystem.model;

// Inheritance: PremiumPlan extends MembershipPlan
public class PremiumPlan extends MembershipPlan {

    private boolean hasPersonalTrainer;
    private int guestPassesPerMonth;

    public PremiumPlan() {
        super();
    }

    public PremiumPlan(String planId, String planName, String description,
                       double price, int durationMonths, String features,
                       boolean hasPersonalTrainer, int guestPassesPerMonth) {
        super(planId, planName, description, price, durationMonths, features);
        this.hasPersonalTrainer = hasPersonalTrainer;
        this.guestPassesPerMonth = guestPassesPerMonth;
    }

    public boolean isHasPersonalTrainer()              { return hasPersonalTrainer; }
    public void    setHasPersonalTrainer(boolean val)  { this.hasPersonalTrainer = val; }

    public int  getGuestPassesPerMonth()               { return guestPassesPerMonth; }
    public void setGuestPassesPerMonth(int val)        { this.guestPassesPerMonth = val; }

    // Polymorphism: override display logic
    @Override
    public String toString() {
        return super.toString()
                + ", trainer=" + hasPersonalTrainer
                + ", guestPasses=" + guestPassesPerMonth;
    }

    // Abstraction: premium-only benefit check
    public String getPremiumBenefitsSummary() {
        return (hasPersonalTrainer ? "Personal Trainer Included. " : "")
                + guestPassesPerMonth + " guest pass(es)/month.";
    }
}

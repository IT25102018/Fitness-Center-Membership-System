package com.fitness.fitnesscentersystem.model;

// Inheritance: StrengthEquipment extends Equipment
public class StrengthEquipment extends Equipment {

    private double maxWeightKg;
    private String weightIncrements;  // e.g. "2.5kg steps"
    private boolean requiresSpotter;

    public StrengthEquipment() {
        super();
        this.setCategory("STRENGTH");
    }

    public StrengthEquipment(String equipmentId, String name, String brand,
                             int quantity, double purchasePrice,
                             String purchaseDate, String condition, String status,
                             double maxWeightKg, String weightIncrements,
                             boolean requiresSpotter) {
        super(equipmentId, name, "STRENGTH", brand, quantity,
                purchasePrice, purchaseDate, condition, status);
        this.maxWeightKg       = maxWeightKg;
        this.weightIncrements  = weightIncrements;
        this.requiresSpotter   = requiresSpotter;
    }

    public double getMaxWeightKg()                        { return maxWeightKg; }
    public void   setMaxWeightKg(double maxWeightKg)      { this.maxWeightKg = maxWeightKg; }

    public String getWeightIncrements()                   { return weightIncrements; }
    public void   setWeightIncrements(String increments)  { this.weightIncrements = increments; }

    public boolean isRequiresSpotter()                    { return requiresSpotter; }
    public void    setRequiresSpotter(boolean val)        { this.requiresSpotter = val; }

    // Polymorphism: override toString from Equipment
    @Override
    public String toString() {
        return super.toString()
                + ", maxWeight=" + maxWeightKg + "kg"
                + ", increments=" + weightIncrements
                + ", requiresSpotter=" + requiresSpotter;
    }

    // Abstraction: strength-specific safety check hidden from caller
    public String getSafetyInfo() {
        return "Max Load: " + maxWeightKg + " kg | "
                + "Increments: " + weightIncrements + " | "
                + "Spotter Required: " + (requiresSpotter ? "Yes" : "No");
    }
}

package com.fitness.fitnesscentersystem.model;

// Inheritance: CardioEquipment extends Equipment
public class CardioEquipment extends Equipment {

    private double maxSpeedKmh;
    private int    maxResistanceLevels;
    private boolean hasHeartRateMonitor;

    public CardioEquipment() {
        super();
        this.setCategory("CARDIO");
    }

    public CardioEquipment(String equipmentId, String name, String brand,
                           int quantity, double purchasePrice,
                           String purchaseDate, String condition, String status,
                           double maxSpeedKmh, int maxResistanceLevels,
                           boolean hasHeartRateMonitor) {
        super(equipmentId, name, "CARDIO", brand, quantity,
                purchasePrice, purchaseDate, condition, status);
        this.maxSpeedKmh          = maxSpeedKmh;
        this.maxResistanceLevels  = maxResistanceLevels;
        this.hasHeartRateMonitor  = hasHeartRateMonitor;
    }

    public double  getMaxSpeedKmh()                         { return maxSpeedKmh; }
    public void    setMaxSpeedKmh(double maxSpeedKmh)       { this.maxSpeedKmh = maxSpeedKmh; }

    public int  getMaxResistanceLevels()                    { return maxResistanceLevels; }
    public void setMaxResistanceLevels(int levels)          { this.maxResistanceLevels = levels; }

    public boolean isHasHeartRateMonitor()                  { return hasHeartRateMonitor; }
    public void    setHasHeartRateMonitor(boolean val)      { this.hasHeartRateMonitor = val; }

    // Polymorphism: override toString from Equipment
    @Override
    public String toString() {
        return super.toString()
                + ", maxSpeed=" + maxSpeedKmh + "km/h"
                + ", resistanceLevels=" + maxResistanceLevels
                + ", heartRateMonitor=" + hasHeartRateMonitor;
    }

    // Abstraction: cardio-specific spec summary hidden from caller
    public String getCardioSpecsSummary() {
        return "Max Speed: " + maxSpeedKmh + " km/h | "
                + "Resistance Levels: " + maxResistanceLevels + " | "
                + "Heart Rate Monitor: " + (hasHeartRateMonitor ? "Yes" : "No");
    }
}

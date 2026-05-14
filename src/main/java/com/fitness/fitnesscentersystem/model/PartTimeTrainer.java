package com.fitness.fitnesscentersystem.model;

// Inheritance: PartTimeTrainer extends Trainer
public class PartTimeTrainer extends Trainer {

    private int    hoursPerWeek;
    private double hourlyRate;

    public PartTimeTrainer() {
        super();
    }

    public PartTimeTrainer(String trainerId, String name, String email,
                           String phone, String specialization,
                           String availability, double salary, String status,
                           int hoursPerWeek, double hourlyRate) {
        super(trainerId, name, email, phone,
                specialization, availability, salary, status);
        this.hoursPerWeek = hoursPerWeek;
        this.hourlyRate   = hourlyRate;
    }

    public int  getHoursPerWeek()               { return hoursPerWeek; }
    public void setHoursPerWeek(int hours)      { this.hoursPerWeek = hours; }

    public double getHourlyRate()               { return hourlyRate; }
    public void   setHourlyRate(double rate)    { this.hourlyRate = rate; }

    // Polymorphism: override toString from Trainer
    @Override
    public String toString() {
        return super.toString()
                + ", hoursPerWeek=" + hoursPerWeek
                + ", hourlyRate=" + hourlyRate;
    }

    // Abstraction: weekly pay calculation hidden from caller
    public double calculateWeeklyPay() {
        return hoursPerWeek * hourlyRate;
    }
}

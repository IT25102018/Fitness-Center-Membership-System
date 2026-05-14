package com.fitness.fitnesscentersystem.model;

public class Trainer {

    private String trainerId;
    private String name;
    private String email;
    private String phone;
    private String specialization;  // e.g. Yoga, Cardio, Weightlifting
    private String availability;    // e.g. MON-WED-FRI, WEEKDAYS, WEEKENDS
    private double salary;
    private String status;          // ACTIVE, INACTIVE

    public Trainer() {}

    public Trainer(String trainerId, String name, String email, String phone,
                   String specialization, String availability,
                   double salary, String status) {
        this.trainerId = trainerId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.specialization = specialization;
        this.availability = availability;
        this.salary = salary;
        this.status = status;
    }

    // Getters & Setters
    public String getTrainerId()                       { return trainerId; }
    public void   setTrainerId(String trainerId)       { this.trainerId = trainerId; }

    public String getName()                            { return name; }
    public void   setName(String name)                 { this.name = name; }

    public String getEmail()                           { return email; }
    public void   setEmail(String email)               { this.email = email; }

    public String getPhone()                           { return phone; }
    public void   setPhone(String phone)               { this.phone = phone; }

    public String getSpecialization()                  { return specialization; }
    public void   setSpecialization(String spec)       { this.specialization = spec; }

    public String getAvailability()                    { return availability; }
    public void   setAvailability(String availability) { this.availability = availability; }

    public double getSalary()                          { return salary; }
    public void   setSalary(double salary)             { this.salary = salary; }

    public String getStatus()                          { return status; }
    public void   setStatus(String status)             { this.status = status; }

    // Save to file as pipe-separated line
    public String toFileString() {
        return trainerId + "|" + name + "|" + email + "|" + phone + "|"
                + specialization + "|" + availability + "|" + salary + "|" + status;
    }

    // Parse from file line
    public static Trainer fromFileString(String line) {
        String[] p = line.split("\\|", -1);
        Trainer t = new Trainer();
        t.setTrainerId(p[0]);
        t.setName(p[1]);
        t.setEmail(p[2]);
        t.setPhone(p[3]);
        t.setSpecialization(p[4]);
        t.setAvailability(p[5]);
        t.setSalary(Double.parseDouble(p[6]));
        t.setStatus(p[7]);
        return t;
    }

    @Override
    public String toString() {
        return "Trainer{id=" + trainerId + ", name=" + name
                + ", spec=" + specialization + ", status=" + status + "}";
    }
}

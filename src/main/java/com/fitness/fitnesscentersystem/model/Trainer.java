package com.fitness.fitnesscentersystem.model;

public abstract class Trainer {

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

    public abstract String getTrainerType();

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
        return getTrainerType() + "|" + trainerId + "|" + name + "|" + email + "|" + phone + "|"
                + specialization + "|" + availability + "|" + salary + "|" + status;
    }

    // Parse from file line
    public static Trainer fromFileString(String line) {
        String[] p = line.split("\\|", -1);
        String type = p[0];

        Trainer t;
        if ("FULL_TIME".equals(type)) {
            FullTimeTrainer ft = new FullTimeTrainer();
            if (p.length > 9) ft.setContractStartDate(p[9]);
            if (p.length > 10) ft.setAnnualLeaveDays(Integer.parseInt(p[10]));
            if (p.length > 11) ft.setDepartment(p[11]);
            t = ft;
        } else if ("PART_TIME".equals(type)) {
            PartTimeTrainer pt = new PartTimeTrainer();
            if (p.length > 9) pt.setHoursPerWeek(Integer.parseInt(p[9]));
            if (p.length > 10) pt.setHourlyRate(Double.parseDouble(p[10]));
            t = pt;
        } else {
            // Fallback for old data or default
            t = new FullTimeTrainer(); // Default to full time if missing type
        }

        // Adjust index if old format (no type at start)
        int offset = (type.equals("FULL_TIME") || type.equals("PART_TIME") || type.equals("GENERAL")) ? 1 : 0;

        t.setTrainerId(p[offset]);
        t.setName(p[offset + 1]);
        t.setEmail(p[offset + 2]);
        t.setPhone(p[offset + 3]);
        t.setSpecialization(p[offset + 4]);
        t.setAvailability(p[offset + 5]);
        t.setSalary(Double.parseDouble(p[offset + 6]));
        t.setStatus(p[offset + 7]);

        return t;
    }

    @Override
    public String toString() {
        return "Trainer{id=" + trainerId + ", type=" + getTrainerType() + ", name=" + name
                + ", spec=" + specialization + ", status=" + status + "}";
    }
}
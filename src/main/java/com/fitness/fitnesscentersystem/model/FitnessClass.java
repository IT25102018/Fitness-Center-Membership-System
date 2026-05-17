package com.fitness.fitnesscentersystem.model;

public class FitnessClass {

    private String classId;
    private String className;
    private String trainerId;       // links to Trainer
    private String trainerName;
    private String schedule;        // e.g. MON 08:00, WED 10:00
    private String duration;        // e.g. 60 mins
    private int    maxCapacity;
    private int    enrolledCount;
    private String classType;       // GROUP, PERSONAL
    private String status;          // ACTIVE, CANCELLED, COMPLETED

    public FitnessClass() {}

    public FitnessClass(String classId, String className, String trainerId,
                        String trainerName, String schedule, String duration,
                        int maxCapacity, int enrolledCount,
                        String classType, String status) {
        this.classId       = classId;
        this.className     = className;
        this.trainerId     = trainerId;
        this.trainerName   = trainerName;
        this.schedule      = schedule;
        this.duration      = duration;
        this.maxCapacity   = maxCapacity;
        this.enrolledCount = enrolledCount;
        this.classType     = classType;
        this.status        = status;
    }

    // Getters & Setters
    public String getClassId()                         { return classId; }
    public void   setClassId(String classId)           { this.classId = classId; }

    public String getClassName()                       { return className; }
    public void   setClassName(String className)       { this.className = className; }

    public String getTrainerId()                       { return trainerId; }
    public void   setTrainerId(String trainerId)       { this.trainerId = trainerId; }

    public String getTrainerName()                     { return trainerName; }
    public void   setTrainerName(String trainerName)   { this.trainerName = trainerName; }

    public String getSchedule()                        { return schedule; }
    public void   setSchedule(String schedule)         { this.schedule = schedule; }

    public String getDuration()                        { return duration; }
    public void   setDuration(String duration)         { this.duration = duration; }

    public int  getMaxCapacity()                       { return maxCapacity; }
    public void setMaxCapacity(int maxCapacity)        { this.maxCapacity = maxCapacity; }

    public int  getEnrolledCount()                     { return enrolledCount; }
    public void setEnrolledCount(int enrolledCount)    { this.enrolledCount = enrolledCount; }

    public String getClassType()                       { return classType; }
    public void   setClassType(String classType)       { this.classType = classType; }

    public String getStatus()                          { return status; }
    public void   setStatus(String status)             { this.status = status; }

    // Helper — check if class has available spots
    public boolean hasAvailableSpots() {
        return enrolledCount < maxCapacity;
    }

    // Helper — available seats remaining
    public int getAvailableSpots() {
        return maxCapacity - enrolledCount;
    }

    // Save to file as pipe-separated line
    public String toFileString() {
        return classId + "|" + className + "|" + trainerId + "|" + trainerName
                + "|" + schedule + "|" + duration + "|" + maxCapacity
                + "|" + enrolledCount + "|" + classType + "|" + status;
    }

    // Parse from file line
    public static FitnessClass fromFileString(String line) {
        String[] p = line.split("\\|", -1);
        FitnessClass fc = new FitnessClass();
        fc.setClassId(p[0]);
        fc.setClassName(p[1]);
        fc.setTrainerId(p[2]);
        fc.setTrainerName(p[3]);
        fc.setSchedule(p[4]);
        fc.setDuration(p[5]);
        fc.setMaxCapacity(Integer.parseInt(p[6]));
        fc.setEnrolledCount(Integer.parseInt(p[7]));
        fc.setClassType(p[8]);
        fc.setStatus(p[9]);
        return fc;
    }

    @Override
    public String toString() {
        return "FitnessClass{id=" + classId + ", name=" + className
                + ", trainer=" + trainerName + ", status=" + status + "}";
    }
}


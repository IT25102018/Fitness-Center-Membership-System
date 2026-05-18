package com.fitness.fitnesscentersystem.model;

// Inheritance: PersonalSession extends FitnessClass
public class PersonalSession extends FitnessClass {

    private String focusArea;     // e.g. Weight Loss, Muscle Gain, Rehab
    private double sessionFee;    // extra fee for personal sessions
    private String sessionNotes;  // trainer notes for the client

    public PersonalSession() {
        super();
        this.setClassType("PERSONAL");
        this.setMaxCapacity(1);   // personal session = 1 client only
    }

    public PersonalSession(String classId, String className, String trainerId,
                           String trainerName, String schedule, String duration,
                           String status, String focusArea,
                           double sessionFee, String sessionNotes) {
        super(classId, className, trainerId, trainerName, schedule,
                duration, 1, 0, "PERSONAL", status);
        this.focusArea    = focusArea;
        this.sessionFee   = sessionFee;
        this.sessionNotes = sessionNotes;
    }

    public String getFocusArea()                    { return focusArea; }
    public void   setFocusArea(String focusArea)    { this.focusArea = focusArea; }

    public double getSessionFee()                   { return sessionFee; }
    public void   setSessionFee(double sessionFee)  { this.sessionFee = sessionFee; }

    public String getSessionNotes()                 { return sessionNotes; }
    public void   setSessionNotes(String notes)     { this.sessionNotes = notes; }

    // Polymorphism: override toString from FitnessClass
    @Override
    public String toString() {
        return super.toString()
                + ", focusArea=" + focusArea
                + ", fee=" + sessionFee
                + ", notes=" + sessionNotes;
    }

    // Abstraction: session cost breakdown hidden from caller
    public String getSessionSummary() {
        return "Focus: " + focusArea + " | "
                + "Session Fee: Rs. " + sessionFee + " | "
                + "Notes: " + sessionNotes;
    }
}

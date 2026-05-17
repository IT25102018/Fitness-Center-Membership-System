package com.fitness.fitnesscentersystem.model;

public class Booking {

    private String bookingId;
    private String userId;
    private String username;
    private String classId;
    private String className;
    private String bookingDate;     // format: YYYY-MM-DD
    private String bookingStatus;   // CONFIRMED, CANCELLED, ATTENDED, NO_SHOW

    public Booking() {}

    public Booking(String bookingId, String userId, String username,
                   String classId, String className,
                   String bookingDate, String bookingStatus) {
        this.bookingId     = bookingId;
        this.userId        = userId;
        this.username      = username;
        this.classId       = classId;
        this.className     = className;
        this.bookingDate   = bookingDate;
        this.bookingStatus = bookingStatus;
    }

    // Getters & Setters
    public String getBookingId()                         { return bookingId; }
    public void   setBookingId(String bookingId)         { this.bookingId = bookingId; }

    public String getUserId()                            { return userId; }
    public void   setUserId(String userId)               { this.userId = userId; }

    public String getUsername()                          { return username; }
    public void   setUsername(String username)           { this.username = username; }

    public String getClassId()                           { return classId; }
    public void   setClassId(String classId)             { this.classId = classId; }

    public String getClassName()                         { return className; }
    public void   setClassName(String className)         { this.className = className; }

    public String getBookingDate()                       { return bookingDate; }
    public void   setBookingDate(String bookingDate)     { this.bookingDate = bookingDate; }

    public String getBookingStatus()                     { return bookingStatus; }
    public void   setBookingStatus(String bookingStatus) { this.bookingStatus = bookingStatus; }

    // Save to file as pipe-separated line
    public String toFileString() {
        return bookingId + "|" + userId + "|" + username + "|"
                + classId + "|" + className + "|" + bookingDate + "|" + bookingStatus;
    }

    // Parse from file line
    public static Booking fromFileString(String line) {
        String[] p = line.split("\\|", -1);
        Booking b = new Booking();
        b.setBookingId(p[0]);
        b.setUserId(p[1]);
        b.setUsername(p[2]);
        b.setClassId(p[3]);
        b.setClassName(p[4]);
        b.setBookingDate(p[5]);
        b.setBookingStatus(p[6]);
        return b;
    }

    @Override
    public String toString() {
        return "Booking{id=" + bookingId + ", user=" + username
                + ", class=" + className + ", status=" + bookingStatus + "}";
    }
}


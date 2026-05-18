package com.fitness.fitnesscentersystem.model;

public class Payment {

    private String paymentId;
    private String userId;
    private String username;
    private String planId;
    private String planName;
    private double amount;
    private String paymentDate;     // format: YYYY-MM-DD
    private String paymentMethod;   // CASH, CARD, BANK_TRANSFER, ONLINE
    private String paymentStatus;   // PAID, PENDING, FAILED, REFUNDED
    private String remarks;

    public Payment() {}

    public Payment(String paymentId, String userId, String username,
                   String planId, String planName, double amount,
                   String paymentDate, String paymentMethod,
                   String paymentStatus, String remarks) {
        this.paymentId     = paymentId;
        this.userId        = userId;
        this.username      = username;
        this.planId        = planId;
        this.planName      = planName;
        this.amount        = amount;
        this.paymentDate   = paymentDate;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
        this.remarks       = remarks;
    }

    // Getters & Setters
    public String getPaymentId()                         { return paymentId; }
    public void   setPaymentId(String paymentId)         { this.paymentId = paymentId; }

    public String getUserId()                            { return userId; }
    public void   setUserId(String userId)               { this.userId = userId; }

    public String getUsername()                          { return username; }
    public void   setUsername(String username)           { this.username = username; }

    public String getPlanId()                            { return planId; }
    public void   setPlanId(String planId)               { this.planId = planId; }

    public String getPlanName()                          { return planName; }
    public void   setPlanName(String planName)           { this.planName = planName; }

    public double getAmount()                            { return amount; }
    public void   setAmount(double amount)               { this.amount = amount; }

    public String getPaymentDate()                       { return paymentDate; }
    public void   setPaymentDate(String paymentDate)     { this.paymentDate = paymentDate; }

    public String getPaymentMethod()                     { return paymentMethod; }
    public void   setPaymentMethod(String method)        { this.paymentMethod = method; }

    public String getPaymentStatus()                     { return paymentStatus; }
    public void   setPaymentStatus(String status)        { this.paymentStatus = status; }

    public String getRemarks()                           { return remarks; }
    public void   setRemarks(String remarks)             { this.remarks = remarks; }

    // Save to file as pipe-separated line
    public String toFileString() {
        return paymentId + "|" + userId + "|" + username + "|"
                + planId + "|" + planName + "|" + amount + "|"
                + paymentDate + "|" + paymentMethod + "|"
                + paymentStatus + "|" + remarks;
    }

    // Parse from file line
    public static Payment fromFileString(String line) {
        String[] p = line.split("\\|", -1);
        Payment pay = new Payment();
        pay.setPaymentId(p[0]);
        pay.setUserId(p[1]);
        pay.setUsername(p[2]);
        pay.setPlanId(p[3]);
        pay.setPlanName(p[4]);
        pay.setAmount(Double.parseDouble(p[5]));
        pay.setPaymentDate(p[6]);
        pay.setPaymentMethod(p[7]);
        pay.setPaymentStatus(p[8]);
        pay.setRemarks(p[9]);
        return pay;
    }

    @Override
    public String toString() {
        return "Payment{id=" + paymentId + ", user=" + username
                + ", plan=" + planName + ", amount=" + amount
                + ", status=" + paymentStatus + "}";
    }
}

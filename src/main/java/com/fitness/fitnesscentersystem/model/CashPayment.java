package com.fitness.fitnesscentersystem.model;

// Inheritance: CashPayment extends Payment
public class CashPayment extends Payment {

    private String receivedBy;      // staff member who received cash
    private String receiptNumber;   // physical receipt number
    private double amountTendered;  // amount given by customer
    private double changeGiven;     // change returned

    public CashPayment() {
        super();
        this.setPaymentMethod("CASH");
    }

    public CashPayment(String paymentId, String userId, String username,
                       String planId, String planName, double amount,
                       String paymentDate, String paymentStatus, String remarks,
                       String receivedBy, String receiptNumber,
                       double amountTendered) {
        super(paymentId, userId, username, planId, planName, amount,
                paymentDate, "CASH", paymentStatus, remarks);
        this.receivedBy     = receivedBy;
        this.receiptNumber  = receiptNumber;
        this.amountTendered = amountTendered;
        this.changeGiven    = amountTendered - amount;
    }

    public String getReceivedBy()                      { return receivedBy; }
    public void   setReceivedBy(String receivedBy)     { this.receivedBy = receivedBy; }

    public String getReceiptNumber()                   { return receiptNumber; }
    public void   setReceiptNumber(String number)      { this.receiptNumber = number; }

    public double getAmountTendered()                  { return amountTendered; }
    public void   setAmountTendered(double amount)     { this.amountTendered = amount; }

    public double getChangeGiven()                     { return changeGiven; }
    public void   setChangeGiven(double change)        { this.changeGiven = change; }

    // Polymorphism: override toString from Payment
    @Override
    public String toString() {
        return super.toString()
                + ", receivedBy=" + receivedBy
                + ", receipt=" + receiptNumber
                + ", tendered=" + amountTendered
                + ", change=" + changeGiven;
    }

    // Abstraction: cash transaction summary hidden from caller
    public String getCashSummary() {
        return "Received By: " + receivedBy
                + " | Receipt #: " + receiptNumber
                + " | Tendered: Rs. " + amountTendered
                + " | Change: Rs. " + changeGiven;
    }
}

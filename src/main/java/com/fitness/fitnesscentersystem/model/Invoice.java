package com.fitness.fitnesscentersystem.model;

public class Invoice {

    private String invoiceId;
    private String paymentId;       // links to Payment
    private String userId;
    private String username;
    private String planName;
    private double subtotal;
    private double discountAmount;
    private double taxAmount;       // tax = 8% of subtotal
    private double totalAmount;
    private String invoiceDate;     // format: YYYY-MM-DD
    private String invoiceStatus;   // ISSUED, PAID, CANCELLED

    public Invoice() {}

    public Invoice(String invoiceId, String paymentId, String userId,
                   String username, String planName, double subtotal,
                   double discountAmount, double taxAmount, double totalAmount,
                   String invoiceDate, String invoiceStatus) {
        this.invoiceId     = invoiceId;
        this.paymentId     = paymentId;
        this.userId        = userId;
        this.username      = username;
        this.planName      = planName;
        this.subtotal      = subtotal;
        this.discountAmount = discountAmount;
        this.taxAmount     = taxAmount;
        this.totalAmount   = totalAmount;
        this.invoiceDate   = invoiceDate;
        this.invoiceStatus = invoiceStatus;
    }

    // Getters & Setters
    public String getInvoiceId()                         { return invoiceId; }
    public void   setInvoiceId(String invoiceId)         { this.invoiceId = invoiceId; }

    public String getPaymentId()                         { return paymentId; }
    public void   setPaymentId(String paymentId)         { this.paymentId = paymentId; }

    public String getUserId()                            { return userId; }
    public void   setUserId(String userId)               { this.userId = userId; }

    public String getUsername()                          { return username; }
    public void   setUsername(String username)           { this.username = username; }

    public String getPlanName()                          { return planName; }
    public void   setPlanName(String planName)           { this.planName = planName; }

    public double getSubtotal()                          { return subtotal; }
    public void   setSubtotal(double subtotal)           { this.subtotal = subtotal; }

    public double getDiscountAmount()                    { return discountAmount; }
    public void   setDiscountAmount(double discount)     { this.discountAmount = discount; }

    public double getTaxAmount()                         { return taxAmount; }
    public void   setTaxAmount(double taxAmount)         { this.taxAmount = taxAmount; }

    public double getTotalAmount()                       { return totalAmount; }
    public void   setTotalAmount(double totalAmount)     { this.totalAmount = totalAmount; }

    public String getInvoiceDate()                       { return invoiceDate; }
    public void   setInvoiceDate(String invoiceDate)     { this.invoiceDate = invoiceDate; }

    public String getInvoiceStatus()                     { return invoiceStatus; }
    public void   setInvoiceStatus(String invoiceStatus) { this.invoiceStatus = invoiceStatus; }

    // Save to file as pipe-separated line
    public String toFileString() {
        return invoiceId + "|" + paymentId + "|" + userId + "|" + username + "|"
                + planName + "|" + subtotal + "|" + discountAmount + "|"
                + taxAmount + "|" + totalAmount + "|"
                + invoiceDate + "|" + invoiceStatus;
    }

    // Parse from file line
    public static Invoice fromFileString(String line) {
        String[] p = line.split("\\|", -1);
        Invoice inv = new Invoice();
        inv.setInvoiceId(p[0]);
        inv.setPaymentId(p[1]);
        inv.setUserId(p[2]);
        inv.setUsername(p[3]);
        inv.setPlanName(p[4]);
        inv.setSubtotal(Double.parseDouble(p[5]));
        inv.setDiscountAmount(Double.parseDouble(p[6]));
        inv.setTaxAmount(Double.parseDouble(p[7]));
        inv.setTotalAmount(Double.parseDouble(p[8]));
        inv.setInvoiceDate(p[9]);
        inv.setInvoiceStatus(p[10]);
        return inv;
    }

    @Override
    public String toString() {
        return "Invoice{id=" + invoiceId + ", user=" + username
                + ", plan=" + planName + ", total=" + totalAmount
                + ", status=" + invoiceStatus + "}";
    }
}

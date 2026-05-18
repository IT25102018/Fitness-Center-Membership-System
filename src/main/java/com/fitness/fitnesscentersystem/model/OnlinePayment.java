package com.fitness.fitnesscentersystem.model;

// Inheritance: OnlinePayment extends Payment
public class OnlinePayment extends Payment {

    private String transactionId;    // gateway transaction reference
    private String gatewayName;      // e.g. PayHere, PayPal, Stripe
    private String cardLastFourDigits;

    public OnlinePayment() {
        super();
        this.setPaymentMethod("ONLINE");
    }

    public OnlinePayment(String paymentId, String userId, String username,
                         String planId, String planName, double amount,
                         String paymentDate, String paymentStatus, String remarks,
                         String transactionId, String gatewayName,
                         String cardLastFourDigits) {
        super(paymentId, userId, username, planId, planName, amount,
                paymentDate, "ONLINE", paymentStatus, remarks);
        this.transactionId      = transactionId;
        this.gatewayName        = gatewayName;
        this.cardLastFourDigits = cardLastFourDigits;
    }

    public String getTransactionId()                         { return transactionId; }
    public void   setTransactionId(String transactionId)     { this.transactionId = transactionId; }

    public String getGatewayName()                           { return gatewayName; }
    public void   setGatewayName(String gatewayName)         { this.gatewayName = gatewayName; }

    public String getCardLastFourDigits()                    { return cardLastFourDigits; }
    public void   setCardLastFourDigits(String digits)       { this.cardLastFourDigits = digits; }

    // Polymorphism: override toString from Payment
    @Override
    public String toString() {
        return super.toString()
                + ", gateway=" + gatewayName
                + ", txnId=" + transactionId
                + ", card=****" + cardLastFourDigits;
    }

    // Abstraction: online payment receipt details hidden from caller
    public String getReceiptSummary() {
        return "Gateway: " + gatewayName
                + " | Transaction ID: " + transactionId
                + " | Card: ****" + cardLastFourDigits;
    }
}

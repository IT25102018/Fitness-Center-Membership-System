package com.fitness.fitnesscentersystem.service;

import com.fitness.fitnesscentersystem.model.Invoice;
import com.fitness.fitnesscentersystem.model.Payment;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PaymentService {

    private static final String PAYMENT_FILE =
            "src/main/resources/data/payments.txt";
    private static final String INVOICE_FILE =
            "src/main/resources/data/invoices.txt";

    private static final double TAX_RATE      = 0.08;  // 8% tax
    private static final double VIP_DISCOUNT  = 0.10;  // 10% for VIP
    private static final double PREM_DISCOUNT = 0.05;  // 5% for PREMIUM

    // ══════════════════════════════════════════════════════════════════════════
    //  FILE HELPERS — PAYMENTS
    // ══════════════════════════════════════════════════════════════════════════

    private List<Payment> readAllPayments() {
        List<Payment> list = new ArrayList<>();
        File file = new File(PAYMENT_FILE);
        if (!file.exists()) { file.getParentFile().mkdirs(); return list; }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty())
                    list.add(Payment.fromFileString(line.trim()));
            }
        } catch (IOException e) { e.printStackTrace(); }
        return list;
    }

    private void writeAllPayments(List<Payment> payments) {
        File file = new File(PAYMENT_FILE);
        file.getParentFile().mkdirs();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, false))) {
            for (Payment p : payments) { bw.write(p.toFileString()); bw.newLine(); }
        } catch (IOException e) { e.printStackTrace(); }
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  FILE HELPERS — INVOICES
    // ══════════════════════════════════════════════════════════════════════════

    private List<Invoice> readAllInvoices() {
        List<Invoice> list = new ArrayList<>();
        File file = new File(INVOICE_FILE);
        if (!file.exists()) { file.getParentFile().mkdirs(); return list; }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty())
                    list.add(Invoice.fromFileString(line.trim()));
            }
        } catch (IOException e) { e.printStackTrace(); }
        return list;
    }

    private void writeAllInvoices(List<Invoice> invoices) {
        File file = new File(INVOICE_FILE);
        file.getParentFile().mkdirs();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, false))) {
            for (Invoice inv : invoices) { bw.write(inv.toFileString()); bw.newLine(); }
        } catch (IOException e) { e.printStackTrace(); }
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  PAYMENT — CRUD
    // ══════════════════════════════════════════════════════════════════════════

    // CREATE — Record a new payment and auto-generate its invoice
    public Payment createPayment(String userId, String username,
                                 String planId, String planName,
                                 double planPrice, String membershipType,
                                 String paymentMethod, String remarks) {

        String paymentId = "PAY-"
                + UUID.randomUUID().toString().substring(0, 6).toUpperCase();

        Payment payment = new Payment(
                paymentId, userId, username, planId, planName,
                planPrice, LocalDate.now().toString(),
                paymentMethod, "PAID", remarks
        );

        List<Payment> payments = readAllPayments();
        payments.add(payment);
        writeAllPayments(payments);

        // Auto-generate invoice for this payment
        generateInvoice(payment, membershipType);

        return payment;
    }

    // READ — all payments
    public List<Payment> getAllPayments() {
        return readAllPayments();
    }

    // READ — payments by user
    public List<Payment> getPaymentsByUser(String userId) {
        List<Payment> result = new ArrayList<>();
        for (Payment p : readAllPayments()) {
            if (p.getUserId().equals(userId)) result.add(p);
        }
        return result;
    }

    // READ — find payment by ID
    public Payment findPaymentById(String paymentId) {
        for (Payment p : readAllPayments()) {
            if (p.getPaymentId().equals(paymentId)) return p;
        }
        return null;
    }

    // READ — payments by status
    public List<Payment> getPaymentsByStatus(String status) {
        List<Payment> result = new ArrayList<>();
        for (Payment p : readAllPayments()) {
            if (p.getPaymentStatus().equalsIgnoreCase(status)) result.add(p);
        }
        return result;
    }

    // UPDATE — update payment status
    public boolean updatePaymentStatus(String paymentId, String newStatus) {
        List<Payment> payments = readAllPayments();
        boolean found = false;
        for (Payment p : payments) {
            if (p.getPaymentId().equals(paymentId)) {
                p.setPaymentStatus(newStatus);
                found = true;
                break;
            }
        }
        if (found) writeAllPayments(payments);
        return found;
    }

    // UPDATE — edit full payment record
    public boolean updatePayment(Payment updated) {
        List<Payment> payments = readAllPayments();
        boolean found = false;
        for (int i = 0; i < payments.size(); i++) {
            if (payments.get(i).getPaymentId().equals(updated.getPaymentId())) {
                payments.set(i, updated);
                found = true;
                break;
            }
        }
        if (found) writeAllPayments(payments);
        return found;
    }

    // DELETE — remove a payment record
    public boolean deletePayment(String paymentId) {
        List<Payment> payments = readAllPayments();
        boolean removed = payments.removeIf(
                p -> p.getPaymentId().equals(paymentId)
        );
        if (removed) writeAllPayments(payments);
        return removed;
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  INVOICE — CRUD
    // ══════════════════════════════════════════════════════════════════════════

    // CREATE — Generate invoice automatically from a payment
    public Invoice generateInvoice(Payment payment, String membershipType) {

        double subtotal  = payment.getAmount();

        // Discount based on membership type (Polymorphism concept in logic)
        double discountRate = 0.0;
        if ("VIP".equalsIgnoreCase(membershipType))     discountRate = VIP_DISCOUNT;
        else if ("PREMIUM".equalsIgnoreCase(membershipType)) discountRate = PREM_DISCOUNT;

        double discountAmount = subtotal * discountRate;
        double afterDiscount  = subtotal - discountAmount;
        double taxAmount      = afterDiscount * TAX_RATE;
        double totalAmount    = afterDiscount + taxAmount;

        // Round to 2 decimal places
        discountAmount = Math.round(discountAmount * 100.0) / 100.0;
        taxAmount      = Math.round(taxAmount      * 100.0) / 100.0;
        totalAmount    = Math.round(totalAmount    * 100.0) / 100.0;

        String invoiceId = "INV-"
                + UUID.randomUUID().toString().substring(0, 6).toUpperCase();

        Invoice invoice = new Invoice(
                invoiceId, payment.getPaymentId(),
                payment.getUserId(), payment.getUsername(),
                payment.getPlanName(), subtotal,
                discountAmount, taxAmount, totalAmount,
                LocalDate.now().toString(), "ISSUED"
        );

        List<Invoice> invoices = readAllInvoices();
        invoices.add(invoice);
        writeAllInvoices(invoices);
        return invoice;
    }

    // READ — all invoices
    public List<Invoice> getAllInvoices() {
        return readAllInvoices();
    }

    // READ — invoices by user
    public List<Invoice> getInvoicesByUser(String userId) {
        List<Invoice> result = new ArrayList<>();
        for (Invoice inv : readAllInvoices()) {
            if (inv.getUserId().equals(userId)) result.add(inv);
        }
        return result;
    }

    // READ — find invoice by ID
    public Invoice findInvoiceById(String invoiceId) {
        for (Invoice inv : readAllInvoices()) {
            if (inv.getInvoiceId().equals(invoiceId)) return inv;
        }
        return null;
    }

    // READ — find invoice by payment ID
    public Invoice findInvoiceByPaymentId(String paymentId) {
        for (Invoice inv : readAllInvoices()) {
            if (inv.getPaymentId().equals(paymentId)) return inv;
        }
        return null;
    }

    // UPDATE — update invoice status
    public boolean updateInvoiceStatus(String invoiceId, String newStatus) {
        List<Invoice> invoices = readAllInvoices();
        boolean found = false;
        for (Invoice inv : invoices) {
            if (inv.getInvoiceId().equals(invoiceId)) {
                inv.setInvoiceStatus(newStatus);
                found = true;
                break;
            }
        }
        if (found) writeAllInvoices(invoices);
        return found;
    }

    // DELETE — remove an invoice
    public boolean deleteInvoice(String invoiceId) {
        List<Invoice> invoices = readAllInvoices();
        boolean removed = invoices.removeIf(
                inv -> inv.getInvoiceId().equals(invoiceId)
        );
        if (removed) writeAllInvoices(invoices);
        return removed;
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  SUMMARY / REPORTS
    // ══════════════════════════════════════════════════════════════════════════

    // Total revenue from all PAID payments
    public double getTotalRevenue() {
        double total = 0;
        for (Payment p : readAllPayments()) {
            if ("PAID".equalsIgnoreCase(p.getPaymentStatus())) {
                total += p.getAmount();
            }
        }
        return Math.round(total * 100.0) / 100.0;
    }

    // Count payments by method
    public long countByMethod(String method) {
        return readAllPayments().stream()
                .filter(p -> p.getPaymentMethod().equalsIgnoreCase(method))
                .count();
    }
}

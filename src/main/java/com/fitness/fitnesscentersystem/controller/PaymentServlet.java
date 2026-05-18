package com.fitness.fitnesscentersystem.controller;

import com.fitness.fitnesscentersystem.model.Invoice;
import com.fitness.fitnesscentersystem.model.Payment;
import com.fitness.fitnesscentersystem.model.User;
import com.fitness.fitnesscentersystem.service.PaymentService;
import com.fitness.fitnesscentersystem.service.MembershipPlanService;
import com.fitness.fitnesscentersystem.model.MembershipPlan;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

import java.util.List;

@Controller
@RequestMapping("/payment")
public class PaymentServlet {

    private final PaymentService        paymentService = new PaymentService();
    private final MembershipPlanService planService    = new MembershipPlanService();

    // ── USER — make a new payment ─────────────────────────────────────────────
    @GetMapping("/make")
    public String showPaymentForm(HttpSession session, Model model) {
        if (session.getAttribute("loggedUser") == null)
            return "redirect:/user/login";
        model.addAttribute("plans", planService.getAllPlans());
        return "payment/make-payment";   // → templates/payment/make-payment.html
    }

    @PostMapping("/make")
    public String processPayment(@RequestParam String planId,
                                 @RequestParam String paymentMethod,
                                 @RequestParam(required = false,
                                         defaultValue = "") String remarks,
                                 HttpSession session, Model model) {
        if (session.getAttribute("loggedUser") == null)
            return "redirect:/user/login";

        User user = (User) session.getAttribute("loggedUser");
        MembershipPlan plan = planService.findById(planId);

        if (plan == null) {
            model.addAttribute("error", "Selected plan not found.");
            model.addAttribute("plans", planService.getAllPlans());
            return "payment/make-payment";
        }

        Payment payment = paymentService.createPayment(
                user.getUserId(), user.getUsername(),
                plan.getPlanId(), plan.getPlanName(),
                plan.getPrice(), user.getMembershipType(),
                paymentMethod, remarks
        );

        // Find the auto-generated invoice
        Invoice invoice = paymentService.findInvoiceByPaymentId(
                payment.getPaymentId()
        );

        model.addAttribute("payment", payment);
        model.addAttribute("invoice", invoice);
        return "payment/receipt";         // → templates/payment/receipt.html
    }

    // ── USER — my payment history (READ) ─────────────────────────────────────
    @GetMapping("/my")
    public String myPayments(HttpSession session, Model model) {
        if (session.getAttribute("loggedUser") == null)
            return "redirect:/user/login";

        User user = (User) session.getAttribute("loggedUser");
        model.addAttribute("payments",
                paymentService.getPaymentsByUser(user.getUserId()));
        return "payment/my-payments";     // → templates/payment/my-payments.html
    }

    // ── USER — my invoices (READ) ─────────────────────────────────────────────
    @GetMapping("/my/invoices")
    public String myInvoices(HttpSession session, Model model) {
        if (session.getAttribute("loggedUser") == null)
            return "redirect:/user/login";

        User user = (User) session.getAttribute("loggedUser");
        model.addAttribute("invoices",
                paymentService.getInvoicesByUser(user.getUserId()));
        return "payment/my-invoices";     // → templates/payment/my-invoices.html
    }

    // ── USER — view single invoice (READ) ─────────────────────────────────────
    @GetMapping("/invoice/{invoiceId}")
    public String viewInvoice(@PathVariable String invoiceId, Model model) {
        Invoice invoice = paymentService.findInvoiceById(invoiceId);
        if (invoice == null) return "redirect:/payment/my/invoices";
        model.addAttribute("invoice", invoice);
        return "payment/invoice-detail";  // → templates/payment/invoice-detail.html
    }

    // ── ADMIN — all payments list (READ) ──────────────────────────────────────
    @GetMapping("/admin/list")
    public String adminPayments(@RequestParam(required = false) String status,
                                Model model) {
        List<Payment> payments;
        if (status != null && !status.trim().isEmpty()) {
            payments = paymentService.getPaymentsByStatus(status);
            model.addAttribute("selectedStatus", status);
        } else {
            payments = paymentService.getAllPayments();
        }
        model.addAttribute("payments",     payments);
        model.addAttribute("totalRevenue", paymentService.getTotalRevenue());
        model.addAttribute("cashCount",    paymentService.countByMethod("CASH"));
        model.addAttribute("cardCount",    paymentService.countByMethod("CARD"));
        model.addAttribute("onlineCount",  paymentService.countByMethod("ONLINE"));
        model.addAttribute("bankCount",    paymentService.countByMethod("BANK_TRANSFER"));
        return "payment/admin-payments";   // → templates/payment/admin-payments.html
    }

    // ── ADMIN — all invoices list (READ) ──────────────────────────────────────
    @GetMapping("/admin/invoices")
    public String adminInvoices(Model model) {
        model.addAttribute("invoices", paymentService.getAllInvoices());
        return "payment/admin-invoices";   // → templates/payment/admin-invoices.html
    }

    // ── ADMIN — update payment status (UPDATE) ────────────────────────────────
    @PostMapping("/admin/status/{paymentId}")
    public String updatePaymentStatus(@PathVariable String paymentId,
                                      @RequestParam String newStatus,
                                      Model model) {
        paymentService.updatePaymentStatus(paymentId, newStatus);
        model.addAttribute("message", "Payment status updated to: " + newStatus);
        model.addAttribute("payments", paymentService.getAllPayments());
        model.addAttribute("totalRevenue", paymentService.getTotalRevenue());
        model.addAttribute("cashCount",    paymentService.countByMethod("CASH"));
        model.addAttribute("cardCount",    paymentService.countByMethod("CARD"));
        model.addAttribute("onlineCount",  paymentService.countByMethod("ONLINE"));
        model.addAttribute("bankCount",    paymentService.countByMethod("BANK_TRANSFER"));
        return "payment/admin-payments";
    }

    // ── ADMIN — update invoice status (UPDATE) ────────────────────────────────
    @PostMapping("/admin/invoice/status/{invoiceId}")
    public String updateInvoiceStatus(@PathVariable String invoiceId,
                                      @RequestParam String newStatus,
                                      Model model) {
        paymentService.updateInvoiceStatus(invoiceId, newStatus);
        model.addAttribute("message", "Invoice status updated.");
        model.addAttribute("invoices", paymentService.getAllInvoices());
        return "payment/admin-invoices";
    }

    // ── ADMIN — delete payment (DELETE) ───────────────────────────────────────
    @PostMapping("/admin/delete/{paymentId}")
    public String deletePayment(@PathVariable String paymentId, Model model) {
        paymentService.deletePayment(paymentId);
        model.addAttribute("message", "Payment record deleted.");
        model.addAttribute("payments", paymentService.getAllPayments());
        model.addAttribute("totalRevenue", paymentService.getTotalRevenue());
        model.addAttribute("cashCount",    paymentService.countByMethod("CASH"));
        model.addAttribute("cardCount",    paymentService.countByMethod("CARD"));
        model.addAttribute("onlineCount",  paymentService.countByMethod("ONLINE"));
        model.addAttribute("bankCount",    paymentService.countByMethod("BANK_TRANSFER"));
        return "payment/admin-payments";
    }

    // ── ADMIN — delete invoice (DELETE) ───────────────────────────────────────
    @PostMapping("/admin/invoice/delete/{invoiceId}")
    public String deleteInvoice(@PathVariable String invoiceId, Model model) {
        paymentService.deleteInvoice(invoiceId);
        model.addAttribute("message", "Invoice deleted.");
        model.addAttribute("invoices", paymentService.getAllInvoices());
        return "payment/admin-invoices";
    }
}

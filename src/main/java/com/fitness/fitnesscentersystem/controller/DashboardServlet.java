package com.fitness.fitnesscentersystem.controller;

import com.fitness.fitnesscentersystem.service.BookingService;
import com.fitness.fitnesscentersystem.service.EquipmentService;
import com.fitness.fitnesscentersystem.service.MembershipPlanService;
import com.fitness.fitnesscentersystem.service.PaymentService;
import com.fitness.fitnesscentersystem.service.TrainerService;
import com.fitness.fitnesscentersystem.service.UserService;
import com.fitness.fitnesscentersystem.model.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardServlet {

    private final UserService           userService   = new UserService();
    private final TrainerService        trainerService  = new TrainerService();
    private final EquipmentService      equipmentService = new EquipmentService();
    private final BookingService        bookingService  = new BookingService();
    private final PaymentService        paymentService  = new PaymentService();
    private final MembershipPlanService planService     = new MembershipPlanService();

    @GetMapping("/")
    public String dashboard(HttpSession session, Model model) {

        User user = (User) session.getAttribute("loggedUser");
        if (user == null) {
            return "redirect:/user/login";
        }

        // Trim role to guard against trailing whitespace / \r from file parsing
        String role = (user.getRole() == null) ? "" : user.getRole().trim();

        boolean isAdmin = "ADMIN".equals(role);
        boolean isUser  = "USER".equals(role);

        // Expose clean flags to the template
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("isUser",  isUser);
        model.addAttribute("userRole", role);  // trimmed role for display

        if (isUser) {
            // USER role: only pass the user's own invoices
            model.addAttribute("userInvoices",
                    paymentService.getInvoicesByUser(user.getUserId()));
        }

        if (isAdmin) {
            // ADMIN role: pass all live stat counts for summary cards & module cards
            model.addAttribute("totalUsers",
                    userService.getAllUsers().size());
            model.addAttribute("totalTrainers",
                    trainerService.getAllTrainers().size());
            model.addAttribute("totalEquipment",
                    equipmentService.getAllEquipment().size());
            model.addAttribute("totalClasses",
                    bookingService.getAllClasses().size());
            model.addAttribute("totalBookings",
                    bookingService.getAllBookings().size());
            model.addAttribute("totalPlans",
                    planService.getAllPlans().size());
            model.addAttribute("totalRevenue",
                    paymentService.getTotalRevenue());
            model.addAttribute("totalPayments",
                    paymentService.getAllPayments().size());
        }

        return "index";   // → templates/index.html
    }
}
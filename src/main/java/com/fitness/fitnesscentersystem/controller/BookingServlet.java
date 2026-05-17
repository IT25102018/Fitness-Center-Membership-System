package com.fitness.fitnesscentersystem.controller;

import com.fitness.fitnesscentersystem.model.Booking;
import com.fitness.fitnesscentersystem.model.FitnessClass;
import com.fitness.fitnesscentersystem.service.BookingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

import java.util.List;

@Controller
@RequestMapping("/booking")
public class BookingServlet {

    private final BookingService bookingService = new BookingService();

    // ── LIST all classes available to book (READ) ─────────────────────────────
    @GetMapping("/classes")
    public String listClasses(Model model) {
        model.addAttribute("classes", bookingService.getActiveClasses());
        return "booking/classes";       // → templates/booking/classes.html
    }

    // ── SEARCH classes ────────────────────────────────────────────────────────
    @GetMapping("/classes/search")
    public String searchClasses(@RequestParam(required = false) String keyword,
                                Model model) {
        if (keyword != null && !keyword.trim().isEmpty()) {
            model.addAttribute("classes", bookingService.searchClasses(keyword));
            model.addAttribute("keyword", keyword);
        } else {
            model.addAttribute("classes", bookingService.getActiveClasses());
        }
        return "booking/classes";
    }

    // ── BOOK a class ──────────────────────────────────────────────────────────
    @PostMapping("/book/{classId}")
    public String bookClass(@PathVariable String classId,
                            HttpSession session, Model model) {
        // Get logged user from session
        Object loggedUser = session.getAttribute("loggedUser");
        if (loggedUser == null) return "redirect:/user/login";

        // Cast to User to get userId and username
        com.fitness.fitnesscentersystem.model.User user =
                (com.fitness.fitnesscentersystem.model.User) loggedUser;

        String result = bookingService.createBooking(
                user.getUserId(), user.getUsername(), classId
        );

        if ("SUCCESS".equals(result)) {
            model.addAttribute("message", "Class booked successfully!");
        } else {
            model.addAttribute("error", result);
        }
        model.addAttribute("classes", bookingService.getActiveClasses());
        return "booking/classes";
    }

    // ── MY BOOKINGS — user's booking list (READ) ──────────────────────────────
    @GetMapping("/my")
    public String myBookings(HttpSession session, Model model) {
        Object loggedUser = session.getAttribute("loggedUser");
        if (loggedUser == null) return "redirect:/user/login";

        com.fitness.fitnesscentersystem.model.User user =
                (com.fitness.fitnesscentersystem.model.User) loggedUser;

        model.addAttribute("bookings",
                bookingService.getBookingsByUser(user.getUserId()));
        return "booking/my-bookings";   // → templates/booking/my-bookings.html
    }

    // ── CANCEL booking (UPDATE status to CANCELLED) ───────────────────────────
    @PostMapping("/cancel/{bookingId}")
    public String cancelBooking(@PathVariable String bookingId,
                                HttpSession session, Model model) {
        Object loggedUser = session.getAttribute("loggedUser");
        if (loggedUser == null) return "redirect:/user/login";

        com.fitness.fitnesscentersystem.model.User user =
                (com.fitness.fitnesscentersystem.model.User) loggedUser;

        String result = bookingService.cancelBooking(bookingId);
        model.addAttribute(
                "SUCCESS".equals(result) ? "message" : "error",
                "SUCCESS".equals(result) ? "Booking cancelled." : result
        );
        model.addAttribute("bookings",
                bookingService.getBookingsByUser(user.getUserId()));
        return "booking/my-bookings";
    }

    // ── ADMIN — manage all classes ────────────────────────────────────────────
    @GetMapping("/admin/classes")
    public String adminClasses(Model model) {
        model.addAttribute("classes", bookingService.getAllClasses());
        return "booking/admin-classes"; // → templates/booking/admin-classes.html
    }

    // ── ADMIN — add class form ────────────────────────────────────────────────
    @GetMapping("/admin/add")
    public String showAddClassForm(Model model) {
        model.addAttribute("fitnessClass", new FitnessClass());
        return "booking/add-class";     // → templates/booking/add-class.html
    }

    @PostMapping("/admin/add")
    public String addClass(@ModelAttribute FitnessClass fitnessClass, Model model) {
        bookingService.addClass(fitnessClass);
        return "redirect:/booking/admin/classes";
    }

    // ── ADMIN — edit class form ───────────────────────────────────────────────
    @GetMapping("/admin/edit/{classId}")
    public String showEditClassForm(@PathVariable String classId, Model model) {
        FitnessClass fc = bookingService.findClassById(classId);
        if (fc == null) return "redirect:/booking/admin/classes";
        model.addAttribute("fitnessClass", fc);
        return "booking/edit-class";    // → templates/booking/edit-class.html
    }

    @PostMapping("/admin/edit")
    public String updateClass(@ModelAttribute FitnessClass fitnessClass, Model model) {
        bookingService.updateClass(fitnessClass);
        return "redirect:/booking/admin/classes";
    }

    // ── ADMIN — view bookings for a class (READ) ──────────────────────────────
    @GetMapping("/admin/class/{classId}/bookings")
    public String classBookings(@PathVariable String classId, Model model) {
        FitnessClass fc = bookingService.findClassById(classId);
        List<Booking> bookings = bookingService.getBookingsByClass(classId);
        model.addAttribute("fitnessClass", fc);
        model.addAttribute("bookings", bookings);
        return "booking/class-bookings"; // → templates/booking/class-bookings.html
    }

    // ── ADMIN — all bookings list ─────────────────────────────────────────────
    @GetMapping("/admin/bookings")
    public String allBookings(Model model) {
        model.addAttribute("bookings", bookingService.getAllBookings());
        return "booking/all-bookings";  // → templates/booking/all-bookings.html
    }

    // ── ADMIN — mark attendance ───────────────────────────────────────────────
    @PostMapping("/admin/attend/{bookingId}")
    public String markAttended(@PathVariable String bookingId, Model model) {
        bookingService.updateBookingStatus(bookingId, "ATTENDED");
        model.addAttribute("bookings", bookingService.getAllBookings());
        model.addAttribute("message", "Marked as attended.");
        return "booking/all-bookings";
    }

    // ── ADMIN — delete class ──────────────────────────────────────────────────
    @PostMapping("/admin/delete/{classId}")
    public String deleteClass(@PathVariable String classId, Model model) {
        bookingService.deleteClass(classId);
        model.addAttribute("classes", bookingService.getAllClasses());
        model.addAttribute("message", "Class deleted.");
        return "booking/admin-classes";
    }
}

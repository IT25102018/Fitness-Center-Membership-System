package controller;

import com.fitness.fitnesscentersystem.model.User;
import com.fitness.fitnesscentersystem.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;



@Controller
@RequestMapping("/user")
public class UserServlet {

    private final UserService userService = new UserService();

    // ── REGISTER ─────────────────────────────────
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "user/register";   // → templates/user/register.html
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user, Model model) {
        boolean success = userService.registerUser(user);
        if (success) {
            model.addAttribute("message", "Registration successful! Please login.");
            return "user/login";
        } else {
            model.addAttribute("error", "Username already exists.");
            return "user/register";
        }
    }

    // ── LOGIN ─────────────────────────────────────
    @GetMapping("/login")
    public String showLoginForm() {
        return "user/login";      // → templates/user/login.html
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam String username,
                            @RequestParam String password,
                            HttpSession session, Model model) {
        User user = userService.login(username, password);
        if (user != null) {
            session.setAttribute("loggedUser", user);
            return "redirect:/";
        }
        model.addAttribute("error", "Invalid username or password.");
        return "user/login";
    }

    // ── PROFILE (READ) ────────────────────────────
    @GetMapping("/profile/{userId}")
    public String viewProfile(@PathVariable String userId, Model model) {
        User user = userService.findById(userId);
        model.addAttribute("user", user);
        return "user/profile";    // → templates/user/profile.html
    }

    // ── UPDATE ────────────────────────────────────
    @PostMapping("/update")
    public String updateUser(@ModelAttribute User user, Model model) {
        boolean updated = userService.updateUser(user);
        model.addAttribute("user", user);
        model.addAttribute("message", updated ? "Profile updated!" : "Update failed.");
        return "user/profile";
    }

    // ── LIST (Admin view) ─────────────────────────
    @GetMapping("/list")
    public String listUsers(Model model) {
        model.addAttribute("users.txt", userService.getAllUsers());
        return "user/list";       // → templates/user/list.html
    }

    // ── DELETE ────────────────────────────────────
    @PostMapping("/delete/{userId}")
    public String deleteUser(@PathVariable String userId, Model model) {
        userService.deleteUser(userId);
        model.addAttribute("users.txt", userService.getAllUsers());
        model.addAttribute("message", "User deleted.");
        return "user/list";
    }

    // ── LOGOUT ───────────────────────────────────
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/user/login";
    }
}

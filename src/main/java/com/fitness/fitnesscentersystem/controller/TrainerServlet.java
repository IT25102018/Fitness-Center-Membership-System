package com.fitness.fitnesscentersystem.controller;

import com.fitness.fitnesscentersystem.model.Trainer;
import com.fitness.fitnesscentersystem.service.TrainerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/trainer")
public class TrainerServlet {

    private final TrainerService trainerService = new TrainerService();

    // ── LIST all trainers (READ) ──────────────────────────────────────────────
    @GetMapping("/list")
    public String listTrainers(Model model) {
        model.addAttribute("trainers", trainerService.getAllTrainers());
        return "trainer/list";          // → templates/trainer/list.html
    }

    // ── SEARCH trainers ───────────────────────────────────────────────────────
    @GetMapping("/search")
    public String searchTrainers(@RequestParam(required = false) String keyword,
                                 Model model) {
        if (keyword != null && !keyword.trim().isEmpty()) {
            model.addAttribute("trainers", trainerService.search(keyword));
            model.addAttribute("keyword", keyword);
        } else {
            model.addAttribute("trainers", trainerService.getAllTrainers());
        }
        return "trainer/list";
    }

    // ── ADD trainer form ──────────────────────────────────────────────────────
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("trainer", new Trainer());
        return "trainer/add";           // → templates/trainer/add.html
    }

    @PostMapping("/add")
    public String addTrainer(@ModelAttribute Trainer trainer, Model model) {
        boolean success = trainerService.addTrainer(trainer);
        if (success) {
            return "redirect:/trainer/list";
        }
        model.addAttribute("error", "A trainer with that email already exists.");
        model.addAttribute("trainer", trainer);
        return "trainer/add";
    }

    // ── VIEW single trainer (READ) ────────────────────────────────────────────
    @GetMapping("/view/{trainerId}")
    public String viewTrainer(@PathVariable String trainerId, Model model) {
        Trainer trainer = trainerService.findById(trainerId);
        if (trainer == null) return "redirect:/trainer/list";
        model.addAttribute("trainer", trainer);
        return "trainer/view";          // → templates/trainer/view.html
    }

    // ── EDIT trainer form ─────────────────────────────────────────────────────
    @GetMapping("/edit/{trainerId}")
    public String showEditForm(@PathVariable String trainerId, Model model) {
        Trainer trainer = trainerService.findById(trainerId);
        if (trainer == null) return "redirect:/trainer/list";
        model.addAttribute("trainer", trainer);
        return "trainer/edit";          // → templates/trainer/edit.html
    }

    @PostMapping("/edit")
    public String updateTrainer(@ModelAttribute Trainer trainer, Model model) {
        boolean updated = trainerService.updateTrainer(trainer);
        model.addAttribute("message",
                updated ? "Trainer updated successfully!" : "Update failed.");
        model.addAttribute("trainers", trainerService.getAllTrainers());
        return "trainer/list";
    }

    // ── DELETE trainer ────────────────────────────────────────────────────────
    @PostMapping("/delete/{trainerId}")
    public String deleteTrainer(@PathVariable String trainerId, Model model) {
        trainerService.deleteTrainer(trainerId);
        model.addAttribute("message", "Trainer removed.");
        model.addAttribute("trainers", trainerService.getAllTrainers());
        return "trainer/list";
    }

    // ── DEACTIVATE trainer (soft delete) ─────────────────────────────────────
    @PostMapping("/deactivate/{trainerId}")
    public String deactivateTrainer(@PathVariable String trainerId, Model model) {
        trainerService.deactivateTrainer(trainerId);
        model.addAttribute("message", "Trainer set to INACTIVE.");
        model.addAttribute("trainers", trainerService.getAllTrainers());
        return "trainer/list";
    }
}

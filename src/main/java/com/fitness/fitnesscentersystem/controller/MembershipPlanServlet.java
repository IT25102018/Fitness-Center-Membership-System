package com.fitness.fitnesscentersystem.controller;

import com.fitness.fitnesscentersystem.model.MembershipPlan;
import com.fitness.fitnesscentersystem.service.MembershipPlanService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/plan")
public class MembershipPlanServlet {

    private final MembershipPlanService planService = new MembershipPlanService();

    // ── LIST all plans (READ) ─────────────────────────────────────────────────
    @GetMapping("/list")
    public String listPlans(Model model) {
        model.addAttribute("plans", planService.getAllPlans());
        return "plan/list";            // → templates/plan/list.html
    }

    // ── ADD plan form ─────────────────────────────────────────────────────────
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("plan", new MembershipPlan());
        return "plan/add";             // → templates/plan/add.html
    }

    @PostMapping("/add")
    public String addPlan(@ModelAttribute MembershipPlan plan, Model model) {
        boolean success = planService.addPlan(plan);
        if (success) {
            return "redirect:/plan/list";
        }
        model.addAttribute("error", "A plan with that name already exists.");
        model.addAttribute("plan", plan);
        return "plan/add";
    }

    // ── EDIT plan form ────────────────────────────────────────────────────────
    @GetMapping("/edit/{planId}")
    public String showEditForm(@PathVariable String planId, Model model) {
        MembershipPlan plan = planService.findById(planId);
        if (plan == null) return "redirect:/plan/list";
        model.addAttribute("plan", plan);
        return "plan/edit";            // → templates/plan/edit.html
    }

    @PostMapping("/edit")
    public String updatePlan(@ModelAttribute MembershipPlan plan, Model model) {
        boolean updated = planService.updatePlan(plan);
        model.addAttribute("message", updated ? "Plan updated successfully!" : "Update failed.");
        model.addAttribute("plans", planService.getAllPlans());
        return "plan/list";
    }

    // ── DELETE plan ───────────────────────────────────────────────────────────
    @PostMapping("/delete/{planId}")
    public String deletePlan(@PathVariable String planId, Model model) {
        planService.deletePlan(planId);
        model.addAttribute("message", "Plan deleted.");
        model.addAttribute("plans", planService.getAllPlans());
        return "plan/list";
    }

    // ── VIEW single plan (READ) ───────────────────────────────────────────────
    @GetMapping("/view/{planId}")
    public String viewPlan(@PathVariable String planId, Model model) {
        MembershipPlan plan = planService.findById(planId);
        model.addAttribute("plan", plan);
        return "plan/view";            // → templates/plan/view.html
    }

    // ── SEARCH by price range ─────────────────────────────────────────────────
    @GetMapping("/search")
    public String searchByPrice(@RequestParam(required = false) Double min,
                                @RequestParam(required = false) Double max,
                                Model model) {
        if (min != null && max != null) {
            model.addAttribute("plans", planService.findByPriceRange(min, max));
            model.addAttribute("min", min);
            model.addAttribute("max", max);
        } else {
            model.addAttribute("plans", planService.getAllPlans());
        }
        return "plan/list";
    }
}

package com.fitness.fitnesscentersystem.controller;

import java.util.List;

import com.fitness.fitnesscentersystem.model.Equipment;
import com.fitness.fitnesscentersystem.service.EquipmentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/equipment")
public class EquipmentServlet {

    private final EquipmentService equipmentService = new EquipmentService();

    // ── LIST all equipment (READ) ─────────────────────────────────────────────
    @GetMapping("/list")
    public String listEquipment(Model model) {
        model.addAttribute("equipmentList", equipmentService.getAllEquipment());
        model.addAttribute("totalValue",    equipmentService.getTotalEquipmentValue());
        model.addAttribute("needsRepair",   equipmentService.getEquipmentNeedingRepair().size());
        return "equipment/list";        // → templates/equipment/list.html
    }

    // ── SEARCH equipment ──────────────────────────────────────────────────────
    @GetMapping("/search")
    public String searchEquipment(@RequestParam(required = false) String keyword,
                                  @RequestParam(required = false) String category,
                                  @RequestParam(required = false) String status,
                                  Model model) {
        List<Equipment> results;

        if (keyword != null && !keyword.trim().isEmpty()) {
            results = equipmentService.search(keyword);
            model.addAttribute("keyword", keyword);
        } else if (category != null && !category.trim().isEmpty()) {
            results = equipmentService.findByCategory(category);
            model.addAttribute("selectedCategory", category);
        } else if (status != null && !status.trim().isEmpty()) {
            results = equipmentService.findByStatus(status);
            model.addAttribute("selectedStatus", status);
        } else {
            results = equipmentService.getAllEquipment();
        }

        model.addAttribute("equipmentList", results);
        model.addAttribute("totalValue",    equipmentService.getTotalEquipmentValue());
        model.addAttribute("needsRepair",   equipmentService.getEquipmentNeedingRepair().size());
        return "equipment/list";
    }

    // ── ADD equipment form ────────────────────────────────────────────────────
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("equipment", new Equipment());
        return "equipment/add";         // → templates/equipment/add.html
    }

    @PostMapping("/add")
    public String addEquipment(@ModelAttribute Equipment equipment, Model model) {
        equipmentService.addEquipment(equipment);
        return "redirect:/equipment/list";
    }

    // ── VIEW single equipment (READ) ──────────────────────────────────────────
    @GetMapping("/view/{equipmentId}")
    public String viewEquipment(@PathVariable String equipmentId, Model model) {
        Equipment equipment = equipmentService.findById(equipmentId);
        if (equipment == null) return "redirect:/equipment/list";
        model.addAttribute("equipment", equipment);
        return "equipment/view";        // → templates/equipment/view.html
    }

    // ── EDIT equipment form ───────────────────────────────────────────────────
    @GetMapping("/edit/{equipmentId}")
    public String showEditForm(@PathVariable String equipmentId, Model model) {
        Equipment equipment = equipmentService.findById(equipmentId);
        if (equipment == null) return "redirect:/equipment/list";
        model.addAttribute("equipment", equipment);
        return "equipment/edit";        // → templates/equipment/edit.html
    }

    @PostMapping("/edit")
    public String updateEquipment(@ModelAttribute Equipment equipment, Model model) {
        boolean updated = equipmentService.updateEquipment(equipment);
        model.addAttribute("message",
                updated ? "Equipment updated successfully!" : "Update failed.");
        model.addAttribute("equipmentList", equipmentService.getAllEquipment());
        model.addAttribute("totalValue",    equipmentService.getTotalEquipmentValue());
        model.addAttribute("needsRepair",   equipmentService.getEquipmentNeedingRepair().size());
        return "equipment/list";
    }

    // ── UPDATE status only ────────────────────────────────────────────────────
    @PostMapping("/status/{equipmentId}")
    public String updateStatus(@PathVariable String equipmentId,
                               @RequestParam String newStatus,
                               Model model) {
        equipmentService.updateStatus(equipmentId, newStatus);
        model.addAttribute("message", "Status updated to: " + newStatus);
        model.addAttribute("equipmentList", equipmentService.getAllEquipment());
        model.addAttribute("totalValue",    equipmentService.getTotalEquipmentValue());
        model.addAttribute("needsRepair",   equipmentService.getEquipmentNeedingRepair().size());
        return "equipment/list";
    }

    // ── DELETE equipment ──────────────────────────────────────────────────────
    @PostMapping("/delete/{equipmentId}")
    public String deleteEquipment(@PathVariable String equipmentId, Model model) {
        equipmentService.deleteEquipment(equipmentId);
        model.addAttribute("message", "Equipment removed.");
        model.addAttribute("equipmentList", equipmentService.getAllEquipment());
        model.addAttribute("totalValue",    equipmentService.getTotalEquipmentValue());
        model.addAttribute("needsRepair",   equipmentService.getEquipmentNeedingRepair().size());
        return "equipment/list";
    }
}

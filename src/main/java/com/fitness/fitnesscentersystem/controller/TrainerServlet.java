package com.fitness.fitnesscentersystem.controller;

import com.fitness.fitnesscentersystem.model.Trainer;
import com.fitness.fitnesscentersystem.service.TrainerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/trainer")
public class TrainerServlet {

    private final TrainerService trainerService = new TrainerService();

    // ── DTO for Form Binding ──────────────────────────────────────────────────
    public static class TrainerDTO {
        private String trainerId;
        private String name;
        private String email;
        private String phone;
        private String specialization;
        private String availability;
        private Double salary;
        private String status;
        private String trainerType;
        private String contractStartDate;
        private Integer annualLeaveDays;
        private String department;
        private Integer hoursPerWeek;
        private Double hourlyRate;

        // Base
        public String getTrainerId() { return trainerId; } public void setTrainerId(String trainerId) { this.trainerId = trainerId; }
        public String getName() { return name; } public void setName(String name) { this.name = name; }
        public String getEmail() { return email; } public void setEmail(String email) { this.email = email; }
        public String getPhone() { return phone; } public void setPhone(String phone) { this.phone = phone; }
        public String getSpecialization() { return specialization; } public void setSpecialization(String specialization) { this.specialization = specialization; }
        public String getAvailability() { return availability; } public void setAvailability(String availability) { this.availability = availability; }
        public Double getSalary() { return salary; } public void setSalary(Double salary) { this.salary = salary; }
        public String getStatus() { return status; } public void setStatus(String status) { this.status = status; }
        // Specific
        public String getTrainerType() { return trainerType; } public void setTrainerType(String trainerType) { this.trainerType = trainerType; }
        public String getContractStartDate() { return contractStartDate; } public void setContractStartDate(String contractStartDate) { this.contractStartDate = contractStartDate; }
        public Integer getAnnualLeaveDays() { return annualLeaveDays; } public void setAnnualLeaveDays(Integer annualLeaveDays) { this.annualLeaveDays = annualLeaveDays; }
        public String getDepartment() { return department; } public void setDepartment(String department) { this.department = department; }
        public Integer getHoursPerWeek() { return hoursPerWeek; } public void setHoursPerWeek(Integer hoursPerWeek) { this.hoursPerWeek = hoursPerWeek; }
        public Double getHourlyRate() { return hourlyRate; } public void setHourlyRate(Double hourlyRate) { this.hourlyRate = hourlyRate; }
    }

    private Trainer convertToTrainer(TrainerDTO dto) {
        if ("PART_TIME".equals(dto.getTrainerType())) {
            com.fitness.fitnesscentersystem.model.PartTimeTrainer pt = new com.fitness.fitnesscentersystem.model.PartTimeTrainer();
            pt.setTrainerId(dto.getTrainerId());
            pt.setName(dto.getName());
            pt.setEmail(dto.getEmail());
            pt.setPhone(dto.getPhone());
            pt.setSpecialization(dto.getSpecialization());
            pt.setAvailability(dto.getAvailability());
            pt.setStatus(dto.getStatus());
            if (dto.getHoursPerWeek() != null) pt.setHoursPerWeek(dto.getHoursPerWeek());
            if (dto.getHourlyRate() != null) pt.setHourlyRate(dto.getHourlyRate());
            return pt;
        } else {
            com.fitness.fitnesscentersystem.model.FullTimeTrainer ft = new com.fitness.fitnesscentersystem.model.FullTimeTrainer();
            ft.setTrainerId(dto.getTrainerId());
            ft.setName(dto.getName());
            ft.setEmail(dto.getEmail());
            ft.setPhone(dto.getPhone());
            ft.setSpecialization(dto.getSpecialization());
            ft.setAvailability(dto.getAvailability());
            ft.setStatus(dto.getStatus());
            if (dto.getSalary() != null) ft.setSalary(dto.getSalary());
            ft.setContractStartDate(dto.getContractStartDate());
            if (dto.getAnnualLeaveDays() != null) ft.setAnnualLeaveDays(dto.getAnnualLeaveDays());
            ft.setDepartment(dto.getDepartment());
            return ft;
        }
    }

    private TrainerDTO convertToDTO(Trainer t) {
        TrainerDTO dto = new TrainerDTO();
        dto.setTrainerId(t.getTrainerId());
        dto.setName(t.getName());
        dto.setEmail(t.getEmail());
        dto.setPhone(t.getPhone());
        dto.setSpecialization(t.getSpecialization());
        dto.setAvailability(t.getAvailability());
        dto.setStatus(t.getStatus());
        dto.setTrainerType(t.getTrainerType());

        if (t instanceof com.fitness.fitnesscentersystem.model.FullTimeTrainer) {
            com.fitness.fitnesscentersystem.model.FullTimeTrainer ft = (com.fitness.fitnesscentersystem.model.FullTimeTrainer) t;
            dto.setSalary(ft.getSalary());
            dto.setContractStartDate(ft.getContractStartDate());
            dto.setAnnualLeaveDays(ft.getAnnualLeaveDays());
            dto.setDepartment(ft.getDepartment());
        } else if (t instanceof com.fitness.fitnesscentersystem.model.PartTimeTrainer) {
            com.fitness.fitnesscentersystem.model.PartTimeTrainer pt = (com.fitness.fitnesscentersystem.model.PartTimeTrainer) t;
            dto.setHoursPerWeek(pt.getHoursPerWeek());
            dto.setHourlyRate(pt.getHourlyRate());
        }
        return dto;
    }

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
        TrainerDTO dto = new TrainerDTO();
        dto.setTrainerType("FULL_TIME"); // default
        model.addAttribute("trainer", dto);
        return "trainer/add";           // → templates/trainer/add.html
    }

    @PostMapping("/add")
    public String addTrainer(@ModelAttribute TrainerDTO trainerDto, Model model) {
        String email = trainerDto.getEmail();
        String phone = trainerDto.getPhone();

        // 1. Email Validation
        if (email == null || email.trim().isEmpty()) {
            model.addAttribute("error", "Email address is required.");
            model.addAttribute("trainer", trainerDto);
            return "trainer/add";
        }
        String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
        if (!email.matches(emailRegex)) {
            model.addAttribute("error", "Invalid email format. Please enter a valid email (e.g., example@domain.com).");
            model.addAttribute("trainer", trainerDto);
            return "trainer/add";
        }

        // 2. Phone Number Validation
        if (phone == null || phone.trim().isEmpty()) {
            model.addAttribute("error", "Phone number is required.");
            model.addAttribute("trainer", trainerDto);
            return "trainer/add";
        }
        String phoneRegex = "^0[0-9]{9}$";
        if (!phone.matches(phoneRegex)) {
            model.addAttribute("error", "Invalid phone number. Must be exactly 10 digits starting with 0 (e.g., 0771234567).");
            model.addAttribute("trainer", trainerDto);
            return "trainer/add";
        }

        Trainer trainer = convertToTrainer(trainerDto);
        boolean success = trainerService.addTrainer(trainer);
        if (success) {
            return "redirect:/trainer/list";
        }
        model.addAttribute("error", "A trainer with that email already exists.");
        model.addAttribute("trainer", trainerDto);
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
        model.addAttribute("trainer", convertToDTO(trainer));
        return "trainer/edit";          // → templates/trainer/edit.html
    }

    @PostMapping("/edit")
    public String updateTrainer(@ModelAttribute TrainerDTO trainerDto, Model model) {
        Trainer trainer = convertToTrainer(trainerDto);
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

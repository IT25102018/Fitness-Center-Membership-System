package com.fitness.fitnesscentersystem.service;

import com.fitness.fitnesscentersystem.model.MembershipPlan;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MembershipPlanService {

    private static final String FILE_PATH = "src/main/resources/data/membership_plans.txt";

    // ── File Helpers ──────────────────────────────────────────────────────────

    private List<MembershipPlan> readAll() {
        List<MembershipPlan> plans = new ArrayList<>();
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            return plans;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    plans.add(MembershipPlan.fromFileString(line.trim()));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return plans;
    }

    private void writeAll(List<MembershipPlan> plans) {
        File file = new File(FILE_PATH);
        file.getParentFile().mkdirs();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, false))) {
            for (MembershipPlan p : plans) {
                bw.write(p.toFileString());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ── CREATE ────────────────────────────────────────────────────────────────

    public boolean addPlan(MembershipPlan plan) {
        if (findByName(plan.getPlanName()) != null) {
            return false; // Plan name already exists
        }
        plan.setPlanId("PLN-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase());
        List<MembershipPlan> plans = readAll();
        plans.add(plan);
        writeAll(plans);
        return true;
    }

    // ── READ — all ────────────────────────────────────────────────────────────

    public List<MembershipPlan> getAllPlans() {
        return readAll();
    }

    // ── READ — by ID ──────────────────────────────────────────────────────────

    public MembershipPlan findById(String planId) {
        for (MembershipPlan p : readAll()) {
            if (p.getPlanId().equals(planId)) return p;
        }
        return null;
    }

    // ── READ — by name ────────────────────────────────────────────────────────

    public MembershipPlan findByName(String planName) {
        for (MembershipPlan p : readAll()) {
            if (p.getPlanName().equalsIgnoreCase(planName)) return p;
        }
        return null;
    }

    // ── UPDATE ────────────────────────────────────────────────────────────────

    public boolean updatePlan(MembershipPlan updated) {
        List<MembershipPlan> plans = readAll();
        boolean found = false;
        for (int i = 0; i < plans.size(); i++) {
            if (plans.get(i).getPlanId().equals(updated.getPlanId())) {
                plans.set(i, updated);
                found = true;
                break;
            }
        }
        if (found) writeAll(plans);
        return found;
    }

    // ── DELETE ────────────────────────────────────────────────────────────────

    public boolean deletePlan(String planId) {
        List<MembershipPlan> plans = readAll();
        boolean removed = plans.removeIf(p -> p.getPlanId().equals(planId));
        if (removed) writeAll(plans);
        return removed;
    }

    // ── SEARCH by price range ─────────────────────────────────────────────────

    public List<MembershipPlan> findByPriceRange(double min, double max) {
        List<MembershipPlan> result = new ArrayList<>();
        for (MembershipPlan p : readAll()) {
            if (p.getPrice() >= min && p.getPrice() <= max) result.add(p);
        }
        return result;
    }
}

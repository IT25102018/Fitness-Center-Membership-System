package com.fitness.fitnesscentersystem.service;

import com.fitness.fitnesscentersystem.model.Equipment;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EquipmentService {

    private static final String FILE_PATH =
            "src/main/resources/data/equipment.txt";

    // ── File Helpers ──────────────────────────────────────────────────────────

    private List<Equipment> readAll() {
        List<Equipment> list = new ArrayList<>();
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            return list;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    list.add(Equipment.fromFileString(line.trim()));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    private void writeAll(List<Equipment> equipmentList) {
        File file = new File(FILE_PATH);
        file.getParentFile().mkdirs();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, false))) {
            for (Equipment e : equipmentList) {
                bw.write(e.toFileString());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ── CREATE ────────────────────────────────────────────────────────────────

    public boolean addEquipment(Equipment equipment) {
        equipment.setEquipmentId(
                "EQP-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase()
        );
        if (equipment.getStatus() == null || equipment.getStatus().isEmpty()) {
            equipment.setStatus("AVAILABLE");
        }
        List<Equipment> list = readAll();
        list.add(equipment);
        writeAll(list);
        return true;
    }

    // ── READ — all ────────────────────────────────────────────────────────────

    public List<Equipment> getAllEquipment() {
        return readAll();
    }

    // ── READ — by ID ──────────────────────────────────────────────────────────

    public Equipment findById(String equipmentId) {
        for (Equipment e : readAll()) {
            if (e.getEquipmentId().equals(equipmentId)) return e;
        }
        return null;
    }

    // ── READ — by category ────────────────────────────────────────────────────

    public List<Equipment> findByCategory(String category) {
        List<Equipment> result = new ArrayList<>();
        for (Equipment e : readAll()) {
            if (e.getCategory().equalsIgnoreCase(category)) result.add(e);
        }
        return result;
    }

    // ── READ — by status ─────────────────────────────────────────────────────

    public List<Equipment> findByStatus(String status) {
        List<Equipment> result = new ArrayList<>();
        for (Equipment e : readAll()) {
            if (e.getStatus().equalsIgnoreCase(status)) result.add(e);
        }
        return result;
    }

    // ── READ — search by name or brand ───────────────────────────────────────

    public List<Equipment> search(String keyword) {
        List<Equipment> result = new ArrayList<>();
        String kw = keyword.toLowerCase();
        for (Equipment e : readAll()) {
            if (e.getName().toLowerCase().contains(kw)
                    || e.getBrand().toLowerCase().contains(kw)) {
                result.add(e);
            }
        }
        return result;
    }

    // ── UPDATE ────────────────────────────────────────────────────────────────

    public boolean updateEquipment(Equipment updated) {
        List<Equipment> list = readAll();
        boolean found = false;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getEquipmentId().equals(updated.getEquipmentId())) {
                list.set(i, updated);
                found = true;
                break;
            }
        }
        if (found) writeAll(list);
        return found;
    }

    // ── UPDATE — status only ──────────────────────────────────────────────────

    public boolean updateStatus(String equipmentId, String newStatus) {
        Equipment equipment = findById(equipmentId);
        if (equipment == null) return false;
        equipment.setStatus(newStatus);
        return updateEquipment(equipment);
    }

    // ── DELETE ────────────────────────────────────────────────────────────────

    public boolean deleteEquipment(String equipmentId) {
        List<Equipment> list = readAll();
        boolean removed = list.removeIf(
                e -> e.getEquipmentId().equals(equipmentId)
        );
        if (removed) writeAll(list);
        return removed;
    }

    // ── SUMMARY — total value of all equipment ────────────────────────────────

    public double getTotalEquipmentValue() {
        double total = 0;
        for (Equipment e : readAll()) {
            total += e.getPurchasePrice() * e.getQuantity();
        }
        return total;
    }

    // ── SUMMARY — equipment needing repair ───────────────────────────────────

    public List<Equipment> getEquipmentNeedingRepair() {
        List<Equipment> result = new ArrayList<>();
        for (Equipment e : readAll()) {
            if ("NEEDS_REPAIR".equalsIgnoreCase(e.getCondition())
                    || "UNDER_MAINTENANCE".equalsIgnoreCase(e.getStatus())) {
                result.add(e);
            }
        }
        return result;
    }
}

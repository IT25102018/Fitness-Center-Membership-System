package com.fitness.fitnesscentersystem.service;

import com.fitness.fitnesscentersystem.model.Trainer;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TrainerService {

    private static final String FILE_PATH =
            "src/main/resources/data/trainers.txt";

    // ── File Helpers ──────────────────────────────────────────────────────────

    private List<Trainer> readAll() {
        List<Trainer> trainers = new ArrayList<>();
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            return trainers;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    trainers.add(Trainer.fromFileString(line.trim()));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return trainers;
    }

    private void writeAll(List<Trainer> trainers) {
        File file = new File(FILE_PATH);
        file.getParentFile().mkdirs();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, false))) {
            for (Trainer t : trainers) {
                bw.write(t.toFileString());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ── CREATE ────────────────────────────────────────────────────────────────

    public boolean addTrainer(Trainer trainer) {
        if (findByEmail(trainer.getEmail()) != null) {
            return false; // Email already registered
        }
        trainer.setTrainerId(
                "TRN-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase()
        );
        trainer.setStatus("ACTIVE");
        List<Trainer> trainers = readAll();
        trainers.add(trainer);
        writeAll(trainers);
        return true;
    }

    // ── READ — all ────────────────────────────────────────────────────────────

    public List<Trainer> getAllTrainers() {
        return readAll();
    }

    // ── READ — active trainers only ───────────────────────────────────────────

    public List<Trainer> getActiveTrainers() {
        List<Trainer> result = new ArrayList<>();
        for (Trainer t : readAll()) {
            if ("ACTIVE".equalsIgnoreCase(t.getStatus())) result.add(t);
        }
        return result;
    }

    // ── READ — by ID ──────────────────────────────────────────────────────────

    public Trainer findById(String trainerId) {
        for (Trainer t : readAll()) {
            if (t.getTrainerId().equals(trainerId)) return t;
        }
        return null;
    }

    // ── READ — by email ───────────────────────────────────────────────────────

    public Trainer findByEmail(String email) {
        for (Trainer t : readAll()) {
            if (t.getEmail().equalsIgnoreCase(email)) return t;
        }
        return null;
    }

    // ── READ — search by name or specialization ───────────────────────────────

    public List<Trainer> search(String keyword) {
        List<Trainer> result = new ArrayList<>();
        String kw = keyword.toLowerCase();
        for (Trainer t : readAll()) {
            if (t.getName().toLowerCase().contains(kw)
                    || t.getSpecialization().toLowerCase().contains(kw)) {
                result.add(t);
            }
        }
        return result;
    }

    // ── UPDATE ────────────────────────────────────────────────────────────────

    public boolean updateTrainer(Trainer updated) {
        List<Trainer> trainers = readAll();
        boolean found = false;
        for (int i = 0; i < trainers.size(); i++) {
            if (trainers.get(i).getTrainerId().equals(updated.getTrainerId())) {
                trainers.set(i, updated);
                found = true;
                break;
            }
        }
        if (found) writeAll(trainers);
        return found;
    }

    // ── DELETE ────────────────────────────────────────────────────────────────

    public boolean deleteTrainer(String trainerId) {
        List<Trainer> trainers = readAll();
        boolean removed = trainers.removeIf(
                t -> t.getTrainerId().equals(trainerId)
        );
        if (removed) writeAll(trainers);
        return removed;
    }

    // ── SOFT DELETE — set status to INACTIVE ──────────────────────────────────

    public boolean deactivateTrainer(String trainerId) {
        Trainer trainer = findById(trainerId);
        if (trainer == null) return false;
        trainer.setStatus("INACTIVE");
        return updateTrainer(trainer);
    }
}

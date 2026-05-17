package com.fitness.fitnesscentersystem.service;

import com.fitness.fitnesscentersystem.model.Booking;
import com.fitness.fitnesscentersystem.model.FitnessClass;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BookingService {

    private static final String BOOKING_FILE =
            "src/main/resources/data/bookings.txt";
    private static final String CLASS_FILE =
            "src/main/resources/data/classes.txt";

    // ══════════════════════════════════════════════════════════════════════════
    //  FILE HELPERS — BOOKINGS
    // ══════════════════════════════════════════════════════════════════════════

    private List<Booking> readAllBookings() {
        List<Booking> list = new ArrayList<>();
        File file = new File(BOOKING_FILE);
        if (!file.exists()) { file.getParentFile().mkdirs(); return list; }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty())
                    list.add(Booking.fromFileString(line.trim()));
            }
        } catch (IOException e) { e.printStackTrace(); }
        return list;
    }

    private void writeAllBookings(List<Booking> bookings) {
        File file = new File(BOOKING_FILE);
        file.getParentFile().mkdirs();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, false))) {
            for (Booking b : bookings) { bw.write(b.toFileString()); bw.newLine(); }
        } catch (IOException e) { e.printStackTrace(); }
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  FILE HELPERS — CLASSES
    // ══════════════════════════════════════════════════════════════════════════

    private List<FitnessClass> readAllClasses() {
        List<FitnessClass> list = new ArrayList<>();
        File file = new File(CLASS_FILE);
        if (!file.exists()) { file.getParentFile().mkdirs(); return list; }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty())
                    list.add(FitnessClass.fromFileString(line.trim()));
            }
        } catch (IOException e) { e.printStackTrace(); }
        return list;
    }

    private void writeAllClasses(List<FitnessClass> classes) {
        File file = new File(CLASS_FILE);
        file.getParentFile().mkdirs();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, false))) {
            for (FitnessClass fc : classes) { bw.write(fc.toFileString()); bw.newLine(); }
        } catch (IOException e) { e.printStackTrace(); }
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  FITNESS CLASS — CRUD
    // ══════════════════════════════════════════════════════════════════════════

    // CREATE — Add a new fitness class
    public boolean addClass(FitnessClass fitnessClass) {
        fitnessClass.setClassId(
                "CLS-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase()
        );
        fitnessClass.setEnrolledCount(0);
        if (fitnessClass.getStatus() == null || fitnessClass.getStatus().isEmpty()) {
            fitnessClass.setStatus("ACTIVE");
        }
        List<FitnessClass> classes = readAllClasses();
        classes.add(fitnessClass);
        writeAllClasses(classes);
        return true;
    }

    // READ — all classes
    public List<FitnessClass> getAllClasses() {
        return readAllClasses();
    }

    // READ — active classes only
    public List<FitnessClass> getActiveClasses() {
        List<FitnessClass> result = new ArrayList<>();
        for (FitnessClass fc : readAllClasses()) {
            if ("ACTIVE".equalsIgnoreCase(fc.getStatus())) result.add(fc);
        }
        return result;
    }

    // READ — find class by ID
    public FitnessClass findClassById(String classId) {
        for (FitnessClass fc : readAllClasses()) {
            if (fc.getClassId().equals(classId)) return fc;
        }
        return null;
    }

    // READ — search classes by name or trainer
    public List<FitnessClass> searchClasses(String keyword) {
        List<FitnessClass> result = new ArrayList<>();
        String kw = keyword.toLowerCase();
        for (FitnessClass fc : readAllClasses()) {
            if (fc.getClassName().toLowerCase().contains(kw)
                    || fc.getTrainerName().toLowerCase().contains(kw)) {
                result.add(fc);
            }
        }
        return result;
    }

    // UPDATE — update class details
    public boolean updateClass(FitnessClass updated) {
        List<FitnessClass> classes = readAllClasses();
        boolean found = false;
        for (int i = 0; i < classes.size(); i++) {
            if (classes.get(i).getClassId().equals(updated.getClassId())) {
                classes.set(i, updated);
                found = true;
                break;
            }
        }
        if (found) writeAllClasses(classes);
        return found;
    }

    // DELETE — remove a class
    public boolean deleteClass(String classId) {
        List<FitnessClass> classes = readAllClasses();
        boolean removed = classes.removeIf(fc -> fc.getClassId().equals(classId));
        if (removed) writeAllClasses(classes);
        return removed;
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  BOOKING — CRUD
    // ══════════════════════════════════════════════════════════════════════════

    // CREATE — Book a class for a user
    public String createBooking(String userId, String username, String classId) {
        FitnessClass fc = findClassById(classId);
        if (fc == null)                    return "Class not found.";
        if (!fc.hasAvailableSpots())       return "Class is fully booked.";
        if (!"ACTIVE".equals(fc.getStatus())) return "Class is not available.";

        // Check duplicate booking
        for (Booking b : readAllBookings()) {
            if (b.getUserId().equals(userId)
                    && b.getClassId().equals(classId)
                    && "CONFIRMED".equals(b.getBookingStatus())) {
                return "You have already booked this class.";
            }
        }

        // Create booking
        Booking booking = new Booking(
                "BKG-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase(),
                userId, username, classId, fc.getClassName(),
                LocalDate.now().toString(), "CONFIRMED"
        );

        // Increment enrolled count
        fc.setEnrolledCount(fc.getEnrolledCount() + 1);
        updateClass(fc);

        List<Booking> bookings = readAllBookings();
        bookings.add(booking);
        writeAllBookings(bookings);
        return "SUCCESS";
    }

    // READ — all bookings
    public List<Booking> getAllBookings() {
        return readAllBookings();
    }

    // READ — bookings for a specific user
    public List<Booking> getBookingsByUser(String userId) {
        List<Booking> result = new ArrayList<>();
        for (Booking b : readAllBookings()) {
            if (b.getUserId().equals(userId)) result.add(b);
        }
        return result;
    }

    // READ — bookings for a specific class
    public List<Booking> getBookingsByClass(String classId) {
        List<Booking> result = new ArrayList<>();
        for (Booking b : readAllBookings()) {
            if (b.getClassId().equals(classId)) result.add(b);
        }
        return result;
    }

    // READ — find booking by ID
    public Booking findBookingById(String bookingId) {
        for (Booking b : readAllBookings()) {
            if (b.getBookingId().equals(bookingId)) return b;
        }
        return null;
    }

    // UPDATE — update booking status
    public boolean updateBookingStatus(String bookingId, String newStatus) {
        List<Booking> bookings = readAllBookings();
        boolean found = false;
        for (Booking b : bookings) {
            if (b.getBookingId().equals(bookingId)) {
                b.setBookingStatus(newStatus);
                found = true;
                break;
            }
        }
        if (found) writeAllBookings(bookings);
        return found;
    }

    // DELETE — cancel a booking
    public String cancelBooking(String bookingId) {
        Booking booking = findBookingById(bookingId);
        if (booking == null) return "Booking not found.";
        if ("CANCELLED".equals(booking.getBookingStatus()))
            return "Booking is already cancelled.";

        // Decrement class enrolled count
        FitnessClass fc = findClassById(booking.getClassId());
        if (fc != null && fc.getEnrolledCount() > 0) {
            fc.setEnrolledCount(fc.getEnrolledCount() - 1);
            updateClass(fc);
        }

        booking.setBookingStatus("CANCELLED");
        List<Booking> bookings = readAllBookings();
        for (int i = 0; i < bookings.size(); i++) {
            if (bookings.get(i).getBookingId().equals(bookingId)) {
                bookings.set(i, booking);
                break;
            }
        }
        writeAllBookings(bookings);
        return "SUCCESS";
    }

    // DELETE — permanently remove booking record
    public boolean deleteBooking(String bookingId) {
        List<Booking> bookings = readAllBookings();
        boolean removed = bookings.removeIf(b -> b.getBookingId().equals(bookingId));
        if (removed) writeAllBookings(bookings);
        return removed;
    }
}

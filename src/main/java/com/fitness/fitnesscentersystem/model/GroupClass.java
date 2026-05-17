package com.fitness.fitnesscentersystem.model;

// Inheritance: GroupClass extends FitnessClass
public class GroupClass extends FitnessClass {

    private String difficultyLevel;  // BEGINNER, INTERMEDIATE, ADVANCED
    private boolean requiresEquipment;
    private String equipmentNeeded;  // e.g. Yoga Mat, Dumbbells

    public GroupClass() {
        super();
        this.setClassType("GROUP");
    }

    public GroupClass(String classId, String className, String trainerId,
                      String trainerName, String schedule, String duration,
                      int maxCapacity, int enrolledCount, String status,
                      String difficultyLevel, boolean requiresEquipment,
                      String equipmentNeeded) {
        super(classId, className, trainerId, trainerName, schedule,
                duration, maxCapacity, enrolledCount, "GROUP", status);
        this.difficultyLevel   = difficultyLevel;
        this.requiresEquipment = requiresEquipment;
        this.equipmentNeeded   = equipmentNeeded;
    }

    public String getDifficultyLevel()                       { return difficultyLevel; }
    public void   setDifficultyLevel(String difficultyLevel) { this.difficultyLevel = difficultyLevel; }

    public boolean isRequiresEquipment()                     { return requiresEquipment; }
    public void    setRequiresEquipment(boolean val)         { this.requiresEquipment = val; }

    public String getEquipmentNeeded()                       { return equipmentNeeded; }
    public void   setEquipmentNeeded(String equipmentNeeded) { this.equipmentNeeded = equipmentNeeded; }

    // Polymorphism: override toString from FitnessClass
    @Override
    public String toString() {
        return super.toString()
                + ", difficulty=" + difficultyLevel
                + ", requiresEquipment=" + requiresEquipment
                + ", equipment=" + equipmentNeeded;
    }

    // Abstraction: group class info hidden from caller
    public String getClassInfoSummary() {
        return "Difficulty: " + difficultyLevel + " | "
                + "Equipment: " + (requiresEquipment ? equipmentNeeded : "None needed") + " | "
                + "Spots Left: " + getAvailableSpots();
    }
}

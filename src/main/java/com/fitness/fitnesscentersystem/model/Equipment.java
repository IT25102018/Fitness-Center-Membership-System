package com.fitness.fitnesscentersystem.model;

public class Equipment {

    private String equipmentId;
    private String name;
    private String category;       // CARDIO, STRENGTH, FLEXIBILITY, RECOVERY
    private String brand;
    private int    quantity;
    private double purchasePrice;
    private String purchaseDate;   // format: YYYY-MM-DD
    private String condition;      // EXCELLENT, GOOD, FAIR, NEEDS_REPAIR
    private String status;         // AVAILABLE, IN_USE, UNDER_MAINTENANCE

    public Equipment() {}

    public Equipment(String equipmentId, String name, String category,
                     String brand, int quantity, double purchasePrice,
                     String purchaseDate, String condition, String status) {
        this.equipmentId   = equipmentId;
        this.name          = name;
        this.category      = category;
        this.brand         = brand;
        this.quantity      = quantity;
        this.purchasePrice = purchasePrice;
        this.purchaseDate  = purchaseDate;
        this.condition     = condition;
        this.status        = status;
    }

    // Getters & Setters
    public String getEquipmentId()                         { return equipmentId; }
    public void   setEquipmentId(String equipmentId)       { this.equipmentId = equipmentId; }

    public String getName()                                { return name; }
    public void   setName(String name)                     { this.name = name; }

    public String getCategory()                            { return category; }
    public void   setCategory(String category)             { this.category = category; }

    public String getBrand()                               { return brand; }
    public void   setBrand(String brand)                   { this.brand = brand; }

    public int  getQuantity()                              { return quantity; }
    public void setQuantity(int quantity)                  { this.quantity = quantity; }

    public double getPurchasePrice()                       { return purchasePrice; }
    public void   setPurchasePrice(double purchasePrice)   { this.purchasePrice = purchasePrice; }

    public String getPurchaseDate()                        { return purchaseDate; }
    public void   setPurchaseDate(String purchaseDate)     { this.purchaseDate = purchaseDate; }

    public String getCondition()                           { return condition; }
    public void   setCondition(String condition)           { this.condition = condition; }

    public String getStatus()                              { return status; }
    public void   setStatus(String status)                 { this.status = status; }

    // Save to file as pipe-separated line
    public String toFileString() {
        return equipmentId + "|" + name + "|" + category + "|" + brand + "|"
                + quantity + "|" + purchasePrice + "|" + purchaseDate + "|"
                + condition + "|" + status;
    }

    // Parse from file line
    public static Equipment fromFileString(String line) {
        String[] p = line.split("\\|", -1);
        Equipment e = new Equipment();
        e.setEquipmentId(p[0]);
        e.setName(p[1]);
        e.setCategory(p[2]);
        e.setBrand(p[3]);
        e.setQuantity(Integer.parseInt(p[4]));
        e.setPurchasePrice(Double.parseDouble(p[5]));
        e.setPurchaseDate(p[6]);
        e.setCondition(p[7]);
        e.setStatus(p[8]);
        return e;
    }

    @Override
    public String toString() {
        return "Equipment{id=" + equipmentId + ", name=" + name
                + ", category=" + category + ", status=" + status + "}";
    }
}

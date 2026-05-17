package model;

public class AdminUser extends User {

    private String adminLevel; // SUPER, REGULAR

    public AdminUser() {
        super();
        this.setRole("ADMIN");
    }

    public AdminUser(String userId, String username, String password,
                     String email, String phone, String adminLevel) {
        super(userId, username, password, email, phone, "N/A", "ADMIN");
        this.adminLevel = adminLevel;
    }

    public String getAdminLevel()              { return adminLevel; }
    public void   setAdminLevel(String level)  { this.adminLevel = level; }

    // Polymorphism: overrides parent toFileString
    @Override
    public String toFileString() {
        return super.toFileString() + "," + adminLevel;
    }

    // Admin-specific method (Abstraction)
    public boolean canDeleteUsers() {
        return "SUPER".equals(adminLevel);
    }
}

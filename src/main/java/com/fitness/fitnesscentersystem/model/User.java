package com.fitness.fitnesscentersystem.model;

public class User {

    private String userId;
    private String username;
    private String password;
    private String email;
    private String phone;
    private String membershipType; // BASIC, PREMIUM, VIP
    private String role;           // USER, ADMIN

    public User() {}

    public User(String userId, String username, String password,
                String email, String phone, String membershipType, String role) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.membershipType = membershipType;
        this.role = role;
    }

    // Getters & Setters
    public String getUserId()                      { return userId; }
    public void   setUserId(String userId)         { this.userId = userId; }

    public String getUsername()                    { return username; }
    public void   setUsername(String username)     { this.username = username; }

    public String getPassword()                    { return password; }
    public void   setPassword(String password)     { this.password = password; }

    public String getEmail()                       { return email; }
    public void   setEmail(String email)           { this.email = email; }

    public String getPhone()                       { return phone; }
    public void   setPhone(String phone)           { this.phone = phone; }

    public String getMembershipType()              { return membershipType; }
    public void   setMembershipType(String mt)     { this.membershipType = mt; }

    public String getRole()                        { return role; }
    public void   setRole(String role)             { this.role = role; }

    // Convert to CSV line for file storage
    public String toFileString() {
        return userId + "," + username + "," + password + ","
                + email + "," + phone + "," + membershipType + "," + role;
    }

    // Parse a CSV line from file
    public static User fromFileString(String line) {
        String[] parts = line.split(",");
        return new User(
                parts[0].trim(), parts[1].trim(), parts[2].trim(),
                parts[3].trim(), parts[4].trim(), parts[5].trim(), parts[6].trim()
        );
    }

    @Override
    public String toString() {
        return "User{id=" + userId + ", username=" + username
                + ", email=" + email + ", role=" + role + "}";
    }

}

package model;

public class User {
    private int id;
    private String name;
    private String email;
    private String password;
    private boolean verified;
    private String role;  // new field

    public User(int id, String name, String email, String password, boolean verified, String role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.verified = verified;
        this.role = role;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; } // setter added
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public boolean isVerified() { return verified; }
    public void setVerified(boolean verified) { this.verified = verified; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    @Override
    public String toString() {
        return String.format("👤 ID:%d | %s | %s | Verified:%s | Role:%s",
                id, name, email, verified ? "✅" : "❌", role);
    }
}

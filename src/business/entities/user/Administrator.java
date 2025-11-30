/* File Name: Administrator.java
 * Flight Reservation System - Term Project
 * Completed by: Ben Cacic, Rowan (Yi-Kai) Chen, Truman (Yao-Chu) Huang, Ryan Lau
 * Submittion Date: November 30, 2025
 */
package business.entities.user;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// Administrator entity class - extends User with admin-specific functionality
public class Administrator extends User {
    private int adminId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String employeeId;
    private LocalDateTime hireDate;
    private String department;
    private boolean isActive;
    private String accessLevel; // SUPER_ADMIN, ADMIN, LIMITED_ADMIN
    private List<String> permissions;
    private LocalDateTime lastLogin;
    private int systemChanges;

    // Constructors
    public Administrator() {
        super();
        this.setRole("ADMIN");
        this.isActive = true;
        this.accessLevel = "ADMIN";
        this.permissions = new ArrayList<>();
        this.systemChanges = 0;
        initializeDefaultPermissions();
    }

    public Administrator(String username, String password, String firstName, String lastName, 
                        String email, String employeeId) {
        super(username, password, "ADMIN");
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.employeeId = employeeId;
        this.hireDate = LocalDateTime.now();
        this.department = "IT Administration";
        this.isActive = true;
        this.accessLevel = "ADMIN";
        this.permissions = new ArrayList<>();
        this.systemChanges = 0;
        initializeDefaultPermissions();
    }

    public Administrator(int userId, String username, String password, int adminId,
                        String firstName, String lastName, String email, String phone,
                        String employeeId, LocalDateTime hireDate, String department,
                        String accessLevel) {
        super(userId, username, password, "ADMIN");
        this.adminId = adminId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.employeeId = employeeId;
        this.hireDate = hireDate;
        this.department = department;
        this.isActive = true;
        this.accessLevel = accessLevel != null ? accessLevel : "ADMIN";
        this.permissions = new ArrayList<>();
        this.systemChanges = 0;
        initializeDefaultPermissions();
    }

    // Initialize default permissions based on access level
    private void initializeDefaultPermissions() {
        permissions.clear();
        
        // Basic admin permissions
        permissions.add("VIEW_REPORTS");
        permissions.add("MANAGE_FLIGHTS");
        permissions.add("MANAGE_BOOKINGS");
        
        if ("SUPER_ADMIN".equals(accessLevel)) {
            permissions.add("MANAGE_USERS");
            permissions.add("SYSTEM_CONFIG");
            permissions.add("DATABASE_ADMIN");
            permissions.add("AUDIT_LOGS");
        } else if ("ADMIN".equals(accessLevel)) {
            permissions.add("MANAGE_AGENTS");
            permissions.add("VIEW_ANALYTICS");
        }
        // LIMITED_ADMIN gets only basic permissions
    }

    // Getters and Setters
    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public LocalDateTime getHireDate() {
        return hireDate;
    }

    public void setHireDate(LocalDateTime hireDate) {
        this.hireDate = hireDate;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(String accessLevel) {
        this.accessLevel = accessLevel;
        initializeDefaultPermissions(); // Reinitialize permissions when access level changes
    }

    public List<String> getPermissions() {
        return new ArrayList<>(permissions);
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = new ArrayList<>(permissions);
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }

    public int getSystemChanges() {
        return systemChanges;
    }

    public void setSystemChanges(int systemChanges) {
        this.systemChanges = systemChanges;
    }

    // Business methods
    public String getFullName() {
        return firstName + " " + lastName;
    }

    public boolean hasPermission(String permission) {
        return permissions.contains(permission);
    }

    public void addPermission(String permission) {
        if (!permissions.contains(permission)) {
            permissions.add(permission);
        }
    }

    public void removePermission(String permission) {
        permissions.remove(permission);
    }

    public boolean canManageUsers() {
        return hasPermission("MANAGE_USERS") && isActive;
    }

    public boolean canAccessSystemConfig() {
        return hasPermission("SYSTEM_CONFIG") && isActive;
    }

    public boolean canViewReports() {
        return hasPermission("VIEW_REPORTS") && isActive;
    }

    public boolean canManageFlights() {
        return hasPermission("MANAGE_FLIGHTS") && isActive;
    }

    public boolean isSuperAdmin() {
        return "SUPER_ADMIN".equals(accessLevel);
    }

    public boolean isLimitedAdmin() {
        return "LIMITED_ADMIN".equals(accessLevel);
    }

    public void recordLogin() {
        this.lastLogin = LocalDateTime.now();
    }

    public void incrementSystemChanges() {
        this.systemChanges++;
    }

    public boolean isRecentlyActive() {
        return lastLogin != null && lastLogin.isAfter(LocalDateTime.now().minusDays(7));
    }

    public boolean hasContactInfo() {
        return email != null && !email.trim().isEmpty() && 
               phone != null && !phone.trim().isEmpty();
    }

    public void deactivateAdmin() {
        this.isActive = false;
    }

    public void reactivateAdmin() {
        this.isActive = true;
    }

    public String getAdminSummary() {
        return String.format("Admin: %s (%s) - Level: %s, Active: %s, Permissions: %d", 
                           getFullName(), employeeId, accessLevel, isActive, permissions.size());
    }

    @Override
    public String toString() {
        return "Administrator{" +
                "adminId=" + adminId +
                ", fullName='" + getFullName() + '\'' +
                ", employeeId='" + employeeId + '\'' +
                ", department='" + department + '\'' +
                ", accessLevel='" + accessLevel + '\'' +
                ", isActive=" + isActive +
                ", systemChanges=" + systemChanges +
                ", permissions=" + permissions.size() +
                ", username='" + getUsername() + '\'' +
                '}';
    }
}

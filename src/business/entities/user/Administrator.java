/* File Name: Administrator.java
 * Flight Reservation System - Term Project
 * Completed by: Ben Cacic, Rowan (Yi-Kai) Chen, Truman (Yao-Chu) Huang, Ryan Lau
 * Submittion Date: November 30, 2025
 */
package business.entities.user;

import java.time.LocalDateTime;

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
    private LocalDateTime lastLogin;
    private int systemChanges;

    // Constructors
    public Administrator() {
        super();
        this.setRole("ADMIN");
        this.isActive = true;
        this.systemChanges = 0;
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
        this.systemChanges = 0;
    }

    public Administrator(int userId, String username, String password, int adminId,
                        String firstName, String lastName, String email, String phone,
                        String employeeId, LocalDateTime hireDate, String department) {
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
        this.systemChanges = 0;
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

    public boolean canManageSystem() {
        return isActive;
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
        return String.format("Admin: %s (%s) - Active: %s, Changes: %d", 
                           getFullName(), employeeId, isActive, systemChanges);
    }

    @Override
    public String toString() {
        return "Administrator{" +
                "adminId=" + adminId +
                ", fullName='" + getFullName() + '\'' +
                ", employeeId='" + employeeId + '\'' +
                ", department='" + department + '\'' +
                ", isActive=" + isActive +
                ", systemChanges=" + systemChanges +
                ", username='" + getUsername() + '\'' +
                '}';
    }
}

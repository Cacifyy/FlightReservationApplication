package business.entities.user;

import java.time.LocalDateTime;

// Flight Agent entity class - extends User with agent-specific functionality
public class FlightAgent extends User {
    private int agentId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String employeeId;
    private LocalDateTime hireDate;
    private String department;
    private boolean isActive;
    private int bookingsHandled;

    // Constructors
    public FlightAgent() {
        super();
        this.setRole("AGENT");
        this.isActive = true;
        this.bookingsHandled = 0;
    }

    public FlightAgent(String username, String password, String firstName, String lastName, 
                      String email, String employeeId) {
        super(username, password, "AGENT");
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.employeeId = employeeId;
        this.hireDate = LocalDateTime.now();
        this.department = "Customer Service";
        this.isActive = true;
        this.bookingsHandled = 0;
    }

    public FlightAgent(int userId, String username, String password, int agentId, 
                      String firstName, String lastName, String email, String phone, 
                      String employeeId, LocalDateTime hireDate, String department) {
        super(userId, username, password, "AGENT");
        this.agentId = agentId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.employeeId = employeeId;
        this.hireDate = hireDate;
        this.department = department;
        this.isActive = true;
        this.bookingsHandled = 0;
    }

    // Getters and Setters
    public int getAgentId() {
        return agentId;
    }

    public void setAgentId(int agentId) {
        this.agentId = agentId;
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

    public int getBookingsHandled() {
        return bookingsHandled;
    }

    public void setBookingsHandled(int bookingsHandled) {
        this.bookingsHandled = bookingsHandled;
    }

    // Business methods
    public String getFullName() {
        return firstName + " " + lastName;
    }

    public void incrementBookingsHandled() {
        this.bookingsHandled++;
    }

    public boolean canHandleBookings() {
        return isActive && isAgent();
    }

    public boolean isNewAgent() {
        return hireDate != null && hireDate.isAfter(LocalDateTime.now().minusMonths(3));
    }

    public boolean isExperienced() {
        return bookingsHandled >= 100;
    }

    public String getAgentLevel() {
        if (isNewAgent()) {
            return "Trainee";
        } else if (bookingsHandled < 50) {
            return "Junior";
        } else if (bookingsHandled < 200) {
            return "Senior";
        } else {
            return "Expert";
        }
    }

    public boolean hasContactInfo() {
        return email != null && !email.trim().isEmpty() && 
               phone != null && !phone.trim().isEmpty();
    }

    public void deactivateAgent() {
        this.isActive = false;
    }

    public void reactivateAgent() {
        this.isActive = true;
    }

    @Override
    public String toString() {
        return "FlightAgent{" +
                "agentId=" + agentId +
                ", fullName='" + getFullName() + '\'' +
                ", employeeId='" + employeeId + '\'' +
                ", department='" + department + '\'' +
                ", isActive=" + isActive +
                ", bookingsHandled=" + bookingsHandled +
                ", level='" + getAgentLevel() + '\'' +
                ", username='" + getUsername() + '\'' +
                '}';
    }
}

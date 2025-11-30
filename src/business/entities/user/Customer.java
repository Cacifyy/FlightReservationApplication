/* File Name: Customer.java
 * Flight Reservation System - Term Project
 * Completed by: Ben Cacic, Rowan (Yi-Kai) Chen, Truman (Yao-Chu) Huang, Ryan Lau
 * Submittion Date: November 30, 2025
 */
package business.entities.user;

import java.time.LocalDateTime;

/**
 * Customer entity class representing a customer in the flight reservation system
 * Extends User class and includes customer-specific information
 */
public class Customer extends User {
    private int customerId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;
    private LocalDateTime createdDate;
    
    // Default constructor
    public Customer() {
        super();
        this.setRole("CUSTOMER");
        this.createdDate = LocalDateTime.now();
    }
    
    // Constructor without IDs (for new customers)
    public Customer(String username, String password, String firstName, 
                   String lastName, String email, String phone, String address) {
        super(username, password, "CUSTOMER");
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.createdDate = LocalDateTime.now();
    }
    
    // Constructor with user ID (for existing customers)
    public Customer(int userId, String username, String password, 
                   String firstName, String lastName, String email, 
                   String phone, String address) {
        super(userId, username, password, "CUSTOMER");
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.createdDate = LocalDateTime.now();
    }
    
    // Full constructor with all IDs
    public Customer(int userId, int customerId, String username, String password,
                   String firstName, String lastName, String email, 
                   String phone, String address, LocalDateTime createdDate) {
        super(userId, username, password, "CUSTOMER");
        this.customerId = customerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.createdDate = createdDate;
    }
    
    // Getters and Setters
    public int getCustomerId() {
        return customerId;
    }
    
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
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
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public LocalDateTime getCreatedDate() {
        return createdDate;
    }
    
    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }
    
    // Business methods
    public String getFullName() {
        return firstName + " " + lastName;
    }
    
    public boolean hasValidEmail() {
        return email != null && email.contains("@") && email.contains(".");
    }
    
    public boolean hasValidPhone() {
        return phone != null && !phone.trim().isEmpty();
    }
    
    public boolean isProfileComplete() {
        return firstName != null && !firstName.trim().isEmpty() &&
               lastName != null && !lastName.trim().isEmpty() &&
               hasValidEmail() &&
               hasValidPhone();
    }
    
    public void updateContactInfo(String email, String phone, String address) {
        if (email != null && !email.trim().isEmpty()) {
            this.email = email;
        }
        if (phone != null && !phone.trim().isEmpty()) {
            this.phone = phone;
        }
        if (address != null && !address.trim().isEmpty()) {
            this.address = address;
        }
    }
    
    @Override
    public String toString() {
        return "Customer{" +
                "customerId=" + customerId +
                ", userId=" + getUserId() +
                ", username='" + getUsername() + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", createdDate=" + createdDate +
                '}';
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Customer customer = (Customer) obj;
        return customerId == customer.customerId && 
               getUserId() == customer.getUserId();
    }
    
    @Override
    public int hashCode() {
        return customerId * 31 + getUserId();
    }
}
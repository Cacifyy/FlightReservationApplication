/* File Name: CustomerController.java
 * Flight Reservation System - Term Project
 * Completed by: Ben Cacic, Rowan (Yi-Kai) Chen, Truman (Yao-Chu) Huang, Ryan Lau
 * Submittion Date: November 30, 2025
 */
package business.controllers;

import data.dao.UserDAO;
import data.dao.CustomerDAO;
import business.entities.user.Customer;

// Controller to coordinate user + customer creation and updates
public class CustomerController {
    private UserDAO userDAO;
    private CustomerDAO customerDAO;

    public CustomerController() {
        this.userDAO = new UserDAO();
        this.customerDAO = new CustomerDAO();
    }

    // Create user and customer rows. Returns customerId (>0) on success, -1 on failure
    public int createCustomerProfile(String username, String password, String firstName, String lastName, String email, String phone, String address) {
        // create user first
        int userId = userDAO.createUserReturningId(username, password, "CUSTOMER");
        if (userId <= 0) return -1;

        int customerId = customerDAO.createCustomer(userId, firstName, lastName, email, phone, address);
        if (customerId <= 0) {
            return -1;
        }
        return customerId;
    }

    public boolean updateCustomerProfile(int customerId, String firstName, String lastName, String email, String phone, String address) {
        return customerDAO.updateCustomer(customerId, firstName, lastName, email, phone, address);
    }

    public Customer getCustomer(int customerId) {
        return customerDAO.getCustomerById(customerId);
    }

    public java.util.List<Customer> getAllCustomers() {
        return customerDAO.getAllCustomers();
    }
}

package business.controllers;

import data.dao.UserDAO;

// Controller for handling user login operations
public class LoginController {
    private UserDAO userDAO;

    public LoginController() {
        this.userDAO = new UserDAO();
    }

    // Authenticate user
    public String login(String username, String password) {
        // Validate input
        if (username == null || username.trim().isEmpty()) {
            System.err.println("Username cannot be empty");
            return null;
        }

        if (password == null || password.trim().isEmpty()) {
            System.err.println("Password cannot be empty");
            return null;
        }

        // Authenticate through DAO
        String role = userDAO.authenticateUser(username, password);

        if (role != null) {
            System.out.println("Login successful for user: " + username + " (Role: " + role + ")");
        } else {
            System.err.println("Login failed for user: " + username);
        }

        return role;
    }

    // Get user ID by username
    public int getUserId(String username) {
        return userDAO.getUserId(username);
    }

    // Check if username exists
    public boolean usernameExists(String username) {
        return userDAO.usernameExists(username);
    }

    // Validate password strength
    public boolean isValidPassword(String password) {
        if (password == null || password.length() < 6) {
            return false;
        }
        return true;
    }
}
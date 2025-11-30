package business.controllers;

import data.dao.UserDAO;
import business.entities.user.User;

// Controller for handling user login operations
public class LoginController {
    private UserDAO userDAO;

    public LoginController() {
        this.userDAO = new UserDAO();
    }

    // Authenticate user and return User entity (or null)
    public User login(String username, String password) {
        // Validate input
        if (username == null || username.trim().isEmpty()) {
            System.err.println("Username cannot be empty");
            return null;
        }

        if (password == null || password.trim().isEmpty()) {
            System.err.println("Password cannot be empty");
            return null;
        }

        // Authenticate through DAO and return a User entity
        User user = userDAO.authenticateUserReturningUser(username, password);

        if (user != null) {
            System.out.println("Login successful for user: " + username + " (Role: " + user.getRole() + ")");
        } else {
            System.err.println("Login failed for user: " + username);
        }

        return user;
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
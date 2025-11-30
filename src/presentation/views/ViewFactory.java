package presentation.views;

import javax.swing.*;
import business.entities.user.User;
import business.entities.user.Customer;

// Simple factory Design Pattern to create views based on user role
public class ViewFactory {
    public static JPanel createView(User user, Customer customer) {
        String role = user != null ? user.getRole() : null;
        String username = user != null ? user.getUsername() : null;
        if (role == null) role = "";
        switch (role.toUpperCase()) {
            case "CUSTOMER":
                return new CustomerPanel(customer);
            case "AGENT":
                return new AgentPanel(username);
            case "ADMIN":
                return new AdminPanel();
            default:
                JPanel panel = new JPanel();
                panel.add(new JLabel("No view implemented for role: " + role));
                return panel;
        }
    }
}

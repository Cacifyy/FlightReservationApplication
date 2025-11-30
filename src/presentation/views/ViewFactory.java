package presentation.views;

import javax.swing.*;

// Simple factory to create role-specific views
public class ViewFactory {
    public static JPanel createView(String role, String username, Integer customerId) {
        if (role == null) role = "";
        switch (role.toUpperCase()) {
            case "CUSTOMER":
                return new CustomerPanel(username, customerId == null ? -1 : customerId);
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

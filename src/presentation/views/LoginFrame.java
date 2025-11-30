package presentation.views;

import business.controllers.LoginController;
import data.dao.CustomerDAO;
import presentation.views.ViewFactory;
import javax.swing.*;
import java.awt.*;

// Login GUI Frame
public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton exitButton;
    private LoginController loginController;

    public LoginFrame() {
        loginController = new LoginController();
        initComponents();
    }

    private void initComponents() {
        setTitle("Flight Reservation System - Login");
        setSize(450, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(new Color(240, 248, 255));

        // Title panel
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(70, 130, 180));
        JLabel titleLabel = new JLabel("Flight Reservation System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(240, 248, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Username
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(usernameLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        usernameField = new JTextField(20);
        usernameField.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(usernameField, gbc);

        // Password
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(passwordField, gbc);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(new Color(240, 248, 255));

        loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setPreferredSize(new Dimension(100, 35));
        loginButton.setBackground(new Color(70, 130, 180));
        loginButton.setForeground(Color.WHITE);
        loginButton.addActionListener(e -> handleLogin());

        exitButton = new JButton("Exit");
        exitButton.setFont(new Font("Arial", Font.BOLD, 14));
        exitButton.setPreferredSize(new Dimension(100, 35));
        exitButton.setBackground(new Color(220, 220, 220));
        exitButton.addActionListener(e -> System.exit(0));

        buttonPanel.add(loginButton);
        buttonPanel.add(exitButton);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(buttonPanel, gbc);

        // Add panels to main panel
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Info panel
        JPanel infoPanel = new JPanel();
        infoPanel.setBackground(new Color(240, 248, 255));
        JLabel infoLabel = new JLabel("Test accounts: admin/admin123, john_doe/customer123");
        infoLabel.setFont(new Font("Arial", Font.ITALIC, 11));
        infoLabel.setForeground(Color.GRAY);
        infoPanel.add(infoLabel);
        mainPanel.add(infoPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // Enter key listener
        passwordField.addActionListener(e -> handleLogin());
    }

    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter both username and password",
                    "Input Required",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String role = loginController.login(username, password);

        if (role != null) {
            JOptionPane.showMessageDialog(this,
                    "Login successful!\nWelcome, " + username,
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);

            openMainFrame(username, role);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Invalid username or password.\nPlease try again.",
                    "Login Failed",
                    JOptionPane.ERROR_MESSAGE);
            passwordField.setText("");
        }
    }

    private void openMainFrame(String username, String role) {
        SwingUtilities.invokeLater(() -> {
            // Create a simple main window that hosts role-specific views produced by the ViewFactory
            JFrame mainFrame = new JFrame("Flight Reservation - " + username + " (" + role + ")");
            mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            mainFrame.setSize(900, 600);
            mainFrame.setLocationRelativeTo(null);

            // Determine customer id if needed
            Integer customerId = null;
            if ("CUSTOMER".equalsIgnoreCase(role)) {
                CustomerDAO customerDAO = new CustomerDAO();
                int userId = loginController.getUserId(username);
                customerId = customerDAO.getCustomerIdByUserId(userId);
            }

            JPanel view = ViewFactory.createView(role, username, customerId);
            mainFrame.add(view);
            mainFrame.setVisible(true);
        });
    }
}
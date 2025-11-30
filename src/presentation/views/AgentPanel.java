package presentation.views;

import business.controllers.CustomerController;
import data.dao.FlightDAO;
import business.entities.flight.Flight;
import business.entities.user.Customer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

// DB-backed Agent panel for managing customers and viewing schedules
public class AgentPanel extends JPanel {
    private String username;
    private CustomerController customerController;
    private FlightDAO flightDAO;

    private DefaultTableModel customersModel;
    private DefaultTableModel schedulesModel;

    public AgentPanel(String username) {
        this.username = username;
        this.customerController = new CustomerController();
        this.flightDAO = new FlightDAO();
        initComponents();
        loadData();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        JLabel header = new JLabel("Agent: " + username);
        header.setFont(new Font("Arial", Font.BOLD, 18));
        header.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.add(header, BorderLayout.WEST);
        JButton signOutBtn = new JButton("Sign out");
        signOutBtn.addActionListener(e -> signOut());
        headerPanel.add(signOutBtn, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();

        JPanel custPanel = new JPanel(new BorderLayout());
        customersModel = new DefaultTableModel(new Object[]{"Cust ID","Username","First","Last","Email","Phone","Address"},0) {
            public boolean isCellEditable(int row, int column){ return false; }
        };
        JTable custTable = new JTable(customersModel);
        JScrollPane custScroll = new JScrollPane(custTable);
        JPanel custButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton addBtn = new JButton("Add Customer");
        JButton editBtn = new JButton("Edit Selected");
        addBtn.addActionListener(e -> onAdd());
        editBtn.addActionListener(e -> onEdit(custTable));
        custButtons.add(addBtn);
        custButtons.add(editBtn);
        custPanel.add(custScroll, BorderLayout.CENTER);
        custPanel.add(custButtons, BorderLayout.SOUTH);

        JPanel schedPanel = new JPanel(new BorderLayout());
        schedulesModel = new DefaultTableModel(new Object[]{"Flight ID","Flight#","Origin","Destination","Departure","Seats"},0) {
            public boolean isCellEditable(int row, int column){ return false; }
        };
        JTable schedTable = new JTable(schedulesModel);
        JScrollPane schedScroll = new JScrollPane(schedTable);
        schedPanel.add(schedScroll, BorderLayout.CENTER);

        tabs.addTab("Customers", custPanel);
        tabs.addTab("Schedules", schedPanel);
        add(tabs, BorderLayout.CENTER);
    }

    private void signOut() {
        java.awt.Window w = SwingUtilities.getWindowAncestor(this);
        if (w != null) w.dispose();
        SwingUtilities.invokeLater(() -> {
            LoginFrame login = new LoginFrame();
            login.setVisible(true);
        });
    }

    private void loadData() {
        customersModel.setRowCount(0);
        List<Customer> list = customerController.getAllCustomers();
        for (Customer c : list) {
            customersModel.addRow(new Object[]{c.getCustomerId(), c.getUsername(), c.getFirstName(), c.getLastName(), c.getEmail(), c.getPhone(), c.getAddress()});
        }

        schedulesModel.setRowCount(0);
        List<Flight> flights = flightDAO.getAllFlights();
        for (Flight f : flights) {
            schedulesModel.addRow(new Object[]{f.getFlightId(), f.getFlightNumber(), f.getOrigin(), f.getDestination(), f.getDepartureTime().toString(), f.getAvailableSeats()});
        }
    }

    private void onAdd() {
        CustomerDialogResult res = showCustomerDialog(null);
        if (res != null) {
            int custId = customerController.createCustomerProfile(res.username, res.password, res.first, res.last, res.email, res.phone, res.address);
            if (custId > 0) {
                JOptionPane.showMessageDialog(this, "Customer created with ID: " + custId, "Created", JOptionPane.INFORMATION_MESSAGE);
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to create customer. Check logs.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void onEdit(JTable table) {
        int sel = table.getSelectedRow();
        if (sel < 0) {
            JOptionPane.showMessageDialog(this, "Select a customer to edit", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int custId = (int) customersModel.getValueAt(sel, 0);
        Customer existing = customerController.getCustomer(custId);
        if (existing == null) {
            JOptionPane.showMessageDialog(this, "Failed to load customer details.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        CustomerDialogResult res = showCustomerDialog(existing);
        if (res != null) {
            boolean ok = customerController.updateCustomerProfile(custId, res.first, res.last, res.email, res.phone, res.address);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Customer updated.", "Updated", JOptionPane.INFORMATION_MESSAGE);
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update customer.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Simple dialog result holder
    private static class CustomerDialogResult {
        String username; String password; String first; String last; String email; String phone; String address;
    }

    private CustomerDialogResult showCustomerDialog(Customer existing) {
        JTextField usernameField = new JTextField(15);
        JTextField passwordField = new JTextField(15);
        JTextField firstField = new JTextField(12);
        JTextField lastField = new JTextField(12);
        JTextField emailField = new JTextField(18);
        JTextField phoneField = new JTextField(12);
        JTextField addressField = new JTextField(20);

        if (existing != null) {
            usernameField.setText(existing.getUsername());
            usernameField.setEditable(false);
            firstField.setText(existing.getFirstName());
            lastField.setText(existing.getLastName());
            emailField.setText(existing.getEmail());
            phoneField.setText(existing.getPhone());
            addressField.setText(existing.getAddress());
        }

        JPanel p = new JPanel(new GridLayout(0,2,6,6));
        p.add(new JLabel("Username:")); p.add(usernameField);
        p.add(new JLabel("Password:")); p.add(passwordField);
        p.add(new JLabel("First name:")); p.add(firstField);
        p.add(new JLabel("Last name:")); p.add(lastField);
        p.add(new JLabel("Email:")); p.add(emailField);
        p.add(new JLabel("Phone:")); p.add(phoneField);
        p.add(new JLabel("Address:")); p.add(addressField);

        int res = JOptionPane.showConfirmDialog(this, p, existing == null ? "Add Customer" : "Edit Customer", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (res != JOptionPane.OK_OPTION) return null;

        CustomerDialogResult out = new CustomerDialogResult();
        out.username = usernameField.getText().trim();
        out.password = passwordField.getText().trim();
        out.first = firstField.getText().trim();
        out.last = lastField.getText().trim();
        out.email = emailField.getText().trim();
        out.phone = phoneField.getText().trim();
        out.address = addressField.getText().trim();
        return out;
    }
}

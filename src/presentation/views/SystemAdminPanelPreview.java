package presentation.views;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

// Preview-only admin panel (no DB) for adding/removing flights and managing aircraft
public class SystemAdminPanelPreview extends JPanel {
    private DefaultTableModel flightsModel;
    private DefaultTableModel aircraftModel;

    public SystemAdminPanelPreview() {
        initComponents();
        loadMockData();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        JLabel header = new JLabel("System Administrator (Preview)");
        header.setFont(new Font("Arial", Font.BOLD, 18));
        header.setBorder(BorderFactory.createEmptyBorder(8,8,8,8));
        add(header, BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();

        // Flights
        JPanel flightsPanel = new JPanel(new BorderLayout());
        flightsModel = new DefaultTableModel(new Object[]{"Flight ID","Flight#","Origin","Destination","Departure","Price","Seats"},0) {
            public boolean isCellEditable(int row, int column){ return false; }
        };
        JTable flightsTable = new JTable(flightsModel);
        flightsPanel.add(new JScrollPane(flightsTable), BorderLayout.CENTER);
        JPanel flightsButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton addFlight = new JButton("Add Flight"); addFlight.setEnabled(false);
        JButton editFlight = new JButton("Edit Flight"); editFlight.setEnabled(false);
        JButton delFlight = new JButton("Delete Flight"); delFlight.setEnabled(false);
        flightsButtons.add(addFlight); flightsButtons.add(editFlight); flightsButtons.add(delFlight);
        flightsPanel.add(flightsButtons, BorderLayout.SOUTH);

        // Aircraft
        JPanel acPanel = new JPanel(new BorderLayout());
        aircraftModel = new DefaultTableModel(new Object[]{"Aircraft ID","Model","Capacity","Airline"},0) {
            public boolean isCellEditable(int row, int column){ return false; }
        };
        JTable acTable = new JTable(aircraftModel);
        acPanel.add(new JScrollPane(acTable), BorderLayout.CENTER);
        JPanel acButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton addAc = new JButton("Add Aircraft"); addAc.setEnabled(false);
        JButton editAc = new JButton("Edit Aircraft"); editAc.setEnabled(false);
        JButton delAc = new JButton("Delete Aircraft"); delAc.setEnabled(false);
        acButtons.add(addAc); acButtons.add(editAc); acButtons.add(delAc);
        acPanel.add(acButtons, BorderLayout.SOUTH);

        tabs.addTab("Flights", flightsPanel);
        tabs.addTab("Aircraft", acPanel);
        add(tabs, BorderLayout.CENTER);
    }

    private void loadMockData() {
        flightsModel.addRow(new Object[]{101, "AC101", "Calgary", "Toronto", "2025-12-01 08:00", "$450.00", 180});
        flightsModel.addRow(new Object[]{202, "WS202", "Calgary", "Vancouver", "2025-12-01 10:00", "$220.00", 150});

        aircraftModel.addRow(new Object[]{1, "Boeing 737", 180, "Air Canada"});
        aircraftModel.addRow(new Object[]{2, "Airbus A320", 150, "WestJet"});
    }
}

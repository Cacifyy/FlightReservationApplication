package presentation.views;

import business.controllers.AdminController;
import business.entities.flight.Aircraft;
import business.entities.flight.Flight;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class SystemAdminPanel extends JPanel {
    private AdminController adminController;
    private DefaultTableModel flightsModel;
    private DefaultTableModel aircraftModel;

    public SystemAdminPanel() {
        this.adminController = new AdminController();
        initComponents();
        loadData();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        JLabel header = new JLabel("System Administrator");
        header.setFont(new Font("Arial", Font.BOLD, 18));
        header.setBorder(BorderFactory.createEmptyBorder(8,8,8,8));
        add(header, BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();

        // Flights tab
        JPanel flightsPanel = new JPanel(new BorderLayout());
        flightsModel = new DefaultTableModel(new Object[]{"Flight ID","Flight#","Origin","Destination","Departure","Price","Seats"},0) {
            public boolean isCellEditable(int row, int column){ return false; }
        };
        JTable flightsTable = new JTable(flightsModel);
        flightsPanel.add(new JScrollPane(flightsTable), BorderLayout.CENTER);
        JPanel flightsButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton addFlight = new JButton("Add Flight");
        JButton editFlight = new JButton("Edit Flight");
        JButton delFlight = new JButton("Delete Flight");
        addFlight.addActionListener(e -> onAddFlight());
        editFlight.addActionListener(e -> onEditFlight(flightsTable));
        delFlight.addActionListener(e -> onDeleteFlight(flightsTable));
        flightsButtons.add(addFlight); flightsButtons.add(editFlight); flightsButtons.add(delFlight);
        flightsPanel.add(flightsButtons, BorderLayout.SOUTH);

        // Aircraft tab
        JPanel acPanel = new JPanel(new BorderLayout());
        aircraftModel = new DefaultTableModel(new Object[]{"Aircraft ID","Model","Capacity","Airline"},0) {
            public boolean isCellEditable(int row, int column){ return false; }
        };
        JTable acTable = new JTable(aircraftModel);
        acPanel.add(new JScrollPane(acTable), BorderLayout.CENTER);
        JPanel acButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton addAc = new JButton("Add Aircraft");
        JButton editAc = new JButton("Edit Aircraft");
        JButton delAc = new JButton("Delete Aircraft");
        addAc.addActionListener(e -> onAddAircraft());
        editAc.addActionListener(e -> onEditAircraft(acTable));
        delAc.addActionListener(e -> onDeleteAircraft(acTable));
        acButtons.add(addAc); acButtons.add(editAc); acButtons.add(delAc);
        acPanel.add(acButtons, BorderLayout.SOUTH);

        tabs.addTab("Flights", flightsPanel);
        tabs.addTab("Aircraft", acPanel);
        add(tabs, BorderLayout.CENTER);
    }

    private void loadData() {
        flightsModel.setRowCount(0);
        List<Flight> flights = adminController.getAllFlights();
        for (Flight f : flights) {
            flightsModel.addRow(new Object[]{f.getFlightId(), f.getFlightNumber(), f.getOrigin(), f.getDestination(), f.getDepartureTime().toString(), String.format("$%.2f", f.getPrice()), f.getAvailableSeats()});
        }

        aircraftModel.setRowCount(0);
        List<Aircraft> acs = adminController.getAllAircraft();
        for (Aircraft a : acs) {
            aircraftModel.addRow(new Object[]{a.getAircraftId(), a.getModel(), a.getCapacity(), a.getAirline()});
        }
    }

    private void onAddFlight() {
        FlightDialog dlg = new FlightDialog(null);
        Flight f = dlg.showDialog();
        if (f != null) {
            boolean ok = adminController.addFlight(f);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Flight added.", "Added", JOptionPane.INFORMATION_MESSAGE);
                loadData();
            } else JOptionPane.showMessageDialog(this, "Failed to add flight.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onEditFlight(JTable table) {
        int sel = table.getSelectedRow();
        if (sel < 0) { JOptionPane.showMessageDialog(this, "Select a flight to edit.", "No Selection", JOptionPane.WARNING_MESSAGE); return; }
        int flightId = (int) flightsModel.getValueAt(sel, 0);
        Flight existing = adminController.getFlightById(flightId);
        if (existing == null) { JOptionPane.showMessageDialog(this, "Failed to load flight.", "Error", JOptionPane.ERROR_MESSAGE); return; }
        FlightDialog dlg = new FlightDialog(existing);
        Flight f = dlg.showDialog();
        if (f != null) {
            f.setFlightId(flightId);
            boolean ok = adminController.updateFlight(f);
            if (ok) { JOptionPane.showMessageDialog(this, "Flight updated.", "Updated", JOptionPane.INFORMATION_MESSAGE); loadData(); }
            else JOptionPane.showMessageDialog(this, "Failed to update flight.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onDeleteFlight(JTable table) {
        int sel = table.getSelectedRow();
        if (sel < 0) { JOptionPane.showMessageDialog(this, "Select a flight to delete.", "No Selection", JOptionPane.WARNING_MESSAGE); return; }
        int flightId = (int) flightsModel.getValueAt(sel, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Delete flight " + flightId + "?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;
        boolean ok = adminController.deleteFlight(flightId);
        if (ok) { JOptionPane.showMessageDialog(this, "Flight deleted.", "Deleted", JOptionPane.INFORMATION_MESSAGE); loadData(); }
        else JOptionPane.showMessageDialog(this, "Failed to delete flight.", "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void onAddAircraft() {
        AircraftDialog dlg = new AircraftDialog(null);
        Aircraft a = dlg.showDialog();
        if (a != null) {
            int id = adminController.createAircraft(a.getModel(), a.getCapacity(), a.getAirline());
            if (id > 0) { JOptionPane.showMessageDialog(this, "Aircraft added.", "Added", JOptionPane.INFORMATION_MESSAGE); loadData(); }
            else JOptionPane.showMessageDialog(this, "Failed to add aircraft.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onEditAircraft(JTable table) {
        int sel = table.getSelectedRow();
        if (sel < 0) { JOptionPane.showMessageDialog(this, "Select an aircraft to edit.", "No Selection", JOptionPane.WARNING_MESSAGE); return; }
        int acId = (int) aircraftModel.getValueAt(sel, 0);
        List<Aircraft> acs = adminController.getAllAircraft();
        Aircraft existing = null;
        for (Aircraft a : acs) if (a.getAircraftId() == acId) { existing = a; break; }
        if (existing == null) { JOptionPane.showMessageDialog(this, "Failed to load aircraft.", "Error", JOptionPane.ERROR_MESSAGE); return; }
        AircraftDialog dlg = new AircraftDialog(existing);
        Aircraft a = dlg.showDialog();
        if (a != null) {
            a.setAircraftId(acId);
            boolean ok = adminController.updateAircraft(acId, a.getModel(), a.getCapacity(), a.getAirline());
            if (ok) { JOptionPane.showMessageDialog(this, "Aircraft updated.", "Updated", JOptionPane.INFORMATION_MESSAGE); loadData(); }
            else JOptionPane.showMessageDialog(this, "Failed to update aircraft.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onDeleteAircraft(JTable table) {
        int sel = table.getSelectedRow();
        if (sel < 0) { JOptionPane.showMessageDialog(this, "Select an aircraft to delete.", "No Selection", JOptionPane.WARNING_MESSAGE); return; }
        int acId = (int) aircraftModel.getValueAt(sel, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Delete aircraft " + acId + "?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;
        boolean ok = adminController.deleteAircraft(acId);
        if (ok) { JOptionPane.showMessageDialog(this, "Aircraft deleted.", "Deleted", JOptionPane.INFORMATION_MESSAGE); loadData(); }
        else JOptionPane.showMessageDialog(this, "Failed to delete aircraft.", "Error", JOptionPane.ERROR_MESSAGE);
    }

    // Simple dialogs for flight and aircraft
    private static class FlightDialog {
        private Flight flight;
        FlightDialog(Flight existing) { this.flight = existing == null ? new Flight() : existing; }
        Flight showDialog() {
            JTextField num = new JTextField(flight.getFlightNumber() == null ? "" : flight.getFlightNumber(), 12);
            JTextField origin = new JTextField(flight.getOrigin() == null ? "" : flight.getOrigin(), 10);
            JTextField dest = new JTextField(flight.getDestination() == null ? "" : flight.getDestination(), 10);
            JTextField dep = new JTextField(flight.getDepartureTime() == null ? "2025-12-01T08:00" : flight.getDepartureTime().toString(), 18);
            JTextField price = new JTextField(String.valueOf(flight.getPrice()), 8);
            JTextField seats = new JTextField(String.valueOf(flight.getAvailableSeats()), 6);
            JTextField acId = new JTextField(String.valueOf(flight.getAircraftId()), 6);

            JPanel p = new JPanel(new GridLayout(0,2,6,6));
            p.add(new JLabel("Flight #:")); p.add(num);
            p.add(new JLabel("Origin:")); p.add(origin);
            p.add(new JLabel("Destination:")); p.add(dest);
            p.add(new JLabel("Departure (YYYY-MM-DDTHH:MM):")); p.add(dep);
            p.add(new JLabel("Price:")); p.add(price);
            p.add(new JLabel("Seats:")); p.add(seats);
            p.add(new JLabel("Aircraft ID:")); p.add(acId);

            int res = JOptionPane.showConfirmDialog(null, p, "Flight", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (res != JOptionPane.OK_OPTION) return null;

            try {
                flight.setFlightNumber(num.getText().trim());
                flight.setOrigin(origin.getText().trim());
                flight.setDestination(dest.getText().trim());
                flight.setDepartureTime(java.time.LocalDateTime.parse(dep.getText().trim()));
                flight.setPrice(Double.parseDouble(price.getText().trim()));
                flight.setAvailableSeats(Integer.parseInt(seats.getText().trim()));
                flight.setAircraftId(Integer.parseInt(acId.getText().trim()));
                return flight;
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Invalid input: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                return null;
            }
        }
    }

    private static class AircraftDialog {
        private Aircraft aircraft;
        AircraftDialog(Aircraft existing) { this.aircraft = existing == null ? new Aircraft() : existing; }
        Aircraft showDialog() {
            JTextField model = new JTextField(aircraft.getModel() == null ? "" : aircraft.getModel(), 12);
            JTextField capacity = new JTextField(String.valueOf(aircraft.getCapacity()), 6);
            JTextField airline = new JTextField(aircraft.getAirline() == null ? "" : aircraft.getAirline(), 12);
            JPanel p = new JPanel(new GridLayout(0,2,6,6));
            p.add(new JLabel("Model:")); p.add(model);
            p.add(new JLabel("Capacity:")); p.add(capacity);
            p.add(new JLabel("Airline:")); p.add(airline);
            int res = JOptionPane.showConfirmDialog(null, p, "Aircraft", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (res != JOptionPane.OK_OPTION) return null;
            try {
                aircraft.setModel(model.getText().trim());
                aircraft.setCapacity(Integer.parseInt(capacity.getText().trim()));
                aircraft.setAirline(airline.getText().trim());
                return aircraft;
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Invalid input: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                return null;
            }
        }
    }
}

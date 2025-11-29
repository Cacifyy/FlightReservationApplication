package presentation.views;

import business.controllers.ReservationController;
import data.dao.FlightDAO;
import business.entities.flight.Flight;
import business.entities.booking.Reservation;
import business.controllers.LoginController;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.sql.Date;
import java.util.List;

// Panel for customer operations: search flights, book, cancel, view reservations
public class CustomerPanel extends JPanel {
    private String username;
    private int customerId;
    private FlightDAO flightDAO;
    private ReservationController reservationController;

    private JTextField originField;
    private JTextField destinationField;
    private JTextField dateField; // yyyy-MM-dd
    private JTable flightsTable;
    private DefaultTableModel flightsModel;

    private JTable reservationsTable;
    private DefaultTableModel reservationsModel;

    public CustomerPanel(String username, int customerId) {
        this.username = username;
        this.customerId = customerId;
        this.flightDAO = new FlightDAO();
        this.reservationController = new ReservationController();
        initComponents();
        loadAllFlights();
        refreshReservations();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JLabel header = new JLabel("Welcome, " + username);
        header.setFont(new Font("Arial", Font.BOLD, 18));
        header.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        add(header, BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();

        // Search tab
        JPanel searchPanel = new JPanel(new BorderLayout());
        JPanel searchForm = new JPanel(new FlowLayout(FlowLayout.LEFT));
        originField = new JTextField(10);
        destinationField = new JTextField(10);
        dateField = new JTextField(10);
        dateField.setToolTipText("Format: yyyy-MM-dd");
        JButton searchBtn = new JButton("Search");
        searchBtn.addActionListener(this::onSearch);

        searchForm.add(new JLabel("Origin:"));
        searchForm.add(originField);
        searchForm.add(new JLabel("Destination:"));
        searchForm.add(destinationField);
        searchForm.add(new JLabel("Date:"));
        searchForm.add(dateField);
        searchForm.add(searchBtn);

        flightsModel = new DefaultTableModel(new Object[]{"ID","Flight#","Origin","Destination","Departure","Price","Seats"},0) {
            public boolean isCellEditable(int row, int column){ return false; }
        };
        flightsTable = new JTable(flightsModel);
        JScrollPane flightsScroll = new JScrollPane(flightsTable);

        JPanel bookPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton bookBtn = new JButton("Book Selected Flight");
        bookBtn.addActionListener(e -> onBook());
        bookPanel.add(bookBtn);

        searchPanel.add(searchForm, BorderLayout.NORTH);
        searchPanel.add(flightsScroll, BorderLayout.CENTER);
        searchPanel.add(bookPanel, BorderLayout.SOUTH);

        // Reservations tab
        JPanel resPanel = new JPanel(new BorderLayout());
        reservationsModel = new DefaultTableModel(new Object[]{"Res ID","Flight ID","Seat","Status","Booked At"},0) {
            public boolean isCellEditable(int row, int column){ return false; }
        };
        reservationsTable = new JTable(reservationsModel);
        JScrollPane resScroll = new JScrollPane(reservationsTable);
        JPanel resButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton cancelBtn = new JButton("Cancel Selected Reservation");
        cancelBtn.addActionListener(e -> onCancel());
        resButtons.add(cancelBtn);

        resPanel.add(resScroll, BorderLayout.CENTER);
        resPanel.add(resButtons, BorderLayout.SOUTH);

        tabs.addTab("Search Flights", searchPanel);
        tabs.addTab("My Reservations", resPanel);

        add(tabs, BorderLayout.CENTER);
    }

    private void onSearch(ActionEvent e) {
        String origin = originField.getText().trim();
        String destination = destinationField.getText().trim();
        String dateStr = dateField.getText().trim();

        Date sqlDate = null;
        if (!dateStr.isEmpty()) {
            try {
                LocalDate ld = LocalDate.parse(dateStr);
                sqlDate = Date.valueOf(ld);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Date must be yyyy-MM-dd", "Invalid Date", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }

        List<Flight> results;
        if (origin.isEmpty() || destination.isEmpty() || sqlDate == null) {
            results = flightDAO.getAllFlights();
        } else {
            results = flightDAO.searchFlights(origin, destination, sqlDate);
        }

        populateFlights(results);
    }

    private void loadAllFlights() {
        List<Flight> all = flightDAO.getAllFlights();
        populateFlights(all);
    }

    private void populateFlights(List<Flight> flights) {
        flightsModel.setRowCount(0);
        for (Flight f : flights) {
            flightsModel.addRow(new Object[]{f.getFlightId(), f.getFlightNumber(), f.getOrigin(), f.getDestination(), f.getDepartureTime().toString(), String.format("$%.2f", f.getPrice()), f.getAvailableSeats()});
        }
    }

    private void onBook() {
        int sel = flightsTable.getSelectedRow();
        if (sel < 0) {
            JOptionPane.showMessageDialog(this, "Please select a flight to book", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (customerId <= 0) {
            JOptionPane.showMessageDialog(this, "Customer record not found. Cannot book.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int flightId = (int) flightsModel.getValueAt(sel, 0);
        String seat = "AUTO";
        int resId = reservationController.createReservation(customerId, flightId, seat);
        if (resId > 0) {
            JOptionPane.showMessageDialog(this, "Booking confirmed (Ref: " + resId + ")", "Booked", JOptionPane.INFORMATION_MESSAGE);
            loadAllFlights();
            refreshReservations();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to create booking. It may be full.", "Booking Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refreshReservations() {
        reservationsModel.setRowCount(0);
        if (customerId <= 0) return;
        List<Reservation> res = reservationController.getReservationsForCustomer(customerId);
        for (Reservation r : res) {
            reservationsModel.addRow(new Object[]{r.getReservationId(), r.getFlightId(), r.getSeatNumber(), r.getStatus().name(), r.getBookingDate()});
        }
    }

    private void onCancel() {
        int sel = reservationsTable.getSelectedRow();
        if (sel < 0) {
            JOptionPane.showMessageDialog(this, "Please select a reservation to cancel", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int resId = (int) reservationsModel.getValueAt(sel, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Cancel reservation " + resId + "?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;
        boolean ok = reservationController.cancelReservation(resId);
        if (ok) {
            JOptionPane.showMessageDialog(this, "Reservation cancelled", "Cancelled", JOptionPane.INFORMATION_MESSAGE);
            loadAllFlights();
            refreshReservations();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to cancel reservation", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

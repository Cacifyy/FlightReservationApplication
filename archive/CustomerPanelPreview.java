package presentation.views;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;

// Lightweight preview of the CustomerPanel UI using mock data
public class CustomerPanelPreview extends JPanel {
    private String username;
    private int customerId;

    private DefaultTableModel flightsModel;
    private DefaultTableModel reservationsModel;

    public CustomerPanelPreview(String username, int customerId) {
        this.username = username;
        this.customerId = customerId;
        initComponents();
        loadMockData();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JLabel header = new JLabel("Welcome, " + username);
        header.setFont(new Font("Arial", Font.BOLD, 18));
        header.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        add(header, BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();

        // Search tab (mocked)
        JPanel searchPanel = new JPanel(new BorderLayout());
        JPanel searchForm = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchForm.add(new JLabel("Origin:"));
        searchForm.add(new JTextField("Calgary", 8));
        searchForm.add(new JLabel("Destination:"));
        searchForm.add(new JTextField("Toronto", 8));
        searchForm.add(new JLabel("Date:"));
        searchForm.add(new JTextField("2025-12-01", 8));

        flightsModel = new DefaultTableModel(new Object[]{"ID","Flight#","Origin","Destination","Departure","Price","Seats"},0) {
            public boolean isCellEditable(int row, int column){ return false; }
        };
        JTable flightsTable = new JTable(flightsModel);
        JScrollPane flightsScroll = new JScrollPane(flightsTable);

        JPanel bookPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton bookBtn = new JButton("Book Selected Flight");
        bookBtn.setEnabled(false); // preview only
        bookPanel.add(bookBtn);

        searchPanel.add(searchForm, BorderLayout.NORTH);
        searchPanel.add(flightsScroll, BorderLayout.CENTER);
        searchPanel.add(bookPanel, BorderLayout.SOUTH);

        // Reservations tab (mocked)
        JPanel resPanel = new JPanel(new BorderLayout());
        reservationsModel = new DefaultTableModel(new Object[]{"Res ID","Flight ID","Seat","Status","Booked At"},0) {
            public boolean isCellEditable(int row, int column){ return false; }
        };
        JTable reservationsTable = new JTable(reservationsModel);
        JScrollPane resScroll = new JScrollPane(reservationsTable);
        JPanel resButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton cancelBtn = new JButton("Cancel Selected Reservation");
        cancelBtn.setEnabled(false);
        resButtons.add(cancelBtn);

        resPanel.add(resScroll, BorderLayout.CENTER);
        resPanel.add(resButtons, BorderLayout.SOUTH);

        tabs.addTab("Search Flights", searchPanel);
        tabs.addTab("My Reservations", resPanel);

        add(tabs, BorderLayout.CENTER);
    }

    private void loadMockData() {
        // add a few flight rows
        flightsModel.addRow(new Object[]{101, "AC101", "Calgary", "Toronto", "2025-12-01 08:00", "$450.00", 12});
        flightsModel.addRow(new Object[]{202, "WS202", "Calgary", "Vancouver", "2025-12-01 10:00", "$220.00", 5});

        // add a couple of reservations
        reservationsModel.addRow(new Object[]{1001, 101, "12A", "CONFIRMED", LocalDateTime.now().minusDays(2)});
        reservationsModel.addRow(new Object[]{1002, 202, "5B", "PENDING", LocalDateTime.now().minusDays(1)});
    }
}

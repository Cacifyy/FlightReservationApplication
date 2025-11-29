package data.dao;

import business.entities.booking.Reservation;
import data.DatabaseConnection;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// DAO for reservation operations
public class ReservationDAO {
    private Connection connection;

    public ReservationDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    // Create a reservation and decrement available seats
    public int createReservation(int customerId, int flightId, String seatNumber) {
        String insert = "INSERT INTO reservations (customer_id, flight_id, seat_number, status) VALUES (?, ?, ?, 'CONFIRMED')";
        String updateSeats = "UPDATE flights SET available_seats = available_seats - 1 WHERE flight_id = ? AND available_seats > 0";
        try {
            connection.setAutoCommit(false);

            try (PreparedStatement stmt = connection.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setInt(1, customerId);
                stmt.setInt(2, flightId);
                stmt.setString(3, seatNumber);
                int affected = stmt.executeUpdate();
                if (affected == 0) {
                    connection.rollback();
                    return -1;
                }

                try (PreparedStatement seatStmt = connection.prepareStatement(updateSeats)) {
                    seatStmt.setInt(1, flightId);
                    int seatsUpdated = seatStmt.executeUpdate();
                    if (seatsUpdated == 0) {
                        connection.rollback();
                        return -1;
                    }
                }

                ResultSet keys = stmt.getGeneratedKeys();
                int reservationId = -1;
                if (keys.next()) {
                    reservationId = keys.getInt(1);
                }
                connection.commit();
                return reservationId;
            }
        } catch (SQLException e) {
            try { connection.rollback(); } catch (SQLException ex) { /* ignore */ }
            System.err.println("Error creating reservation: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try { connection.setAutoCommit(true); } catch (SQLException ex) { /* ignore */ }
        }
        return -1;
    }

    // Get reservations for a customer
    public List<Reservation> getReservationsByCustomerId(int customerId) {
        List<Reservation> list = new ArrayList<>();
        String query = "SELECT * FROM reservations WHERE customer_id = ? ORDER BY booking_date DESC";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, customerId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Reservation r = new Reservation();
                r.setReservationId(rs.getInt("reservation_id"));
                r.setCustomerId(rs.getInt("customer_id"));
                r.setFlightId(rs.getInt("flight_id"));
                Timestamp ts = rs.getTimestamp("booking_date");
                if (ts != null) r.setBookingDate(ts.toLocalDateTime());
                String status = rs.getString("status");
                try {
                    r.setStatus(Reservation.ReservationStatus.valueOf(status));
                } catch (Exception ex) {
                    r.setStatus(Reservation.ReservationStatus.PENDING);
                }
                r.setSeatNumber(rs.getString("seat_number"));
                list.add(r);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching reservations: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    // Cancel a reservation and release a seat
    public boolean cancelReservation(int reservationId) {
        String getFlight = "SELECT flight_id, status FROM reservations WHERE reservation_id = ?";
        String updateRes = "UPDATE reservations SET status = 'CANCELLED' WHERE reservation_id = ?";
        String releaseSeat = "UPDATE flights SET available_seats = available_seats + 1 WHERE flight_id = ?";

        try {
            connection.setAutoCommit(false);

            int flightId = -1;
            try (PreparedStatement stmt = connection.prepareStatement(getFlight)) {
                stmt.setInt(1, reservationId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    String status = rs.getString("status");
                    if ("CANCELLED".equalsIgnoreCase(status)) {
                        connection.setAutoCommit(true);
                        return false;
                    }
                    flightId = rs.getInt("flight_id");
                } else {
                    connection.setAutoCommit(true);
                    return false;
                }
            }

            try (PreparedStatement stmt = connection.prepareStatement(updateRes)) {
                stmt.setInt(1, reservationId);
                int updated = stmt.executeUpdate();
                if (updated == 0) {
                    connection.rollback();
                    return false;
                }
            }

            try (PreparedStatement stmt = connection.prepareStatement(releaseSeat)) {
                stmt.setInt(1, flightId);
                stmt.executeUpdate();
            }

            connection.commit();
            return true;
        } catch (SQLException e) {
            try { connection.rollback(); } catch (SQLException ex) { /* ignore */ }
            System.err.println("Error cancelling reservation: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try { connection.setAutoCommit(true); } catch (SQLException ex) { /* ignore */ }
        }
        return false;
    }
}

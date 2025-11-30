/* File Name: ReservationDAO.java
 * Flight Reservation System - Term Project
 * Completed by: Ben Cacic, Rowan (Yi-Kai) Chen, Truman (Yao-Chu) Huang, Ryan Lau
 * Submittion Date: November 30, 2025
 */
package data.dao;

import business.entities.booking.Reservation;
import business.entities.booking.ReservationDetail;
import data.DatabaseConnection;
import java.sql.*;
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

    // Update reservation status and manage seats accordingly
    public boolean updateReservationStatus(int reservationId, String newStatus) {
        String getReservation = "SELECT flight_id, status FROM reservations WHERE reservation_id = ?";
        String updateRes = "UPDATE reservations SET status = ? WHERE reservation_id = ?";
        String releaseSeat = "UPDATE flights SET available_seats = available_seats + 1 WHERE flight_id = ?";
        String decrementSeat = "UPDATE flights SET available_seats = available_seats - 1 WHERE flight_id = ? AND available_seats > 0";

        try {
            connection.setAutoCommit(false);

            int flightId = -1;
            String oldStatus = null;
            try (PreparedStatement stmt = connection.prepareStatement(getReservation)) {
                stmt.setInt(1, reservationId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    oldStatus = rs.getString("status");
                    flightId = rs.getInt("flight_id");
                } else {
                    connection.setAutoCommit(true);
                    return false;
                }
            }

            // Update reservation status
            try (PreparedStatement stmt = connection.prepareStatement(updateRes)) {
                stmt.setString(1, newStatus);
                stmt.setInt(2, reservationId);
                int updated = stmt.executeUpdate();
                if (updated == 0) {
                    connection.rollback();
                    return false;
                }
            }

            // Manage seat availability based on status change
            // If changing from CONFIRMED to CANCELLED, release seat
            // If changing from CANCELLED to CONFIRMED, decrement seat
            if ("CONFIRMED".equalsIgnoreCase(oldStatus) && "CANCELLED".equalsIgnoreCase(newStatus)) {
                try (PreparedStatement stmt = connection.prepareStatement(releaseSeat)) {
                    stmt.setInt(1, flightId);
                    stmt.executeUpdate();
                }
            } else if ("CANCELLED".equalsIgnoreCase(oldStatus) && "CONFIRMED".equalsIgnoreCase(newStatus)) {
                try (PreparedStatement stmt = connection.prepareStatement(decrementSeat)) {
                    stmt.setInt(1, flightId);
                    int seatsUpdated = stmt.executeUpdate();
                    if (seatsUpdated == 0) {
                        connection.rollback();
                        return false; // No seats available
                    }
                }
            }

            connection.commit();
            return true;
        } catch (SQLException e) {
            try { connection.rollback(); } catch (SQLException ex) { /* ignore */ }
            System.err.println("Error updating reservation status: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try { connection.setAutoCommit(true); } catch (SQLException ex) { /* ignore */ }
        }
        return false;
    }

    // Cancel a reservation and release a seat
    public boolean cancelReservation(int reservationId) {
        return updateReservationStatus(reservationId, "CANCELLED");
    }

    // Get all reservations with customer and flight details (for agents)
    public List<ReservationDetail> getAllReservationsWithDetails() {
        List<ReservationDetail> list = new ArrayList<>();
        String query = "SELECT r.reservation_id, r.customer_id, r.flight_id, r.booking_date, r.status, r.seat_number, " +
                       "c.first_name, c.last_name, c.email, f.flight_number, f.origin, f.destination, f.departure_time " +
                       "FROM reservations r " +
                       "LEFT JOIN customers c ON r.customer_id = c.customer_id " +
                       "LEFT JOIN flights f ON r.flight_id = f.flight_id " +
                       "ORDER BY r.booking_date DESC";
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ReservationDetail rd = new ReservationDetail();
                rd.setReservationId(rs.getInt("reservation_id"));
                rd.setCustomerId(rs.getInt("customer_id"));
                rd.setFlightId(rs.getInt("flight_id"));
                
                Timestamp bookingTs = rs.getTimestamp("booking_date");
                if (bookingTs != null) rd.setBookingDate(bookingTs.toLocalDateTime());
                
                rd.setStatus(rs.getString("status"));
                rd.setSeatNumber(rs.getString("seat_number"));
                
                // Customer details
                rd.setCustomerFirstName(rs.getString("first_name"));
                rd.setCustomerLastName(rs.getString("last_name"));
                rd.setCustomerEmail(rs.getString("email"));
                
                // Flight details
                rd.setFlightNumber(rs.getString("flight_number"));
                rd.setOrigin(rs.getString("origin"));
                rd.setDestination(rs.getString("destination"));
                
                Timestamp depTs = rs.getTimestamp("departure_time");
                if (depTs != null) rd.setDepartureTime(depTs.toLocalDateTime());
                
                list.add(rd);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching all reservations: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }
}

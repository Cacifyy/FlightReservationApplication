/* File Name: PaymentDAO.java
 * Flight Reservation System - Term Project
 * Completed by: Ben Cacic, Rowan (Yi-Kai) Chen, Truman (Yao-Chu) Huang, Ryan Lau
 * Submittion Date: November 30, 2025
 */
package data.dao;

import business.entities.payment.Payment;
import business.entities.payment.Payment.PaymentMethod;
import business.entities.payment.Payment.PaymentStatus;
import data.DatabaseConnection;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Payment operations
 */
public class PaymentDAO {
    private Connection connection;

    public PaymentDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    /**
     * Create new payment
     */
    public boolean createPayment(Payment payment) {
        String query = "INSERT INTO payments (reservation_id, amount, payment_method, card_number, status) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, payment.getReservationId());
            stmt.setBigDecimal(2, payment.getAmount());
            stmt.setString(3, payment.getPaymentMethod().name());
            stmt.setString(4, payment.getCardNumber());
            stmt.setString(5, payment.getStatus().name());

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    payment.setPaymentId(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error creating payment: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Get payment by ID
     */
    public Payment getPaymentById(int paymentId) {
        String query = "SELECT * FROM payments WHERE payment_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, paymentId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return extractPaymentFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error getting payment by ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get payment by reservation ID
     */
    public Payment getPaymentByReservationId(int reservationId) {
        String query = "SELECT * FROM payments WHERE reservation_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, reservationId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return extractPaymentFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error getting payment by reservation ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get all payments
     */
    public List<Payment> getAllPayments() {
        List<Payment> payments = new ArrayList<>();
        String query = "SELECT * FROM payments ORDER BY payment_date DESC";

        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                payments.add(extractPaymentFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting all payments: " + e.getMessage());
            e.printStackTrace();
        }
        return payments;
    }

    /**
     * Get payments by customer ID (through reservations)
     */
    public List<Payment> getPaymentsByCustomerId(int customerId) {
        List<Payment> payments = new ArrayList<>();
        String query = "SELECT p.* FROM payments p " +
                "JOIN reservations r ON p.reservation_id = r.reservation_id " +
                "WHERE r.customer_id = ? ORDER BY p.payment_date DESC";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, customerId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                payments.add(extractPaymentFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting payments by customer: " + e.getMessage());
            e.printStackTrace();
        }
        return payments;
    }

    /**
     * Update payment
     */
    public boolean updatePayment(Payment payment) {
        String query = "UPDATE payments SET reservation_id = ?, amount = ?, payment_method = ?, card_number = ?, status = ? WHERE payment_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, payment.getReservationId());
            stmt.setBigDecimal(2, payment.getAmount());
            stmt.setString(3, payment.getPaymentMethod().name());
            stmt.setString(4, payment.getCardNumber());
            stmt.setString(5, payment.getStatus().name());
            stmt.setInt(6, payment.getPaymentId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating payment: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Update payment status
     */
    public boolean updatePaymentStatus(int paymentId, PaymentStatus status) {
        String query = "UPDATE payments SET status = ? WHERE payment_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, status.name());
            stmt.setInt(2, paymentId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating payment status: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Complete payment (set status to COMPLETED)
     */
    public boolean completePayment(int paymentId) {
        return updatePaymentStatus(paymentId, PaymentStatus.COMPLETED);
    }

    /**
     * Mark payment as failed
     */
    public boolean failPayment(int paymentId) {
        return updatePaymentStatus(paymentId, PaymentStatus.FAILED);
    }

    /**
     * Refund payment (set status to REFUNDED)
     */
    public boolean refundPayment(int paymentId) {
        return updatePaymentStatus(paymentId, PaymentStatus.REFUNDED);
    }

    /**
     * Delete payment
     */
    public boolean deletePayment(int paymentId) {
        String query = "DELETE FROM payments WHERE payment_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, paymentId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting payment: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Check if payment exists for reservation
     */
    public boolean hasPayment(int reservationId) {
        String query = "SELECT COUNT(*) FROM payments WHERE reservation_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, reservationId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error checking payment existence: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Get total revenue from completed payments
     */
    public BigDecimal getTotalRevenue() {
        String query = "SELECT SUM(amount) FROM payments WHERE status = 'COMPLETED'";

        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            if (rs.next()) {
                BigDecimal revenue = rs.getBigDecimal(1);
                return revenue != null ? revenue : BigDecimal.ZERO;
            }
        } catch (SQLException e) {
            System.err.println("Error getting total revenue: " + e.getMessage());
            e.printStackTrace();
        }
        return BigDecimal.ZERO;
    }

    /**
     * Get payments by status
     */
    public List<Payment> getPaymentsByStatus(PaymentStatus status) {
        List<Payment> payments = new ArrayList<>();
        String query = "SELECT * FROM payments WHERE status = ? ORDER BY payment_date DESC";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, status.name());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                payments.add(extractPaymentFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting payments by status: " + e.getMessage());
            e.printStackTrace();
        }
        return payments;
    }

    /**
     * Get payments by payment method
     */
    public List<Payment> getPaymentsByMethod(PaymentMethod paymentMethod) {
        List<Payment> payments = new ArrayList<>();
        String query = "SELECT * FROM payments WHERE payment_method = ? ORDER BY payment_date DESC";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, paymentMethod.name());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                payments.add(extractPaymentFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting payments by method: " + e.getMessage());
            e.printStackTrace();
        }
        return payments;
    }

    /**
     * Get payments within date range
     */
    public List<Payment> getPaymentsByDateRange(Timestamp startDate, Timestamp endDate) {
        List<Payment> payments = new ArrayList<>();
        String query = "SELECT * FROM payments WHERE payment_date BETWEEN ? AND ? ORDER BY payment_date DESC";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setTimestamp(1, startDate);
            stmt.setTimestamp(2, endDate);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                payments.add(extractPaymentFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting payments by date range: " + e.getMessage());
            e.printStackTrace();
        }
        return payments;
    }

    /**
     * Helper method to extract Payment object from ResultSet
     */
    private Payment extractPaymentFromResultSet(ResultSet rs) throws SQLException {
        Payment payment = new Payment();
        payment.setPaymentId(rs.getInt("payment_id"));
        payment.setReservationId(rs.getInt("reservation_id"));
        payment.setAmount(rs.getBigDecimal("amount"));
        payment.setPaymentDate(rs.getTimestamp("payment_date").toLocalDateTime());
        payment.setPaymentMethod(PaymentMethod.valueOf(rs.getString("payment_method")));
        payment.setCardNumber(rs.getString("card_number"));
        payment.setStatus(PaymentStatus.valueOf(rs.getString("status")));
        return payment;
    }
}
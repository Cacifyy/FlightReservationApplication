/* File Name: Reservation.java
 * Flight Reservation System - Term Project
 * Completed by: Ben Cacic, Rowan (Yi-Kai) Chen, Truman (Yao-Chu) Huang, Ryan Lau
 * Submittion Date: November 30, 2025
 */
package business.entities.booking;

import business.entities.user.Customer;
import business.entities.flight.Flight;
import business.entities.payment.Payment;
import java.time.LocalDateTime;
import java.math.BigDecimal;

/**
 * Reservation entity class representing a flight reservation in the system
 * Matches the reservations table in the database schema
 */
public class Reservation {
    
    public enum ReservationStatus {
        CONFIRMED, CANCELLED, PENDING
    }
    
    private int reservationId;
    private int customerId;
    private int flightId;
    private LocalDateTime bookingDate;
    private ReservationStatus status;
    private String seatNumber;
    
    // Related entities (for convenience)
    private Customer customer;
    private Flight flight;
    private Payment payment;
    
    // Default constructor
    public Reservation() {
        this.bookingDate = LocalDateTime.now();
        this.status = ReservationStatus.PENDING;
    }
    
    // Constructor for new reservation (without IDs)
    public Reservation(int customerId, int flightId, String seatNumber) {
        this();
        this.customerId = customerId;
        this.flightId = flightId;
        this.seatNumber = seatNumber;
    }
    
    // Constructor with entities
    public Reservation(Customer customer, Flight flight, String seatNumber) {
        this();
        this.customer = customer;
        this.flight = flight;
        if (customer != null) {
            this.customerId = customer.getCustomerId();
        }
        if (flight != null) {
            this.flightId = flight.getFlightId();
        }
        this.seatNumber = seatNumber;
    }
    
    // Full constructor (for database loading)
    public Reservation(int reservationId, int customerId, int flightId,
                      LocalDateTime bookingDate, ReservationStatus status, String seatNumber) {
        this.reservationId = reservationId;
        this.customerId = customerId;
        this.flightId = flightId;
        this.bookingDate = bookingDate;
        this.status = status;
        this.seatNumber = seatNumber;
    }
    
    // Complete constructor with entities
    public Reservation(int reservationId, int customerId, int flightId,
                      LocalDateTime bookingDate, ReservationStatus status, String seatNumber,
                      Customer customer, Flight flight, Payment payment) {
        this(reservationId, customerId, flightId, bookingDate, status, seatNumber);
        this.customer = customer;
        this.flight = flight;
        this.payment = payment;
    }
    
    // Getters and Setters
    public int getReservationId() {
        return reservationId;
    }
    
    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }
    
    public int getCustomerId() {
        return customerId;
    }
    
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }
    
    public int getFlightId() {
        return flightId;
    }
    
    public void setFlightId(int flightId) {
        this.flightId = flightId;
    }
    
    public LocalDateTime getBookingDate() {
        return bookingDate;
    }
    
    public void setBookingDate(LocalDateTime bookingDate) {
        this.bookingDate = bookingDate;
    }
    
    public ReservationStatus getStatus() {
        return status;
    }
    
    public void setStatus(ReservationStatus status) {
        this.status = status;
    }
    
    public String getSeatNumber() {
        return seatNumber;
    }
    
    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }
    
    public Customer getCustomer() {
        return customer;
    }
    
    public void setCustomer(Customer customer) {
        this.customer = customer;
        if (customer != null) {
            this.customerId = customer.getCustomerId();
        }
    }
    
    public Flight getFlight() {
        return flight;
    }
    
    public void setFlight(Flight flight) {
        this.flight = flight;
        if (flight != null) {
            this.flightId = flight.getFlightId();
        }
    }
    
    public Payment getPayment() {
        return payment;
    }
    
    public void setPayment(Payment payment) {
        this.payment = payment;
    }
    
    // Business methods
    public boolean isPending() {
        return status == ReservationStatus.PENDING;
    }
    
    public boolean isConfirmed() {
        return status == ReservationStatus.CONFIRMED;
    }
    
    public boolean isCancelled() {
        return status == ReservationStatus.CANCELLED;
    }
    
    public boolean canBeCancelled() {
        return status == ReservationStatus.CONFIRMED || status == ReservationStatus.PENDING;
    }
    
    public boolean canBeConfirmed() {
        return status == ReservationStatus.PENDING;
    }
    
    public void confirmReservation() {
        if (canBeConfirmed()) {
            this.status = ReservationStatus.CONFIRMED;
        }
    }
    
    public void cancelReservation() {
        if (canBeCancelled()) {
            this.status = ReservationStatus.CANCELLED;
            // Release the seat back to the flight
            if (flight != null) {
                flight.releaseSeat();
            }
        }
    }
    
    public BigDecimal getTotalAmount() {
        if (flight != null) {
            return BigDecimal.valueOf(flight.getPrice());
        }
        return BigDecimal.ZERO;
    }
    
    public boolean hasPayment() {
        return payment != null;
    }
    
    public boolean isPaymentCompleted() {
        return hasPayment() && payment.isCompleted();
    }
    
    public boolean requiresPayment() {
        return !hasPayment() || !payment.isCompleted();
    }
    
    public String getReservationReference() {
        return "RES" + String.format("%06d", reservationId);
    }
    
    public boolean isValidSeat() {
        return seatNumber != null && !seatNumber.trim().isEmpty();
    }
    
    public String getFlightDetails() {
        if (flight != null) {
            return flight.getFlightNumber() + " from " + flight.getOrigin() + 
                   " to " + flight.getDestination();
        }
        return "Flight ID: " + flightId;
    }
    
    public String getCustomerName() {
        if (customer != null) {
            return customer.getFullName();
        }
        return "Customer ID: " + customerId;
    }
    
    public boolean isUpcoming() {
        if (flight != null && flight.getDepartureTime() != null) {
            return flight.getDepartureTime().isAfter(LocalDateTime.now());
        }
        return false;
    }
    
    @Override
    public String toString() {
        return "Reservation{" +
                "reservationId=" + reservationId +
                ", customerId=" + customerId +
                ", flightId=" + flightId +
                ", bookingDate=" + bookingDate +
                ", status=" + status +
                ", seatNumber='" + seatNumber + '\'' +
                ", flightDetails='" + getFlightDetails() + '\'' +
                ", customerName='" + getCustomerName() + '\'' +
                '}';
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Reservation reservation = (Reservation) obj;
        return reservationId == reservation.reservationId;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(reservationId);
    }
}
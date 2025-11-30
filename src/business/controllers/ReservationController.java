/* File Name: ReservationController.java
 * Flight Reservation System - Term Project
 * Completed by: Ben Cacic, Rowan (Yi-Kai) Chen, Truman (Yao-Chu) Huang, Ryan Lau
 * Submittion Date: November 30, 2025
 */
package business.controllers;

import data.dao.ReservationDAO;
import business.entities.booking.Reservation;
import business.entities.booking.ReservationDetail;
import java.util.List;

// Controller to mediate reservation actions
public class ReservationController {
    private ReservationDAO reservationDAO;

    public ReservationController() {
        this.reservationDAO = new ReservationDAO();
    }

    public int createReservation(int customerId, int flightId, String seatNumber) {
        return reservationDAO.createReservation(customerId, flightId, seatNumber);
    }

    public List<Reservation> getReservationsForCustomer(int customerId) {
        return reservationDAO.getReservationsByCustomerId(customerId);
    }

    public boolean cancelReservation(int reservationId) {
        return reservationDAO.cancelReservation(reservationId);
    }

    public boolean updateReservationStatus(int reservationId, String newStatus) {
        return reservationDAO.updateReservationStatus(reservationId, newStatus);
    }

    public List<ReservationDetail> getAllReservationsWithDetails() {
        return reservationDAO.getAllReservationsWithDetails();
    }
}

package business.controllers;

import data.dao.ReservationDAO;
import business.entities.booking.Reservation;
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
}

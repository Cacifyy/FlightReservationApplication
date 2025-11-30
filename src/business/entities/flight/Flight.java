/* File Name: Flight.java
 * Flight Reservation System - Term Project
 * Completed by: Ben Cacic, Rowan (Yi-Kai) Chen, Truman (Yao-Chu) Huang, Ryan Lau
 * Submittion Date: November 30, 2025
 */
package business.entities.flight;

import java.time.LocalDateTime;
import java.time.Duration;

// Flight entity class
public class Flight {
    private int flightId;
    private String flightNumber;
    private int aircraftId;
    private String origin;
    private String destination;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private double price;
    private int availableSeats;
    private String status; // SCHEDULED, DELAYED, CANCELLED, COMPLETED

    // Constructors
    public Flight() {
    }

    public Flight(String flightNumber, String origin, String destination,
            LocalDateTime departureTime, LocalDateTime arrivalTime,
            double price, int availableSeats) {
        this.flightNumber = flightNumber;
        this.origin = origin;
        this.destination = destination;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.price = price;
        this.availableSeats = availableSeats;
        this.status = "SCHEDULED";
    }

    // Getters and Setters
    public int getFlightId() {
        return flightId;
    }

    public void setFlightId(int flightId) {
        this.flightId = flightId;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public int getAircraftId() {
        return aircraftId;
    }

    public void setAircraftId(int aircraftId) {
        this.aircraftId = aircraftId;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
    }

    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(LocalDateTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Business methods
    public boolean hasAvailableSeats() {
        return availableSeats > 0;
    }

    public boolean bookSeat() {
        if (hasAvailableSeats()) {
            availableSeats--;
            return true;
        }
        return false;
    }

    public void releaseSeat() {
        availableSeats++;
    }

    public long getFlightDuration() {
        if (departureTime != null && arrivalTime != null) {
            Duration duration = Duration.between(departureTime, arrivalTime);
            return duration.toMinutes();
        }
        return 0;
    }

    public boolean isScheduled() {
        return "SCHEDULED".equals(status);
    }

    public boolean isCancelled() {
        return "CANCELLED".equals(status);
    }

    @Override
    public String toString() {
        return "Flight{" +
                "flightNumber='" + flightNumber + '\'' +
                ", origin='" + origin + '\'' +
                ", destination='" + destination + '\'' +
                ", departureTime=" + departureTime +
                ", price=" + price +
                ", availableSeats=" + availableSeats +
                ", status='" + status + '\'' +
                '}';
    }
}
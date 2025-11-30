/* File Name: Aircraft.java
 * Flight Reservation System - Term Project
 * Completed by: Ben Cacic, Rowan (Yi-Kai) Chen, Truman (Yao-Chu) Huang, Ryan Lau
 * Submittion Date: November 30, 2025
 */
package business.entities.flight;

// Aircraft entity class
public class Aircraft {
    private int aircraftId;
    private String model;
    private int capacity;
    private String airline;

    // Constructors
    public Aircraft() {
    }

    public Aircraft(String model, int capacity, String airline) {
        this.model = model;
        this.capacity = capacity;
        this.airline = airline;
    }

    public Aircraft(int aircraftId, String model, int capacity, String airline) {
        this.aircraftId = aircraftId;
        this.model = model;
        this.capacity = capacity;
        this.airline = airline;
    }

    // Getters and Setters
    public int getAircraftId() {
        return aircraftId;
    }

    public void setAircraftId(int aircraftId) {
        this.aircraftId = aircraftId;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getAirline() {
        return airline;
    }

    public void setAirline(String airline) {
        this.airline = airline;
    }

    // Business methods
    public boolean isValidCapacity() {
        return capacity > 0;
    }

    public boolean canAccommodatePassengers(int passengerCount) {
        return passengerCount <= capacity && passengerCount > 0;
    }

    public String getAircraftInfo() {
        return airline + " " + model + " (Capacity: " + capacity + ")";
    }

    public boolean isWideBody() {
        // Wide-body aircraft typically have capacity > 200
        return capacity > 200;
    }

    public boolean isNarrowBody() {
        // Narrow-body aircraft typically have capacity <= 200
        return capacity <= 200;
    }

    @Override
    public String toString() {
        return "Aircraft{" +
                "aircraftId=" + aircraftId +
                ", model='" + model + '\'' +
                ", capacity=" + capacity +
                ", airline='" + airline + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Aircraft aircraft = (Aircraft) obj;
        return aircraftId == aircraft.aircraftId;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(aircraftId);
    }
}

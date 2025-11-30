package business.controllers;

import data.dao.AircraftDAO;
import data.dao.FlightDAO;
import business.entities.flight.Aircraft;
import business.entities.flight.Flight;

import java.util.List;

public class AdminController {
    private AircraftDAO aircraftDAO;
    private FlightDAO flightDAO;

    public AdminController() {
        this.aircraftDAO = new AircraftDAO();
        this.flightDAO = new FlightDAO();
    }

    // Aircraft operations
    public List<Aircraft> getAllAircraft() { return aircraftDAO.getAllAircraft(); }
    public int createAircraft(String model, int capacity, String airline) { return aircraftDAO.createAircraft(model, capacity, airline); }
    public boolean updateAircraft(int id, String model, int capacity, String airline) { return aircraftDAO.updateAircraft(id, model, capacity, airline); }
    public boolean deleteAircraft(int id) { return aircraftDAO.deleteAircraft(id); }

    // Flight operations (FlightDAO already supports these)
    public List<Flight> getAllFlights() { return flightDAO.getAllFlights(); }
    public Flight getFlightById(int id) { return flightDAO.getFlightById(id); }
    public boolean addFlight(Flight f) { return flightDAO.addFlight(f); }
    public boolean updateFlight(Flight f) { return flightDAO.updateFlight(f); }
    public boolean deleteFlight(int id) { return flightDAO.deleteFlight(id); }
}

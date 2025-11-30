package data.dao;

import business.entities.flight.Flight;
import data.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// Data Access Object for Flight operations
public class FlightDAO {
    private Connection connection;

    public FlightDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    // Search flights by origin, destination, and date
    public List<Flight> searchFlights(String origin, String destination, Date date) {
        List<Flight> flights = new ArrayList<>();
        String query = "SELECT * FROM flights WHERE origin = ? AND destination = ? AND DATE(departure_time) = ? AND status = 'SCHEDULED'";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, origin);
            stmt.setString(2, destination);
            stmt.setDate(3, date);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                flights.add(extractFlightFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error searching flights: " + e.getMessage());
            e.printStackTrace();
        }

        return flights;
    }

    // Flexible search: any combination of origin, destination, and date (null/empty means ignored)
    public List<Flight> searchFlightsFlexible(String origin, String destination, Date date) {
        List<Flight> flights = new ArrayList<>();
        StringBuilder sb = new StringBuilder("SELECT * FROM flights WHERE status = 'SCHEDULED'");
        List<Object> params = new ArrayList<>();

        if (origin != null && !origin.trim().isEmpty()) {
            sb.append(" AND origin = ?");
            params.add(origin.trim());
        }
        if (destination != null && !destination.trim().isEmpty()) {
            sb.append(" AND destination = ?");
            params.add(destination.trim());
        }
        if (date != null) {
            sb.append(" AND DATE(departure_time) = ?");
            params.add(date);
        }

        sb.append(" ORDER BY departure_time");

        try (PreparedStatement stmt = connection.prepareStatement(sb.toString())) {
            for (int i = 0; i < params.size(); i++) {
                Object p = params.get(i);
                if (p instanceof Date) stmt.setDate(i + 1, (Date) p);
                else stmt.setString(i + 1, p.toString());
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                flights.add(extractFlightFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error searching flights (flexible): " + e.getMessage());
            e.printStackTrace();
        }

        return flights;
    }

    // Get all flights
    public List<Flight> getAllFlights() {
        List<Flight> flights = new ArrayList<>();
        String query = "SELECT * FROM flights ORDER BY departure_time";

        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                flights.add(extractFlightFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting all flights: " + e.getMessage());
            e.printStackTrace();
        }

        return flights;
    }

    // Get flight by ID
    public Flight getFlightById(int flightId) {
        String query = "SELECT * FROM flights WHERE flight_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, flightId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return extractFlightFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error getting flight by ID: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    // Add new flight
    public boolean addFlight(Flight flight) {
        String query = "INSERT INTO flights (flight_number, aircraft_id, origin, destination, departure_time, arrival_time, price, available_seats, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, flight.getFlightNumber());
            stmt.setInt(2, flight.getAircraftId());
            stmt.setString(3, flight.getOrigin());
            stmt.setString(4, flight.getDestination());
            stmt.setTimestamp(5, Timestamp.valueOf(flight.getDepartureTime()));
            stmt.setTimestamp(6, Timestamp.valueOf(flight.getArrivalTime()));
            stmt.setDouble(7, flight.getPrice());
            stmt.setInt(8, flight.getAvailableSeats());
            stmt.setString(9, flight.getStatus());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error adding flight: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Update flight
    public boolean updateFlight(Flight flight) {
        String query = "UPDATE flights SET flight_number = ?, aircraft_id = ?, origin = ?, destination = ?, departure_time = ?, arrival_time = ?, price = ?, available_seats = ?, status = ? WHERE flight_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, flight.getFlightNumber());
            stmt.setInt(2, flight.getAircraftId());
            stmt.setString(3, flight.getOrigin());
            stmt.setString(4, flight.getDestination());
            stmt.setTimestamp(5, Timestamp.valueOf(flight.getDepartureTime()));
            stmt.setTimestamp(6, Timestamp.valueOf(flight.getArrivalTime()));
            stmt.setDouble(7, flight.getPrice());
            stmt.setInt(8, flight.getAvailableSeats());
            stmt.setString(9, flight.getStatus());
            stmt.setInt(10, flight.getFlightId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating flight: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Delete flight
    public boolean deleteFlight(int flightId) {
        String query = "DELETE FROM flights WHERE flight_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, flightId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting flight: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Update available seats
    public boolean updateAvailableSeats(int flightId, int seats) {
        String query = "UPDATE flights SET available_seats = ? WHERE flight_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, seats);
            stmt.setInt(2, flightId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating seats: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Helper method to extract Flight object from ResultSet
    private Flight extractFlightFromResultSet(ResultSet rs) throws SQLException {
        Flight flight = new Flight();
        flight.setFlightId(rs.getInt("flight_id"));
        flight.setFlightNumber(rs.getString("flight_number"));
        flight.setAircraftId(rs.getInt("aircraft_id"));
        flight.setOrigin(rs.getString("origin"));
        flight.setDestination(rs.getString("destination"));
        flight.setDepartureTime(rs.getTimestamp("departure_time").toLocalDateTime());
        flight.setArrivalTime(rs.getTimestamp("arrival_time").toLocalDateTime());
        flight.setPrice(rs.getDouble("price"));
        flight.setAvailableSeats(rs.getInt("available_seats"));
        flight.setStatus(rs.getString("status"));
        return flight;
    }
}
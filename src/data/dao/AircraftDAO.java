package data.dao;

import data.DatabaseConnection;
import business.entities.flight.Aircraft;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AircraftDAO {
    private Connection connection;

    public AircraftDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    public List<Aircraft> getAllAircraft() {
        List<Aircraft> list = new ArrayList<>();
        String query = "SELECT * FROM aircraft ORDER BY aircraft_id";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Aircraft a = new Aircraft();
                a.setAircraftId(rs.getInt("aircraft_id"));
                a.setModel(rs.getString("model"));
                a.setCapacity(rs.getInt("capacity"));
                a.setAirline(rs.getString("airline"));
                list.add(a);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching aircraft: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    public int createAircraft(String model, int capacity, String airline) {
        String query = "INSERT INTO aircraft (model, capacity, airline) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, model);
            stmt.setInt(2, capacity);
            stmt.setString(3, airline);
            int affected = stmt.executeUpdate();
            if (affected == 0) return -1;
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) return keys.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error creating aircraft: " + e.getMessage());
            e.printStackTrace();
        }
        return -1;
    }

    public boolean updateAircraft(int aircraftId, String model, int capacity, String airline) {
        String query = "UPDATE aircraft SET model = ?, capacity = ?, airline = ? WHERE aircraft_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, model);
            stmt.setInt(2, capacity);
            stmt.setString(3, airline);
            stmt.setInt(4, aircraftId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating aircraft: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteAircraft(int aircraftId) {
        String query = "DELETE FROM aircraft WHERE aircraft_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, aircraftId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting aircraft: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}

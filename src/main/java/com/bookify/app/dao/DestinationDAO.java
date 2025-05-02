package com.bookify.app.dao;

import com.bookify.app.database.DatabaseConnection;
import com.bookify.app.model.Destination;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DestinationDAO {
    
    public List<Destination> getAllDestinations() {
        List<Destination> destinations = new ArrayList<>();
        String query = "SELECT * FROM destinations";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                Destination destination = new Destination();
                destination.setId(rs.getInt("id"));
                destination.setName(rs.getString("name"));
                destination.setCountry(rs.getString("country"));
                destination.setDescription(rs.getString("description"));
                destination.setPricePerPerson(rs.getDouble("price_per_person"));
                destinations.add(destination);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving destinations: " + e.getMessage());
            e.printStackTrace();
        }
        
        return destinations;
    }
    
    public Destination getDestinationById(int id) {
        String query = "SELECT * FROM destinations WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Destination destination = new Destination();
                destination.setId(rs.getInt("id"));
                destination.setName(rs.getString("name"));
                destination.setCountry(rs.getString("country"));
                destination.setDescription(rs.getString("description"));
                destination.setPricePerPerson(rs.getDouble("price_per_person"));
                return destination;
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving destination: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    public boolean addDestination(Destination destination) {
        String query = "INSERT INTO destinations (name, country, description, price_per_person) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, destination.getName());
            pstmt.setString(2, destination.getCountry());
            pstmt.setString(3, destination.getDescription());
            pstmt.setDouble(4, destination.getPricePerPerson());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        destination.setId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error adding destination: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    public boolean updateDestination(Destination destination) {
        String query = "UPDATE destinations SET name = ?, country = ?, description = ?, price_per_person = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, destination.getName());
            pstmt.setString(2, destination.getCountry());
            pstmt.setString(3, destination.getDescription());
            pstmt.setDouble(4, destination.getPricePerPerson());
            pstmt.setInt(5, destination.getId());
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error updating destination: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    public boolean deleteDestination(int id) {
        String query = "DELETE FROM destinations WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, id);
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting destination: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
} 
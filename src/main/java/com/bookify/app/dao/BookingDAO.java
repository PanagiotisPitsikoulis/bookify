package com.bookify.app.dao;

import com.bookify.app.database.DatabaseConnection;
import com.bookify.app.model.Booking;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookingDAO {
    
    public List<Booking> getAllBookings() {
        List<Booking> bookings = new ArrayList<>();
        String query = "SELECT b.*, c.name as customer_name, d.name as destination_name " +
                      "FROM bookings b " +
                      "JOIN customers c ON b.customer_id = c.id " +
                      "JOIN destinations d ON b.destination_id = d.id";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                Booking booking = extractBookingFromResultSet(rs);
                bookings.add(booking);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving bookings: " + e.getMessage());
            e.printStackTrace();
        }
        
        return bookings;
    }
    
    public List<Booking> getBookingsByCustomerId(int customerId) {
        List<Booking> bookings = new ArrayList<>();
        String query = "SELECT b.*, c.name as customer_name, d.name as destination_name " +
                      "FROM bookings b " +
                      "JOIN customers c ON b.customer_id = c.id " +
                      "JOIN destinations d ON b.destination_id = d.id " +
                      "WHERE b.customer_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, customerId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Booking booking = extractBookingFromResultSet(rs);
                bookings.add(booking);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving bookings for customer: " + e.getMessage());
            e.printStackTrace();
        }
        
        return bookings;
    }
    
    public Booking getBookingById(int id) {
        String query = "SELECT b.*, c.name as customer_name, d.name as destination_name " +
                      "FROM bookings b " +
                      "JOIN customers c ON b.customer_id = c.id " +
                      "JOIN destinations d ON b.destination_id = d.id " +
                      "WHERE b.id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return extractBookingFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving booking: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    private Booking extractBookingFromResultSet(ResultSet rs) throws SQLException {
        Booking booking = new Booking();
        booking.setId(rs.getInt("id"));
        booking.setCustomerId(rs.getInt("customer_id"));
        booking.setDestinationId(rs.getInt("destination_id"));
        booking.setBookingDate(rs.getString("booking_date"));
        booking.setTravelDate(rs.getString("travel_date"));
        booking.setNumberOfPeople(rs.getInt("number_of_people"));
        booking.setTotalPrice(rs.getDouble("total_price"));
        booking.setStatus(rs.getString("status"));
        booking.setCustomerName(rs.getString("customer_name"));
        booking.setDestinationName(rs.getString("destination_name"));
        return booking;
    }
    
    public boolean addBooking(Booking booking) {
        String query = "INSERT INTO bookings (customer_id, destination_id, booking_date, travel_date, " +
                      "number_of_people, total_price, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, booking.getCustomerId());
            pstmt.setInt(2, booking.getDestinationId());
            pstmt.setString(3, booking.getBookingDate());
            pstmt.setString(4, booking.getTravelDate());
            pstmt.setInt(5, booking.getNumberOfPeople());
            pstmt.setDouble(6, booking.getTotalPrice());
            pstmt.setString(7, booking.getStatus());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        booking.setId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error adding booking: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    public boolean updateBooking(Booking booking) {
        String query = "UPDATE bookings SET customer_id = ?, destination_id = ?, booking_date = ?, " +
                      "travel_date = ?, number_of_people = ?, total_price = ?, status = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, booking.getCustomerId());
            pstmt.setInt(2, booking.getDestinationId());
            pstmt.setString(3, booking.getBookingDate());
            pstmt.setString(4, booking.getTravelDate());
            pstmt.setInt(5, booking.getNumberOfPeople());
            pstmt.setDouble(6, booking.getTotalPrice());
            pstmt.setString(7, booking.getStatus());
            pstmt.setInt(8, booking.getId());
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error updating booking: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    public boolean deleteBooking(int id) {
        String query = "DELETE FROM bookings WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, id);
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting booking: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
} 
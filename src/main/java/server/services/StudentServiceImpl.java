package server.services;

import server.database.DatabaseConnection;
import shared.classes.*;
import shared.interfaces.StudentService;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentServiceImpl implements Remote, StudentService, Serializable {
    private static final long serialVersionUID = 1L; // Add a serialVersionUID
    @Override
    public Booking createBooking(int studentID, int sessionID, String sessionMode, String bookingStatus,
                                 double sessionPrice) throws RemoteException {
        Booking newBooking = new Booking(studentID, sessionID, sessionMode, bookingStatus, sessionPrice);
        try (Connection conn = DatabaseConnection.setCon();
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO bookings (studentID, sessionID, sessionMode, bookingStatus, sessionPrice) VALUES (?, ?, ?, ?, ?)"
             )) {

            // Set parameters for the booking
            stmt.setInt(1, studentID);
            stmt.setInt(2, sessionID);
            stmt.setString(3, sessionMode);
            stmt.setString(4, bookingStatus);
            stmt.setDouble(5, sessionPrice);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RemoteException("Database error: " + e.getMessage());
        }
        return newBooking;
    }

    @Override
    public Booking viewStudentBooking(int studentID) throws RemoteException {
        Booking booking = null;
        try (Connection conn = DatabaseConnection.setCon();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT * FROM bookings WHERE studentID = ?"
             )) {

            stmt.setInt(1, studentID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                booking = new Booking(
                        rs.getInt("studentID"),
                        rs.getInt("sessionID"),
                        rs.getString("sessionMode"),
                        rs.getString("bookingStatus"),
                        rs.getDouble("sessionPrice")
                );
            }

        } catch (SQLException e) {
            throw new RemoteException("Database error while viewing booking: " + e.getMessage());
        }
        return booking;
    }

    @Override
    public Booking modifyBooking(int studentID, int sessionID, String newSessionMode,
                                 String newBookingStatus, double newSessionPrice) throws RemoteException {
        Booking updatedBooking = null;
        try (Connection conn = DatabaseConnection.setCon();
             PreparedStatement stmt = conn.prepareStatement(
                     "UPDATE bookings SET sessionMode = ?, bookingStatus = ?, sessionPrice = ? " +
                             "WHERE studentID = ? AND sessionID = ?"
             )) {

            // Set parameters for the update
            stmt.setString(1, newSessionMode);
            stmt.setString(2, newBookingStatus);
            stmt.setDouble(3, newSessionPrice);
            stmt.setInt(4, studentID);
            stmt.setInt(5, sessionID);

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                updatedBooking = new Booking(
                        studentID,
                        sessionID,
                        newSessionMode,
                        newBookingStatus,
                        newSessionPrice
                );
            }

        } catch (SQLException e) {
            throw new RemoteException("Database error while modifying booking: " + e.getMessage());
        }
        return updatedBooking;
    }

    @Override
    public Subject viewSubject() {
        return null;
    }

    @Override
    public LessonPlan viewLessonPlan() {
        return null;
    }

    @Override
    public Payment viewPaymentHistory() {
        return null;
    }

    @Override
    public Student viewStudentBalance() {
        return null;
    }

    @Override
    public Payment createPayment() {return null;}
}

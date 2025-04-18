package server.services;

import server.database.DatabaseConnection;
import shared.classes.*;
import shared.interfaces.StudentService;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class StudentServiceImpl implements Remote, StudentService, Serializable {
    private static final long serialVersionUID = 1L; // Add a serialVersionUID
    @Override
    public Booking createBooking(int studentID, String sessionID, String sessionMode, String bookingStatus,
                                 double sessionPrice) throws RemoteException {
        // Validate sessionMode against allowed values
        if (!isValidSessionMode(sessionMode)) {
            throw new RemoteException("Invalid session mode. Must be 'Face-to-Face' or 'Online'");
        }

        try (Connection conn = DatabaseConnection.setCon();
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO booking (studentID, sessionID, sessionMode, sessionPrice, bookingStatus) " +
                             "VALUES (?, ?, ?, ?, ?)")) {

            // Set parameters with proper types
            stmt.setInt(1, studentID);          // Numeric student ID
            stmt.setString(2, sessionID);       // Alphanumeric session ID
            stmt.setString(3, sessionMode);     // Validated enum value
            stmt.setString(5, bookingStatus);   // Status string
            stmt.setDouble(4, sessionPrice);    // Price

            stmt.executeUpdate();
            return new Booking(studentID, sessionID, sessionMode, bookingStatus, sessionPrice);

        } catch (SQLException e) {
            throw new RemoteException("Database error: " + e.getMessage());
        }
    }

    private boolean isValidSessionMode(String mode) {
        return "Face-to-Face".equals(mode) || "Online".equals(mode);
    }

    @Override
    public Booking viewStudentBooking(int studentID) throws RemoteException {
        Booking booking = null;
        try (Connection conn = DatabaseConnection.setCon();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT * FROM booking WHERE studentID = ?"
             )) {

            stmt.setInt(1, studentID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                booking = new Booking(
                        rs.getInt("studentID"),
                        rs.getString("sessionID"),
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
    public List<TutorSession> viewAvailableSessions() throws RemoteException {
        List<TutorSession> sessions = new ArrayList<>();
        String query = "SELECT \n" +
                "    ts.sessionID, \n" +
                "    ts.tutorID, \n" +
                "            ts.subjectID, \n" +
                "            ts.sessionStatus, \n" +
                "            ts.sessionDate, \n" +
                "            ts.sessionTime, \n" +
                "            ts.sessionDuration, \n" +
                "            ts.numberOfStudents, \n" +
                "            ts.maximumStudents, \n" +
                "            ts.sessionPrice, \n" +
                "            ts.sessionMode, \n" +
                "            s.subjectLevel, \n" +
                "            s.subjectName \n" +
                "            FROM tutorsession ts \n" +
                "            JOIN subject s ON ts.subjectID = s.subjectID \n" +
                "            WHERE ts.sessionStatus IN ('In Progress', 'Scheduled')";

        try (Connection conn = DatabaseConnection.setCon();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                TutorSession session = new TutorSession(
                        rs.getString("sessionID"),
                        rs.getInt("tutorID"),
                        rs.getString("subjectID"),
                        rs.getString("subjectName"),
                        rs.getString("sessionDate"),
                        rs.getString("sessionTime"),
                        rs.getInt("sessionDuration"),
                        rs.getString("sessionStatus"),
                        rs.getInt("numberOfStudents"),
                        rs.getInt("maximumStudents"),
                        rs.getDouble("sessionPrice"),
                        rs.getString("sessionMode")
                );

                // Store subjectLevel in the session object (you'll need to add this field)
                session.setSubjectLevel(rs.getString("subjectLevel"));
                sessions.add(session);
            }
            return sessions;
        } catch (SQLException e) {
            throw new RemoteException("Database error: " + e.getMessage());
        }
    }

    @Override
    public Booking modifyBooking(int studentID, String sessionID, String newSessionMode,
                                 String newBookingStatus, double newSessionPrice) throws RemoteException {
        Booking updatedBooking = null;
        try (Connection conn = DatabaseConnection.setCon();
             PreparedStatement stmt = conn.prepareStatement(
                     "UPDATE booking SET sessionMode = ?, bookingStatus = ?, sessionPrice = ? " +
                             "WHERE studentID = ? AND sessionID = ?"
             )) {

            // set parameters for the update
            stmt.setString(1, newSessionMode);
            stmt.setString(2, newBookingStatus);
            stmt.setDouble(3, newSessionPrice);
            stmt.setInt(4, studentID);
            stmt.setString(5, sessionID);

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                updatedBooking = new Booking(studentID, sessionID, newSessionMode, newBookingStatus, newSessionPrice);
            }

        } catch (SQLException e) {
            throw new RemoteException("Database error while modifying booking: " + e.getMessage());
        }
        return updatedBooking;
    }

    @Override
    public List<Subject> viewSubject() throws RemoteException {
        System.out.println("[SERVER] Fetching subjects from database...");
        List<Subject> subjects = new ArrayList<>();
        try (Connection conn = DatabaseConnection.setCon();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT * FROM subject"
             )) {

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Subject subject = new Subject(
                        rs.getString("subjectID"),
                        rs.getString("subjectName"),
                        rs.getString("subjectDescription"),
                        rs.getString("subjectLevel")
                );
                subjects.add(subject);
                System.out.println("[SERVER] Fetched subject: " + subject);
            }

            if (subjects.isEmpty()) {
                System.out.println("[SERVER] No subjects found in the database.");
            }

        } catch (SQLException e) {
            throw new RemoteException("Database error while fetching subjects: " + e.getMessage());
        }
        return subjects;
    }

    @Override
    public List<LessonPlan> viewHighSchoolLessonPlan() throws RemoteException {
        System.out.println("[SERVER] Fetching high school lesson plans from database...");
        List<LessonPlan> lessonPlans = new ArrayList<>();
        try (Connection conn = DatabaseConnection.setCon();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT * FROM lessonplan WHERE subjectID LIKE 'HS%'"
             )) {

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                LessonPlan lessonPlan = new LessonPlan(
                        rs.getString("lessonPlanID"),
                        rs.getString("subjectID"),
                        rs.getString("objectives"),
                        rs.getString("topicsCovered")
                );
                lessonPlans.add(lessonPlan);
                System.out.println("[SERVER] Fetched high school lesson plan: " + lessonPlan);
            }

            if (lessonPlans.isEmpty()) {
                System.out.println("[SERVER] No high school lesson plans found in the database.");
            }

        } catch (SQLException e) {
            throw new RemoteException("Database error while fetching high school lesson plans: " + e.getMessage());
        }
        return lessonPlans;
    }

    @Override
    public List<LessonPlan> viewCollegeLessonPlan() throws RemoteException {
        System.out.println("[SERVER] Fetching college lesson plans from database...");
        List<LessonPlan> lessonPlans = new ArrayList<>();
        try (Connection conn = DatabaseConnection.setCon();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT * FROM lessonplan WHERE subjectID LIKE 'IT%'"
             )) {

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                LessonPlan lessonPlan = new LessonPlan(
                        rs.getString("lessonPlanID"),
                        rs.getString("subjectID"),
                        rs.getString("objectives"),
                        rs.getString("topicsCovered")
                );
                lessonPlans.add(lessonPlan);
                System.out.println("[SERVER] Fetched college lesson plan: " + lessonPlan);
            }

            if (lessonPlans.isEmpty()) {
                System.out.println("[SERVER] No college lesson plans found in the database.");
            }

        } catch (SQLException e) {
            throw new RemoteException("Database error while fetching college lesson plans: " + e.getMessage());
        }
        return lessonPlans;
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
    public Payment createPayment(String studentID, double amount, String paymentMethod) throws RemoteException {
        // first we get the latest payment ID from database
        String lastPaymentID = getLastPaymentID();
        String newPaymentID = incrementPaymentID(lastPaymentID);

        LocalDateTime paymentDateTime = LocalDateTime.now();
        String paymentDate = paymentDateTime.toLocalDate().toString();
        String paymentTime = paymentDateTime.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm:ss"));

        Payment newPayment = new Payment(newPaymentID, studentID, amount, paymentDateTime, paymentMethod);

        try (Connection conn = DatabaseConnection.setCon();
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO payments (paymentID, studentID, amount, paymentDate, paymentTime, paymentMethod) " +
                             "VALUES (?, ?, ?, ?, ?, ?)"
             )) {

            stmt.setString(1, newPaymentID);
            stmt.setString(2, studentID);
            stmt.setDouble(3, amount);
            stmt.setString(4, paymentDate);
            stmt.setString(5, paymentTime);
            stmt.setString(6, paymentMethod);

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RemoteException("Database error while creating payment: " + e.getMessage());
        }

        return newPayment;
    }

    private String getLastPaymentID() throws RemoteException {
        try (Connection conn = DatabaseConnection.setCon();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT paymentID FROM payments ORDER BY paymentID DESC LIMIT 1")) {

            if (rs.next()) {
                return rs.getString("paymentID");
            }
            return "P000"; // default starting value if no payments exist

        } catch (SQLException e) {
            throw new RemoteException("Failed to get last payment ID: " + e.getMessage());
        }
    }

    private String incrementPaymentID(String lastPaymentID) {
        // extractor for number parts
        String prefix = lastPaymentID.substring(0, 1); // "P"
        int number = Integer.parseInt(lastPaymentID.substring(1)); // "001" -> 1

        // incerment and format back to three digits
        number++;
        return prefix + String.format("%03d", number);
    }
}

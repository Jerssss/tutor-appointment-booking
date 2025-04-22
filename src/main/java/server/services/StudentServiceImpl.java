package server.services;

import server.database.DatabaseConnection;
import shared.classes.*;
import shared.interfaces.StudentService;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
    public List<BookingDetails> viewStudentBooking(int studentID) throws RemoteException {
        System.out.println("[SERVER] Executing query for studentID: " + studentID);
        List<BookingDetails> bookings = new ArrayList<>();
        try (Connection conn = DatabaseConnection.setCon();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT b.studentID, b.sessionID, b.sessionMode, " +
                             "b.bookingStatus, b.sessionPrice, ts.sessionDate, " +
                             "ts.sessionTime, ts.sessionDuration, s.subjectName, " +
                             "CONCAT(ut.firstName, ' ', ut.lastName) AS tutorName " +
                             "FROM booking b " +
                             "JOIN tutorsession ts ON b.sessionID = ts.sessionID " +
                             "JOIN subject s ON ts.subjectID = s.subjectID " +
                             "JOIN user ut ON ut.userID = ts.tutorID " +
                             "WHERE b.studentID = ?"
             )) {

            stmt.setInt(1, studentID);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                // Create base Booking object using parent class constructor
                Booking booking = new Booking(
                        rs.getInt("studentID"),
                        rs.getString("sessionID"),
                        rs.getString("sessionMode"),
                        rs.getString("bookingStatus"),
                        rs.getDouble("sessionPrice")
                );

                // Create BookingDetails with extended information
                BookingDetails details = new BookingDetails(
                        rs.getString("subjectName"),
                        rs.getString("tutorName"),
                        rs.getString("sessionDate"),
                        rs.getString("sessionTime"),
                        rs.getInt("sessionDuration")
                );

                // Set the inherited booking fields
                details.setStudentID(booking.getStudentID());
                details.setSessionID(booking.getSessionID());
                details.setSessionMode(booking.getSessionMode());
                details.setBookingStatus(booking.getBookingStatus());
                details.setSessionPrice(booking.getSessionPrice());

                bookings.add(details);
            }

        } catch (SQLException e) {
            throw new RemoteException("Database error while viewing bookings: " + e.getMessage());
        }
        return bookings;
    }

    @Override
    public List<TutorSession> viewAvailableSessions() throws RemoteException {
        List<TutorSession> sessions = new ArrayList<>();
        String query = "SELECT \n" +
                "    ts.sessionID, \n" +
                "    ts.tutorID, \n" +
                "    ts.subjectID, \n" +
                "    ts.sessionStatus, \n" +
                "    ts.sessionDate, \n" +
                "    ts.sessionTime, \n" +
                "    ts.sessionDuration, \n" +
                "    ts.numberOfStudents, \n" +
                "    ts.maximumStudents, \n" +
                "    ts.sessionPrice, \n" +
                "    ts.sessionMode, \n" +
                "    s.subjectLevel, \n" +
                "    s.subjectName \n" +
                "FROM tutorsession ts \n" +
                "JOIN subject s ON ts.subjectID = s.subjectID \n" +
                "WHERE ts.sessionStatus IN ('In Progress', 'Scheduled')";

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
    public Booking modifyBooking(int studentID, String sessionID, String newSessionMode, String newBookingStatus, double newSessionPrice, String newSessionDate, String newSessionTime) throws RemoteException {
        try (Connection conn = DatabaseConnection.setCon();
             PreparedStatement stmt = conn.prepareStatement(
                     "UPDATE booking b " +
                             "JOIN tutorsession ts ON b.sessionID = ts.sessionID " +
                             "SET b.sessionMode = ?, b.bookingStatus = ?, b.sessionPrice = ?, ts.sessionDate = ?, ts.sessionTime = ? " +
                             "WHERE b.studentID = ? AND b.sessionID = ?"
             )) {

            // Set parameters for the update
            stmt.setString(1, newSessionMode);
            stmt.setString(2, newBookingStatus);
            stmt.setDouble(3, newSessionPrice);
            stmt.setString(4, newSessionDate);
            stmt.setString(5, newSessionTime);
            stmt.setInt(6, studentID);
            stmt.setString(7, sessionID);

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                return new Booking(studentID, sessionID, newSessionMode, newBookingStatus, newSessionPrice);
            }

        } catch (SQLException e) {
            throw new RemoteException("Database error while modifying booking: " + e.getMessage());
        }
        return null;
    }

    @Override
    public boolean cancelBooking(String sessionID) throws RemoteException {
        try (Connection conn = DatabaseConnection.setCon();
             PreparedStatement stmt = conn.prepareStatement(
                     "UPDATE booking SET bookingStatus = 'Cancelled' WHERE sessionID = ?"
             )) {
            stmt.setString(1, sessionID);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0; // Return true if a booking was cancelled
        } catch (SQLException e) {
            throw new RemoteException("Database error while cancelling booking: " + e.getMessage());
        }
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
    public List<PaymentDetails> viewPaymentHistory(int studentID) throws RemoteException {
        List<PaymentDetails> paymentHistory = new ArrayList<>();
        System.out.println("[SERVER] Executing SQL query for student ID: " + studentID);

        String query = "SELECT p.paymentID, p.studentID, p.amount, p.paymentDate, p.paymentTime, " +
                "    p.paymentMethod, b.sessionMode, " +
                "    GROUP_CONCAT(s.subjectName SEPARATOR ', ') AS subjectNames, " +
                "    b.bookingStatus " +
                "FROM payment p " +
                "JOIN booking b ON p.studentID = b.studentID " +
                "JOIN tutorsession ts ON b.sessionID = ts.sessionID " +
                "JOIN subject s ON ts.subjectID = s.subjectID " +
                "WHERE p.studentID = ? " +
                "GROUP BY p.paymentID, p.studentID, p.amount, p.paymentDate, p.paymentTime, p.paymentMethod, b.sessionMode, b.bookingStatus " +
                "ORDER BY p.paymentDate DESC";

        try (Connection conn = DatabaseConnection.setCon();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, studentID);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                PaymentDetails paymentDetails = new PaymentDetails(
                        rs.getString("paymentID"),
                        rs.getString("studentID"),
                        rs.getDate("paymentDate").toLocalDate(),
                        rs.getTime("paymentTime").toLocalTime(),
                        rs.getString("paymentMethod"),
                        rs.getDouble("amount"),
                        rs.getString("paymentID"), // InvoiceID is the same as paymentID
                        rs.getString("subjectNames"), // Use the concatenated subject names
                        rs.getString("sessionMode"),
                        rs.getString("bookingStatus") // Retrieve the actual booking status from the database
                );
                paymentHistory.add(paymentDetails);
            }
            System.out.println("[SERVER] Retrieved payment history size: " + paymentHistory.size());
        } catch (SQLException e) {
            throw new RemoteException("Database error while fetching payment history: " + e.getMessage());
        }
        return paymentHistory;
    }

    @Override
    public List<BalanceDetails> viewStudentBalanceDetails(int studentID) throws RemoteException {
        List<BalanceDetails> balanceDetailsList = new ArrayList<>();
        String query = "SELECT u.userID, u.firstName, u.lastName, u.phoneNumber, u.email, u.role, s.balance, s.academicLevel," +
                "       ts.sessionDate, ts.sessionTime, ts.sessionDuration, sub.subjectName AS courseName, b.sessionMode," +
                "       CONCAT(t.firstName, ' ', t.lastName) AS tutorName, b.bookingStatus" +
                "       FROM user u" +
                "       JOIN student s ON u.userID = s.studentID" +
                "       JOIN booking b ON u.userID = b.studentID " +
                "       JOIN tutorsession ts ON b.sessionID = ts.sessionID " +
                "       JOIN subject sub ON ts.subjectID = sub.subjectID " +
                "       JOIN user t ON ts.tutorID = t.userID" +
                "       WHERE u.userID = ?";

        try (Connection conn = DatabaseConnection.setCon();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, studentID);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                BalanceDetails details = new BalanceDetails(
                        rs.getString("userID"),
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getLong("phoneNumber"),
                        rs.getString("email"),
                        rs.getString("role"),
                        rs.getDouble("balance"),
                        rs.getString("academicLevel"),
                        rs.getDate("sessionDate").toLocalDate(),
                        rs.getTime("sessionTime").toLocalTime(),
                        rs.getInt("sessionDuration"),
                        rs.getString("courseName"),
                        rs.getString("sessionMode"),
                        rs.getString("tutorName"),
                        rs.getString("bookingStatus")
                );
                balanceDetailsList.add(details);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RemoteException("Error fetching balance details", e);
        }
        return balanceDetailsList;
    }

    @Override
    public Payment createPayment(String studentID, double amount, String paymentMethod) throws RemoteException {
        // first we get the latest payment ID from database
        String lastPaymentID = getLastPaymentID();
        String newPaymentID = incrementPaymentID(lastPaymentID);

        LocalDateTime paymentDateTime = LocalDateTime.now();
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();
        String paymentDate = paymentDateTime.toLocalDate().toString();
        String paymentTime = paymentDateTime.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm:ss"));

        Payment newPayment = new Payment(newPaymentID, studentID, date, time, paymentMethod, amount);

        try (Connection conn = DatabaseConnection.setCon();
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO payment (paymentID, studentID, amount, paymentDate, paymentTime, paymentMethod) " +
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
             ResultSet rs = stmt.executeQuery("SELECT paymentID FROM payment ORDER BY paymentID DESC LIMIT 1")) {

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

        // increment and format back to three digits
        number++;
        return prefix + String.format("%03d", number);
    }
}
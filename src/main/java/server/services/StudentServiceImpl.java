package server.services;

import server.database.DatabaseConnection;
import shared.classes.*;
import shared.interfaces.StudentService;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StudentServiceImpl extends UnicastRemoteObject implements Remote, StudentService, Serializable {
    private static final long serialVersionUID = 1L;

    public StudentServiceImpl() throws RemoteException {
        super();
    }

    @Override
    public Booking createBooking(String studentID, String sessionID, String sessionMode, String bookingStatus,
                                 double sessionPrice) throws RemoteException {
        // Check for overlapping bookings before creating a new booking
        if (hasOverlappingBooking(studentID, sessionID)) {
            throw new RemoteException("Cannot reserve session: You have an active session that overlaps with this time slot.");
        }

        try (Connection conn = DatabaseConnection.setCon();
             CallableStatement cstmt = conn.prepareCall("{CALL createBooking(?, ?, ?, ?, ?)}")) {

            cstmt.setString(1, studentID);
            cstmt.setString(2, sessionID);
            cstmt.setString(3, sessionMode);
            cstmt.setDouble(4, sessionPrice);
            cstmt.setString(5, bookingStatus);

            cstmt.executeUpdate();

            // Verify booking was actually created
            try (PreparedStatement ps = conn.prepareStatement(
                    "SELECT * FROM booking WHERE studentID = ? AND sessionID = ?")) {
                ps.setString(1, studentID);
                ps.setString(2, sessionID);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    return new Booking(studentID, sessionID, sessionMode, bookingStatus, sessionPrice);
                }
                throw new RemoteException("Booking failed - session may be full");
            }
        } catch (SQLException e) {
            throw new RemoteException("Database error: " + e.getMessage());
        }
    }

    @Override
    public boolean cancelBooking(String sessionID) throws RemoteException {
        try (Connection conn = DatabaseConnection.setCon()) {
            conn.setAutoCommit(false);
            try {
                // Update booking status to Cancelled
                try (PreparedStatement stmt = conn.prepareStatement(
                        "UPDATE booking SET bookingStatus = 'Cancelled' WHERE sessionID = ?")) {
                    stmt.setString(1, sessionID);
                    stmt.executeUpdate();
                }

                // Decrement numberOfStudents in tutorsession
                try (PreparedStatement stmt = conn.prepareStatement(
                        "UPDATE tutorsession SET numberOfStudents = numberOfStudents - 1 WHERE sessionID = ? AND numberOfStudents > 0")) {
                    stmt.setString(1, sessionID);
                    int rowsAffected = stmt.executeUpdate();
                    conn.commit();
                    return rowsAffected > 0;
                }
            } catch (SQLException e) {
                conn.rollback();
                throw new RemoteException("Database error while cancelling booking: " + e.getMessage());
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RemoteException("Database connection error: " + e.getMessage());
        }
    }

    // Method to check for overlapping bookings
    private boolean hasOverlappingBooking(String studentID, String sessionID) throws RemoteException {
        try (Connection conn = DatabaseConnection.setCon()) {
            // Get the session details for the session to be booked
            String sessionQuery = "SELECT sessionDate, sessionTime, sessionDuration FROM tutorsession WHERE sessionID = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sessionQuery)) {
                stmt.setString(1, sessionID);
                ResultSet rs = stmt.executeQuery();
                if (!rs.next()) {
                    throw new RemoteException("Session not found");
                }

                LocalDate sessionDate = rs.getDate("sessionDate").toLocalDate();
                LocalTime sessionTime = rs.getTime("sessionTime").toLocalTime();
                int duration = rs.getInt("sessionDuration");
                LocalDateTime sessionStart = LocalDateTime.of(sessionDate, sessionTime);
                LocalDateTime sessionEnd = sessionStart.plusMinutes(duration);

                // Check for overlapping Approved bookings on the same day
                String overlapQuery = "SELECT ts.sessionDate, ts.sessionTime, ts.sessionDuration " +
                        "FROM booking b " +
                        "JOIN tutorsession ts ON b.sessionID = ts.sessionID " +
                        "WHERE b.studentID = ? AND b.bookingStatus = 'Approved' " +
                        "AND ts.sessionDate = ? AND ts.sessionID != ?";
                try (PreparedStatement overlapStmt = conn.prepareStatement(overlapQuery)) {
                    overlapStmt.setString(1, studentID);
                    overlapStmt.setDate(2, java.sql.Date.valueOf(sessionDate));
                    overlapStmt.setString(3, sessionID);
                    ResultSet overlapRs = overlapStmt.executeQuery();

                    while (overlapRs.next()) {
                        LocalDate existingDate = overlapRs.getDate("sessionDate").toLocalDate();
                        LocalTime existingTime = overlapRs.getTime("sessionTime").toLocalTime();
                        int existingDuration = overlapRs.getInt("sessionDuration");
                        LocalDateTime existingStart = LocalDateTime.of(existingDate, existingTime);
                        LocalDateTime existingEnd = existingStart.plusMinutes(existingDuration);

                        // Check if the sessions overlap
                        if (!(sessionEnd.isBefore(existingStart) || sessionStart.isAfter(existingEnd))) {
                            return true; // Overlap found
                        }
                    }
                }
            }
        } catch (SQLException e) {
            throw new RemoteException("Database error while checking for overlapping bookings: " + e.getMessage());
        }
        return false; // No overlap found
    }

    @Override
    public List<BookingDetails> viewStudentBooking(int studentID) throws RemoteException {
        List<BookingDetails> bookings = new ArrayList<>();
        try (Connection conn = DatabaseConnection.setCon();
             CallableStatement cstmt = conn.prepareCall("{CALL viewStudentBooking(?)}")) {

            cstmt.setInt(1, studentID);
            ResultSet rs = cstmt.executeQuery();

            while (rs.next()) {
                Booking booking = new Booking(
                        rs.getString("studentID"),
                        rs.getString("sessionID"),
                        rs.getString("sessionMode"),
                        rs.getString("bookingStatus"),
                        rs.getDouble("sessionPrice")
                );

                BookingDetails details = new BookingDetails(
                        rs.getString("subjectName"),
                        rs.getString("tutorName"),
                        rs.getString("sessionDate"),
                        rs.getString("sessionTime"),
                        rs.getInt("sessionDuration")
                );

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
    public List<TutorSession> viewAvailableSessions(String studentID) throws RemoteException {
        List<TutorSession> sessions = new ArrayList<>();
        try (Connection conn = DatabaseConnection.setCon();
             CallableStatement stmt = conn.prepareCall("{CALL ViewAvailableSessions(?)}")) {

            stmt.setString(1, studentID);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                TutorSession session = new TutorSession(
                        rs.getString("sessionID"),
                        rs.getString("tutorID"),
                        rs.getString("subjectID"),
                        rs.getString("subjectName"),
                        rs.getDate("sessionDate").toLocalDate(),
                        rs.getTime("sessionTime").toLocalTime(),
                        rs.getInt("sessionDuration"),
                        rs.getString("academicLevel"),
                        rs.getString("sessionStatus"),
                        rs.getInt("numberOfStudents"),
                        rs.getInt("maximumStudents"),
                        rs.getDouble("sessionPrice"),
                        rs.getString("sessionMode")
                );
                sessions.add(session);
            }
        } catch (SQLException e) {
            System.err.println("[SERVER | "+ new Date()+ "] Database error: " + e.getMessage());
            throw new RemoteException("Database error: " + e.getMessage());
        }
        System.out.println("[SERVER | "+ new Date()+ "] Found " + sessions.size() + " available sessions");
        return sessions;
    }

    @Override
    public Booking modifyBooking(String studentID, String sessionID, String newSessionMode, String newBookingStatus,
                                 double newSessionPrice, String newSessionDate, String newSessionTime) throws RemoteException {
        try (Connection conn = DatabaseConnection.setCon();
             CallableStatement cstmt = conn.prepareCall("{CALL modifyBooking(?, ?, ?, ?, ?, ?, ?)}")) {

            cstmt.setString(1, studentID);
            cstmt.setString(2, sessionID);
            cstmt.setString(3, newSessionMode);
            cstmt.setString(4, newBookingStatus);
            cstmt.setDouble(5, newSessionPrice);
            cstmt.setString(6, newSessionDate);
            cstmt.setString(7, newSessionTime);

            int rowsAffected = cstmt.executeUpdate();

            if (rowsAffected > 0) {
                return new Booking(studentID, sessionID, newSessionMode, newBookingStatus, newSessionPrice);
            }
        } catch (SQLException e) {
            throw new RemoteException("Database error while modifying booking: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Subject> viewSubject() throws RemoteException {
        System.out.println("[SERVER | "+ new Date()+ "] Fetching subjects from database...");
        List<Subject> subjects = new ArrayList<>();
        try (Connection conn = DatabaseConnection.setCon();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM subject")) {

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Subject subject = new Subject(
                        rs.getString("subjectID"),
                        rs.getString("subjectName"),
                        rs.getString("subjectDescription"),
                        rs.getString("academicLevel")
                );
                subjects.add(subject);
                System.out.println("[SERVER | "+ new Date()+ "] Fetched subject: " + subject);
            }

            if (subjects.isEmpty()) {
                System.out.println("[SERVER | "+ new Date()+ "] No subjects found in the database.");
            }

        } catch (SQLException e) {
            throw new RemoteException("Database error while fetching subjects: " + e.getMessage());
        }
        return subjects;
    }

    @Override
    public List<LessonPlan> viewHighSchoolLessonPlan() throws RemoteException {
        System.out.println("[SERVER | "+ new Date()+ "] Fetching high school lesson plans from database...");
        List<LessonPlan> lessonPlans = new ArrayList<>();
        try (Connection conn = DatabaseConnection.setCon();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT * FROM lessonplan WHERE subjectID LIKE 'HS%'")) {

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                LessonPlan lessonPlan = new LessonPlan(
                        rs.getString("lessonPlanID"),
                        rs.getString("subjectID"),
                        rs.getString("objectives"),
                        rs.getString("topicsCovered")
                );
                lessonPlans.add(lessonPlan);
                System.out.println("[SERVER | "+ new Date()+ "] Fetched high school lesson plan: " + lessonPlan);
            }

            if (lessonPlans.isEmpty()) {
                System.out.println("[SERVER | "+ new Date()+ "] No high school lesson plans found in the database.");
            }

        } catch (SQLException e) {
            throw new RemoteException("Database error while fetching high school lesson plans: " + e.getMessage());
        }
        return lessonPlans;
    }

    @Override
    public List<LessonPlan> viewCollegeLessonPlan() throws RemoteException {
        System.out.println("[SERVER | "+ new Date()+ "] Fetching college lesson plans from database...");
        List<LessonPlan> lessonPlans = new ArrayList<>();
        try (Connection conn = DatabaseConnection.setCon();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT * FROM lessonplan WHERE subjectID LIKE 'IT%'")) {

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                LessonPlan lessonPlan = new LessonPlan(
                        rs.getString("lessonPlanID"),
                        rs.getString("subjectID"),
                        rs.getString("objectives"),
                        rs.getString("topicsCovered")
                );
                lessonPlans.add(lessonPlan);
                System.out.println("[SERVER | "+ new Date()+ "] Fetched college lesson plan: " + lessonPlan);
            }

            if (lessonPlans.isEmpty()) {
                System.out.println("[SERVER | "+ new Date()+ "] No college lesson plans found in the database.");
            }

        } catch (SQLException e) {
            throw new RemoteException("Database error while fetching college lesson plans: " + e.getMessage());
        }
        return lessonPlans;
    }

    @Override
    public List<PaymentDetails> viewPaymentHistory(String studentID) throws RemoteException {
        List<PaymentDetails> paymentHistory = new ArrayList<>();
        System.out.println("[SERVER | "+ new Date()+ "] Executing SQL query for student ID: " + studentID);

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

            stmt.setString(1, studentID);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                PaymentDetails paymentDetails = new PaymentDetails(
                        rs.getString("paymentID"),
                        rs.getString("studentID"),
                        rs.getDate("paymentDate").toLocalDate(),
                        rs.getTime("paymentTime").toLocalTime(),
                        rs.getString("paymentMethod"),
                        rs.getDouble("amount"),
                        rs.getString("paymentID"),
                        rs.getString("subjectNames"),
                        rs.getString("sessionMode"),
                        rs.getString("bookingStatus")
                );
                paymentHistory.add(paymentDetails);
            }
            System.out.println("[SERVER | "+ new Date()+ "] Retrieved payment history size: " + paymentHistory.size());
        } catch (SQLException e) {
            throw new RemoteException("Database error while fetching payment history: " + e.getMessage());
        }
        return paymentHistory;
    }

    @Override
    public List<BalanceDetails> viewStudentBalanceDetails(String studentID) throws RemoteException {
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

            stmt.setString(1, studentID);
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
    public Payment createPayment(String studentId, double amount, String paymentMethod)
            throws RemoteException {

        Connection conn = null;
        CallableStatement callableStmt = null;
        try {
            conn = DatabaseConnection.setCon();
            if (conn == null) {
                throw new RemoteException("Database connection failed");
            }

            conn.setAutoCommit(false);
            String paymentId = getNextPaymentId(conn);

            try {
                callableStmt = conn.prepareCall("{call createPayment(?, ?, ?)}");
                callableStmt.setString(1, studentId);
                callableStmt.setDouble(2, amount);
                callableStmt.setString(3, paymentMethod);
                callableStmt.executeUpdate();
            } finally {
                if (callableStmt != null) {
                    callableStmt.close();
                }
            }

            conn.commit();
            return new Payment(paymentId, studentId, LocalDate.now(),
                    LocalTime.now(), paymentMethod, amount);

        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                // Log rollback error
            }
            throw new RemoteException("Payment failed: " + e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                // Log connection close error
            }
        }
    }

    private String getNextPaymentId(Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(
                     "SELECT MAX(paymentID) FROM payment")) {

            if (rs.next()) {
                String maxId = rs.getString(1);
                return maxId != null ?
                        "P" + String.format("%03d", Integer.parseInt(maxId.substring(1)) + 1)
                        : "P001";
            }
            return "P001";
        }
    }

    @Override
    public boolean updateStudentBalance(String studentId, double amount) throws RemoteException {
        try (Connection conn = DatabaseConnection.setCon();
             PreparedStatement stmt = conn.prepareStatement(
                     "UPDATE student SET balance = balance + ? WHERE studentID = ?")) {

            stmt.setDouble(1, amount);
            stmt.setString(2, studentId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RemoteException("Balance update failed: " + e.getMessage());
        }
    }

    @Override
    public double getStudentBalance(String studentID) throws RemoteException {
        try (Connection conn = DatabaseConnection.setCon();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT balance FROM student WHERE studentID = ?")) {

            stmt.setString(1, studentID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getDouble("balance");
            } else {
                throw new RemoteException("Student not found");
            }
        } catch (SQLException e) {
            throw new RemoteException("Database error while fetching balance: " + e.getMessage());
        }
    }

    @Override
    public Tutor getTutorDetails(String tutorId) throws RemoteException {
        String query = "SELECT u.userID, u.firstName, u.lastName, u.phoneNumber, " +
                "u.email, u.role, u.password, t.expertise " +
                "FROM user u JOIN tutor t ON u.userID = t.tutorID " +
                "WHERE u.userID = ?";

        try (Connection conn = DatabaseConnection.setCon();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, tutorId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Tutor(
                        rs.getString("userID"),
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getLong("phoneNumber"),
                        rs.getString("email"),
                        rs.getString("role"),
                        rs.getString("password"),
                        rs.getString("expertise")
                );
            }
            throw new RemoteException("Tutor not found");
        } catch (SQLException e) {
            throw new RemoteException("Database error: " + e.getMessage());
        }
    }
}
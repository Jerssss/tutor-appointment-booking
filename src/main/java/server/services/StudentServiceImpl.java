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
import java.util.List;

public class StudentServiceImpl extends UnicastRemoteObject implements Remote, StudentService, Serializable {
    private static final long serialVersionUID = 1L;

    public StudentServiceImpl() throws RemoteException {
        super();
    }

    @Override
    public Booking createBooking(String studentID, String sessionID, String sessionMode, String bookingStatus,
                                 double sessionPrice) throws RemoteException {
        // Validate bookingStatus against allowed values
        if (!isValidBookingStatus(bookingStatus)) {
            throw new RemoteException("Invalid booking status. Must be 'Pending', 'Approved', or 'Cancelled'");
        }

        if (!isValidSessionMode(sessionMode)) {
            throw new RemoteException("Invalid session mode. Must be 'Face-to-Face' or 'Online'");
        }

        try (Connection conn = DatabaseConnection.setCon();
             CallableStatement cstmt = conn.prepareCall("{call CreateBooking(?, ?, ?, ?, ?)}")) {

            cstmt.setString(1, studentID);
            cstmt.setString(2, sessionID);
            cstmt.setString(3, sessionMode);
            cstmt.setDouble(4, sessionPrice);
            cstmt.setString(5, bookingStatus);

            cstmt.executeUpdate();
            return new Booking(studentID, sessionID, sessionMode, bookingStatus, sessionPrice);

        } catch (SQLException e) {
            throw new RemoteException("Database error: " + e.getMessage());
        }
    }

    private boolean isValidBookingStatus(String status) {
        return "Pending".equals(status) || "Approved".equals(status) || "Cancelled".equals(status);
    }

    private boolean isValidSessionMode(String mode) {
        return "Face-to-Face".equals(mode) || "Online".equals(mode);
    }

    @Override
    public List<BookingDetails> viewStudentBooking(int studentID) throws RemoteException {
        List<BookingDetails> bookings = new ArrayList<>();
        try (Connection conn = DatabaseConnection.setCon();
             CallableStatement cstmt = conn.prepareCall("{CALL ViewStudentBooking(?)}")) {

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
    public List<TutorSession> viewAvailableSessions() throws RemoteException {
        List<TutorSession> sessions = new ArrayList<>();

        try (Connection conn = DatabaseConnection.setCon();
             CallableStatement stmt = conn.prepareCall("{CALL ViewAvailableSessions()}");
             ResultSet rs = stmt.executeQuery()) {

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

                session.setAcademicLevel(rs.getString("academicLevel"));
                sessions.add(session);
            }
            return sessions;
        } catch (SQLException e) {
            throw new RemoteException("Database error: " + e.getMessage());
        }
    }

    @Override
    public Booking modifyBooking(String studentID, String sessionID, String newSessionMode, String newBookingStatus,
                                 double newSessionPrice, String newSessionDate, String newSessionTime) throws RemoteException {
        try (Connection conn = DatabaseConnection.setCon();
             CallableStatement cstmt = conn.prepareCall("{call ModifyBooking(?, ?, ?, ?, ?, ?, ?)}")) {

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
    public boolean cancelBooking(String sessionID) throws RemoteException {
        try (Connection conn = DatabaseConnection.setCon();
             PreparedStatement stmt = conn.prepareStatement(
                     "UPDATE booking SET bookingStatus = 'Cancelled' WHERE sessionID = ?")) {

            stmt.setString(1, sessionID);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new RemoteException("Database error while cancelling booking: " + e.getMessage());
        }
    }

    @Override
    public List<Subject> viewSubject() throws RemoteException {
        System.out.println("[SERVER] Fetching subjects from database...");
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
    public List<PaymentDetails> viewPaymentHistory(String studentID) throws RemoteException {
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
            System.out.println("[SERVER] Retrieved payment history size: " + paymentHistory.size());
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
        try (Connection conn = DatabaseConnection.setCon()) {
            conn.setAutoCommit(false);

            try {
                String paymentId = getNextPaymentId(conn);
                System.out.println("[PAYMENT] Generated payment ID: " + paymentId);

                try (PreparedStatement paymentStmt = conn.prepareStatement(
                        "INSERT INTO payment (paymentID, studentID, amount, paymentDate, paymentTime, paymentMethod) " +
                                "VALUES (?, ?, ?, CURDATE(), CURTIME(), ?)")) {
                    paymentStmt.setString(1, paymentId);
                    paymentStmt.setString(2, studentId);
                    paymentStmt.setDouble(3, amount);
                    paymentStmt.setString(4, paymentMethod);
                    paymentStmt.executeUpdate();
                }

                conn.commit();
                return new Payment(paymentId, studentId, LocalDate.now(),
                        LocalTime.now(), paymentMethod, amount);

            } catch (SQLException e) {
                conn.rollback();
                System.err.println("[ERROR] Transaction rolled back: " + e.getMessage());
                throw new RemoteException("Payment failed: " + e.getMessage());
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RemoteException("Database connection error: " + e.getMessage());
        }
    }

    private String getNextPaymentId(Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(
                     "SELECT MAX(paymentID) FROM payment FOR UPDATE")) {

            if (rs.next()) {
                String maxId = rs.getString(1);
                if (maxId != null) {
                    int num = Integer.parseInt(maxId.substring(1)) + 1;
                    return "P" + String.format("%03d", num);
                }
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
}
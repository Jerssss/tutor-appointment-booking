package server.services;

import shared.classes.*;
import shared.interfaces.TutorService;
import server.database.DatabaseConnection;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TutorServiceImpl extends UnicastRemoteObject implements TutorService, Serializable {
    private static final long serialVersionUID = 1L; // Add a serialVersionUID
    private static final Connection con = DatabaseConnection.setCon();
    private static Statement stmt;

    public TutorServiceImpl() throws RemoteException {
        super();
    }

    public String generateNewLessonPlanID() {
        String query = "SELECT lessonPlanID FROM lessonplan ORDER BY lessonPlanID DESC LIMIT 1";
        String latestLessonPlanID = ""; // default

        try {
            stmt = con.createStatement();
            ResultSet resultSet = stmt.executeQuery(query);

            if (resultSet.next()) {
                latestLessonPlanID = resultSet.getString("lessonPlanID");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // Extract numeric part and increment
        String numericPart = latestLessonPlanID.replaceAll("[^0-9]", "");
        int nextID = Integer.parseInt(numericPart) + 1;
        return "LP" + nextID;
    }


    @Override
    public void addLessonPlan(LessonPlan newLessonPlan) throws RemoteException, SQLException {
        String query = "INSERT INTO lessonplan (lessonPlanID, subjectID, subjectName, objectives, topicsCovered) VALUES (?, ?, ?, ?, ?);";
        try {
            String newLessonPlanID = generateNewLessonPlanID();

            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, newLessonPlanID);
            preparedStatement.setString(2, newLessonPlan.getSubjectID());
            preparedStatement.setString(3, newLessonPlan.getSubjectName()); // Add subjectName
            preparedStatement.setString(4, newLessonPlan.getObjectives());
            preparedStatement.setString(5, newLessonPlan.getTopicsCovered());
            preparedStatement.executeUpdate();
        } catch (SQLException e1) {
            if (con != null) con.rollback(); // Rollback on error
            e1.printStackTrace();
        } catch (Exception e2) {
            e2.printStackTrace();
        } finally {
            if (con != null) con.setAutoCommit(true);
        }
    }

    @Override
    public List<TutorSession> viewSessionList(String tutorID) throws RemoteException {
        List<TutorSession> tutorSessions = new ArrayList<>();
        System.out.println("Retrieving sessions for tutor ID: " + tutorID);

        String query = "SELECT ts.sessionID, ts.tutorID, ts.subjectID, s.subjectName, ts.sessionStatus, " +
                "ts.sessionDate, ts.sessionTime, ts.sessionDuration, ts.numberOfStudents, " +
                "ts.maximumStudents, ts.sessionPrice, b.sessionMode " +
                "FROM tutorsession ts " +
                "JOIN subject s ON ts.subjectID = s.subjectID " +
                "LEFT JOIN booking b ON ts.sessionID = b.sessionID " +
                "WHERE ts.tutorID = ?";

        try (Connection conn = DatabaseConnection.setCon();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, tutorID); // set tutorID
            ResultSet resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                // Create TutorSession object and add to the list
                TutorSession session = new TutorSession(
                        resultSet.getString("sessionID"),
                        resultSet.getString("tutorID"),
                        resultSet.getString("subjectID"),
                        resultSet.getString("subjectName"),
                        resultSet.getDate("sessionDate").toLocalDate(),
                        resultSet.getTime("sessionTime").toLocalTime(),
                        resultSet.getInt("sessionDuration"),
                        resultSet.getString("sessionStatus"),
                        resultSet.getInt("numberOfStudents"),
                        resultSet.getInt("maximumStudents"),
                        resultSet.getDouble("sessionPrice"),
                        resultSet.getString("sessionMode")
                );
                tutorSessions.add(session);
                System.out.println("Retrieved session: " + session);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RemoteException("Error retrieving session list: " + e.getMessage(), e);
        }
        return tutorSessions;
    }

    @Override
    public List<Student> getStudentsBySession(String sessionID) throws RemoteException {
        List<Student> students = new ArrayList<>();
        String query = "SELECT u.userID, u.firstName, u.lastName " +
                "FROM booking b " +
                "JOIN student s ON b.studentID = s.studentID " +
                "JOIN user u ON s.studentID = u.userID " +
                "WHERE b.sessionID = ?";

        try (Connection conn = DatabaseConnection.setCon();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, sessionID);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String userID = rs.getString("userID");
                String firstName = rs.getString("firstName");
                String lastName = rs.getString("lastName");

                Student student = new Student(userID, firstName, lastName);
                students.add(student);
            }
        } catch (SQLException e) {
            throw new RemoteException("Database error while retrieving students: " + e.getMessage());
        }
        return students;
    }

    @Override
    public List<LessonPlan> viewLessonPlanByTutor(String tutorID) throws RemoteException {
        List<LessonPlan> lessonPlanList = new ArrayList<>();

        String query = "SELECT lp.lessonPlanID, s.subjectID, s.subjectName, lp.objectives, lp.topicsCovered " +
                "FROM lessonplan lp " +
                "JOIN tutorsession ts ON lp.subjectID = ts.subjectID " +
                "JOIN subject s ON lp.subjectID = s.subjectID " +
                "WHERE ts.tutorID = ? and lp.visibility = 'Available'";

        try (Connection conn = DatabaseConnection.setCon();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, tutorID);
            ResultSet resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                String lessonPlanID = resultSet.getString("lessonPlanID");
                String subjectID = resultSet.getString("subjectID");
                String subjectName = resultSet.getString("subjectName");
                String objectives = resultSet.getString("objectives");
                String topicsCovered = resultSet.getString("topicsCovered");

                // Debugging output to check values
                System.out.println("Lesson Plan ID: " + lessonPlanID);
                System.out.println("Subject ID: " + subjectID);
                System.out.println("Subject Name: " + subjectName); // Check this value
                System.out.println("Objectives: " + objectives);
                System.out.println("Topics Covered: " + topicsCovered);

                // Create LessonPlan object with subjectID and subjectName
                LessonPlan lessonPlan = new LessonPlan(lessonPlanID, subjectID, subjectName, objectives, topicsCovered);
                lessonPlanList.add(lessonPlan);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RemoteException("Error retrieving lesson plans for tutor: " + e.getMessage());
        }
        return lessonPlanList;
    }

    @Override
    public void modifyLessonPlan(String lessonPlanID, String newObjectives, String newTopicsCovered) throws RemoteException {
        String query = "UPDATE lessonplan SET objectives = ?, topicsCovered = ? WHERE lessonPlanID = ?";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, newObjectives);
            preparedStatement.setString(2, newTopicsCovered);
            preparedStatement.setString(3, lessonPlanID);
            preparedStatement.executeUpdate();
        } catch (SQLException e1) {
            e1.printStackTrace();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    @Override
    public void deleteLessonPlan(String lessonPlanID) throws RemoteException {
        String sql = "DELETE FROM lessonplan WHERE lessonPlanID = ?";

        try (Connection conn = DatabaseConnection.setCon();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, lessonPlanID);
            stmt.executeUpdate();

            System.out.println("[SERVER] Deleted lesson plan with ID: " + lessonPlanID);
        } catch (SQLException e) {
            System.err.println("[SERVER ERROR] Failed to delete lesson plan: " + e.getMessage());
        }
    }

    @Override
    public TutorSession getSessionDetails(String sessionID) throws RemoteException {
        TutorSession session = null;
        String query = "SELECT ts.sessionID, ts.tutorID, ts.subjectID, s.subjectName, ts.sessionStatus, " +
                "ts.sessionDate, ts.sessionTime, ts.sessionDuration, ts.numberOfStudents, " +
                "ts.maximumStudents, ts.sessionPrice, b.sessionMode, ts.sessionType " +
                "FROM tutorsession ts " +
                "JOIN subject s ON ts.subjectID = s.subjectID " +
                "LEFT JOIN booking b ON ts.sessionID = b.sessionID " +
                "WHERE ts.sessionID = ?";

        try (Connection conn = DatabaseConnection.setCon();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, sessionID);
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                session = new TutorSession(
                        resultSet.getString("sessionID"),
                        resultSet.getString("tutorID"),
                        resultSet.getString("subjectID"),
                        resultSet.getString("subjectName"),
                        resultSet.getDate("sessionDate").toLocalDate(),
                        resultSet.getTime("sessionTime").toLocalTime(),
                        resultSet.getInt("sessionDuration"),
                        resultSet.getString("sessionStatus"),
                        resultSet.getInt("numberOfStudents"),
                        resultSet.getInt("maximumStudents"),
                        resultSet.getDouble("sessionPrice"),
                        resultSet.getString("sessionMode"),
                        resultSet.getString("sessionType")
                );
                System.out.println("Retrieved session: " + session);
            }
        } catch (SQLException e) {
            throw new RemoteException("Database error while retrieving session details: " + e.getMessage());
        }
        return session;
    }
}
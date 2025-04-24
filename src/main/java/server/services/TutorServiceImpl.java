package server.services;

import shared.classes.Student;
import shared.classes.TutorSession;
import shared.interfaces.TutorService;
import shared.classes.LessonPlan;
import server.database.DatabaseConnection;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class TutorServiceImpl extends UnicastRemoteObject implements TutorService, Serializable {
    private static final long serialVersionUID = 1L; // Add a serialVersionUID
    private Connection connection;
    private static Connection con = DatabaseConnection.setCon();
    private static Statement stmt;

    public TutorServiceImpl() throws RemoteException {
        super();
        this.connection = DatabaseConnection.setCon();
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
        String query = "INSERT INTO lessonplan (lessonPlanID, subjectID, objectives, topicsCovered) VALUES (?, ?, ?, ?);";
        try {
            String newLessonPlanID = generateNewLessonPlanID();

            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, newLessonPlanID);
            preparedStatement.setString(2, newLessonPlan.getSubjectID());
            preparedStatement.setString(3, newLessonPlan.getObjectives());
            preparedStatement.setString(4, newLessonPlan.getTopicsCovered());
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
    public List<TutorSession> viewSessionList() throws RemoteException {
        List<TutorSession> tutorsessions = new ArrayList<>();
        String query = "SELECT ts.sessionID, ts.tutorID, ts.subjectID, s.subjectName, ts.sessionStatus, " +
                "ts.sessionDate, ts.sessionTime, ts.sessionDuration, ts.numberOfStudents, " +
                "ts.maximumStudents, ts.sessionPrice, b.sessionMode " +
                "FROM tutorsession ts " +
                "JOIN subject s ON ts.subjectID = s.subjectID " +
                "LEFT JOIN booking b ON ts.sessionID = b.sessionID"; // Use LEFT JOIN to include all sessions

        try {
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery(query);

            while (resultSet.next()) {
                String sessionID = resultSet.getString("sessionID");
                int tutorID = resultSet.getInt("tutorID");
                String subjectID = resultSet.getString("subjectID");
                String subjectName = resultSet.getString("subjectName");
                String sessionStatus = resultSet.getString("sessionStatus");
                LocalDate sessionDate = LocalDate.parse(resultSet.getDate("sessionDate").toString());
                LocalTime sessionTime = resultSet.getTime("sessionTime").toLocalTime();
                int sessionDuration = resultSet.getInt("sessionDuration");
                int numberOfStudents = resultSet.getInt("numberOfStudents");
                int maximumStudents = resultSet.getInt("maximumStudents");
                double sessionPrice = resultSet.getDouble("sessionPrice");
                String sessionMode = resultSet.getString("sessionMode");

                TutorSession session = new TutorSession(sessionID, String.valueOf(tutorID), subjectID, subjectName,
                        sessionDate, sessionTime, sessionDuration, sessionStatus, numberOfStudents, maximumStudents, sessionPrice, sessionMode);
                tutorsessions.add(session);
                System.out.println(session);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RemoteException("Error retrieving session list: " + e.getMessage(), e);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tutorsessions;
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
    public List<LessonPlan> viewLessonPlan() throws RemoteException{
        List<LessonPlan> lessonPlanList = new ArrayList<>();

        String query = "SELECT lp.lessonPlanID, s.subjectName, lp.objectives, lp.topicsCovered FROM lessonplan lp " +
                "NATURAL JOIN subject s";

        try (Connection conn = DatabaseConnection.setCon();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            ResultSet resultSet = stmt.executeQuery(query);

            while (resultSet.next()) {
                String lessonPlanID = resultSet.getString(1);
                String subjectName = resultSet.getString(2);
                String objectives = resultSet.getString(3);
                String topicsCovered = resultSet.getString(4);

                LessonPlan lessonPlan = new LessonPlan(lessonPlanID, subjectName, objectives, topicsCovered);
                lessonPlanList.add(lessonPlan);
            }
        } catch (SQLException e1) {
            e1.printStackTrace();
        } catch (Exception e2) {
            e2.printStackTrace();
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

}